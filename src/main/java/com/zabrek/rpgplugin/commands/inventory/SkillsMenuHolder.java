package com.zabrek.rpgplugin.commands.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SkillsMenuHolder implements InventoryHolder {
    private final Inventory inventory;

    public SkillsMenuHolder() {
        this.inventory = Bukkit.createInventory(
                this,
                9,
                Component.text("Select Your Skill", NamedTextColor.DARK_GRAY)
        );

        setupItems();
    }

    private void setupItems() {
        ItemStack lavaItem = new ItemStack(Material.MAGMA_CREAM);
        ItemMeta lavaMeta = lavaItem.getItemMeta();

        if (lavaMeta != null) {
            lavaMeta.displayName(Component.text("Class: Lava Leather", NamedTextColor.RED));
            lavaMeta.lore(List.of(
                    Component.text("• 25% chance of ignoring fire", NamedTextColor.GRAY),
                    Component.text("• It grants you Strength II when activated", NamedTextColor.GRAY)
            ));
            lavaItem.setItemMeta(lavaMeta);
        }

        ItemStack windItem = new ItemStack(Material.FEATHER);
        ItemMeta windMeta = windItem.getItemMeta();
        if (windMeta != null) {
            windMeta.displayName(Component.text("Class: Wind Leap", NamedTextColor.AQUA));
            windMeta.lore(List.of(
                    Component.text("• It makes you immune to fall damage")
            ));
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
