package com.zabrek.rpgplugin.listeners;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.commands.inventory.*;
import com.zabrek.rpgplugin.database.*;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class OnMenuClick implements Listener {
    private final DataManager dataManager;

    public OnMenuClick(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof SkillsMenuHolder)) return;

        event.setCancelled(true);
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        UUID id = player.getUniqueId();

        Skills selected = null;

        switch (clickedItem.getType()) {
            case MAGMA_CREAM -> selected = Skills.LAVA_LEATHER;
            case FEATHER -> selected = Skills.WIND_LEAP;
            default -> {
                break;
            }
        }

        if (selected != null) {
            PlayerData playerData = dataManager.getPlayerData(id);

            if (playerData != null) {
                playerData.setEquippedSkill(selected);
            }

            player.sendMessage(Component.text("You have selected the skill: ", NamedTextColor.GREEN)
                    .append(Component.text(selected.getDisplayName(), NamedTextColor.YELLOW)));
            player.closeInventory();
        }
    }
}
