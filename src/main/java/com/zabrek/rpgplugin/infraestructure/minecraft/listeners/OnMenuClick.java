package com.zabrek.rpgplugin.infraestructure.minecraft.listeners;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;

import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class OnMenuClick implements Listener {
    private final PlayerRepository dataManager;
    private final RPGPlugin plugin;

    public OnMenuClick(PlayerRepository dataManager, RPGPlugin plugin) {
        this.dataManager = dataManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof com.zabrek.rpgplugin.infraestructure.minecraft.commands.inventory.SkillsMenuHolder)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        ItemStack clickedItem = event.getCurrentItem();
        if (!clickedItem.hasItemMeta()) return;

        ItemMeta meta = clickedItem.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "selected_skill");

        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) return;
        String skillName = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        Player player = (Player) event.getWhoClicked();
        UUID id = player.getUniqueId();

        try {
            Skills selected = Skills.valueOf(skillName);
            PlayerData playerData = dataManager.getPlayerData(id);

            if (playerData != null) {
                playerData.setEquippedSkill(selected);
            }

            player.sendMessage(Component.text("You have selected the skill: ", NamedTextColor.GREEN)
                    .append(Component.text(selected.getDisplayName(), NamedTextColor.YELLOW)));

            player.closeInventory();
        } catch (IllegalArgumentException e) {
            player.sendMessage(Component.text("Error: Invalid skill data on item.", NamedTextColor.RED));
        }
    }
}
