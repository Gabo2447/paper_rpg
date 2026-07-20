package com.zabrek.rpgplugin.infraestructure.minecraft.commands.inventory;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.shared.DomainRules;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class RPGMenu implements InventoryHolder {
    protected Inventory inventory;
    protected RPGPlugin plugin;
    protected NamespacedKey skillKey;

    public RPGMenu(RPGPlugin plugin) {
        this.plugin = plugin;
        this.skillKey = new NamespacedKey(plugin, DomainRules.NAMESPACED_KEY);
    }

    public abstract void setMenuItems(Player player);
    public abstract void handleMenuClick(InventoryClickEvent event, PlayerRepository dataManager);

    @Override
    public Inventory getInventory() { return inventory; }

    /**
     * Calculate the absolute cell reference based on the row (1–6) and column (1–9)
     */
    protected int getSlot(int row, int column) {
        if (row < 1 || column < 1 || row > 6 || column > 9) {
            throw new IllegalArgumentException("Row (1–6) or Column (1–9) outside the boundaries of Minecraft");
        }
        return ((row - 1) * 9) + (column - 1);
    }

    /**
     *  Draws the menu using a visual character array.
     *  @param layout Array of strings (each row must contain 9 characters)
     *  @param ingredientMap Map that associates each character with its corresponding ItemStack
     */
    protected void setupLayout(String[] layout, Map<Character, ItemStack> ingredientMap) {
        for (int row = 0; row < layout.length; row++) {
            String rowString = layout[row];
            for (int col = 0; col < 9 && col < rowString.length(); col++) {
                char c = rowString.charAt(col);

                if (ingredientMap.containsKey(c)) {
                    int slot = row * 9 + col;
                    inventory.setItem(slot, ingredientMap.get(c));
                }
            }
        }
    }

    protected ItemStack createCustomHead(String urlBase64, Skills skill, List<Component> lore) {
        String gradientTag = getGradientForSkill(skill);
        Component title = MiniMessage.miniMessage().deserialize(gradientTag + skill.getDisplayName() + "</gradient>");
        String id = skill.getId();

        // Create item
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta == null) return head;

        // Set meta
        meta.getPersistentDataContainer().set(skillKey, PersistentDataType.STRING, id);

        meta.customName(title);
        meta.lore(lore);

        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "");
        profile.getProperties().add(new ProfileProperty("textures", urlBase64));

        meta.setPlayerProfile(profile);head.setItemMeta(meta);

        return head;
    }

    protected ItemStack createDecorativePanel(Material material) {
        ItemStack panel = new ItemStack(material);
        ItemMeta meta = panel.getItemMeta();

        if (meta != null) {
            meta.customName(Component.text(" "));
            meta.lore(new ArrayList<>());
            panel.setItemMeta(meta);
        }
        return panel;
    }

    private String getGradientForSkill(Skills skill) {
        return switch (skill) {
            case OBSIDIAN_SKIN -> "<gradient:#4f3b78:#927fbf>";           // Dark purple/obsidian to lavender
            case PIERCING_ARROW -> "<gradient:#f9d423:#ff4e50>";          // From bright yellow to arrow red
            case SEISMIC_IMPACT -> "<gradient:#FF416C:#FF4B2B>";          // Destructor Red / Magma
            case ADRENALINE_IN_THE_BLOOD -> "<gradient:#e65c00:#F9D423>"; // Blood-orange to blood-yellow
            case STREAK_OF_GOOD_LUCK -> "<gradient:#11998e:#38ef7d>";     // Emerald green / lucky clover
            default -> "<gradient:#ffffff:#aaaaaa>";                      // White/Gray, just in case
        };
    }

    protected ItemStack createEnchantedItem(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.customName(Component.text(name));
            meta.addEnchant(Enchantment.EFFICIENCY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

    protected List<Component> buildLore(String... lines) {
        return java.util.Arrays.stream(lines)
                .map(line -> MiniMessage.miniMessage().deserialize("<italic:false><gray>" + line + "</gray>"))
                .toList();
    }
}
