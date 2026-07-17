package com.zabrek.rpgplugin.commands.inventory;

import com.zabrek.rpgplugin.Skills;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkillsMenuHolder implements InventoryHolder {
    private final Inventory inventory;
    private final Plugin plugin;

    public SkillsMenuHolder(Plugin plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(
                this,
                9,
                Component.text("Select Your Skill", NamedTextColor.DARK_GRAY)
        );

        setupItems();
    }

    private void setupItems() {
        NamespacedKey key = new NamespacedKey(plugin, "selected_skill");

        // --- ITEM: LAVA ---
        ItemStack lavaItem = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta lavaMeta = lavaItem.getItemMeta();

        if (lavaMeta != null) {
            lavaMeta.displayName(Component.text("Class: Lava Leather", NamedTextColor.RED));
            lavaMeta.lore(List.of(
                    Component.text("• 25% chance of ignoring fire", NamedTextColor.GRAY),
                    Component.text("• It grants you Strength V when activated", NamedTextColor.GRAY)
            ));
            lavaMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Skills.LAVA_LEATHER.name());
            lavaItem.setItemMeta(lavaMeta);
        }

        // --- ITEM: WIND ---
        ItemStack windItem = new ItemStack(Material.FEATHER);
        ItemMeta windMeta = windItem.getItemMeta();
        if (windMeta != null) {
            windMeta.displayName(Component.text("Class: Wind Leap", NamedTextColor.AQUA));
            windMeta.lore(List.of(
                    Component.text("• It makes you immune to fall damage")
            ));
            windMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, Skills.WIND_LEAP.name());
            windItem.setItemMeta(windMeta);
        }

        this.inventory.setItem(3, lavaItem);
        this.inventory.setItem(5, windItem);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
