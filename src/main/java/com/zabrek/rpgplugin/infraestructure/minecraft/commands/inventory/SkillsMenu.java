package com.zabrek.rpgplugin.infraestructure.minecraft.commands.inventory;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.infraestructure.RPGPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsMenu extends RPGMenu {
    private final Player menuOwner;

    public SkillsMenu(Player player, RPGPlugin plugin) {
        super(plugin);

        this.inventory = Bukkit.createInventory(this, 9 * 4, "My Skills");
        this.menuOwner = player;
        setMenuItems(player);
    }

    @Override
    public void setMenuItems(Player player) {
        String[] layout = {
                "#       #",
                "# M F P #",
                "#  S O  #",
                "#       #"
        };

        Map<Character, ItemStack> ingredients = new HashMap<>();

        ingredients.put('#', createDecorativePanel(Material.YELLOW_STAINED_GLASS_PANE));

        String textureBlackstone = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDgyYmI1MThlMzg4NGQ2MDEyYmJkYmFiNjQ0ZGI3OTBhMjM4NDJkYTVjNmFmNjQ1ZjZlMjhmNmNlODMzNjc3NiJ9fX0=";
        String textureObsidian = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUxZmQyMjIzZjgyOTY1YjE5Y2ViYzliODk3MTBhOGRkNTExZDcxMjdmMDZhZWI1MTY0NjNmM2IxNDI5MGI5NyJ9fX0=";
        String textureHayTarget = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmVlNjg4N2E4OTZlZTdmMGRiNThkZGY3MmQxNjhlZDQ0M2NhOWYzNGQyNTIzNTE4ZmYyY2JmOTAxMmMyNDVlYyJ9fX0=";
        String textureLuckyBlock = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE0ZjhiM2JlYWZlNDYzY2MxNWE5ODMxZTliNDMyMGUxMDU1ZGQwMzYyYmVjNzU2NWJjNWQxYjM2NTdhMzFiOCJ9fX0=";
        String textureHeart = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE2ZjYyOTNlMjMxZmY0MzMxNGQ4NzUwN2QyZjMxMGE5MDFlYzM0NGIyY2RmM2ZkYWY0NGZiNjkyMTQxMDgyZSJ9fX0=";

        // Item - Seismic Impact
        List<Component> loreSeismicImpact = List.of(
                Component.text("• When you fall, you generate a Shockwave that deals damage as you fall")
        );

        // Item - Streak of Good Luck
        List<Component> loreStreakOfGoodLuck = List.of(
                Component.text("• There is a chance that when the block breaks, it will break along with all the ore")
        );

        // Item - Piercing Arrow
        List<Component> lorePiercingArrow = List.of(
                Component.text("• The bow has a chance that, when an arrow is fired, it will pass through all enemies")
        );

        // Item - Adrenaline in the Blood
        List<Component> loreAdrenaline = List.of(
                Component.text("• If your health drops below 30%, your speed and damage increase for a few seconds")
        );

        // Item - Obsidian Skin
        List<Component> loreObsidianSkin = List.of(
                Component.text("• There is a chance to completely block the damage from an attack"),
                Component.text("• There is a chance to reduce the effects of spells")
        );

        ingredients.put('O', createCustomHead(textureObsidian, Skills.OBSIDIAN_SKIN, loreObsidianSkin));
        ingredients.put('S', createCustomHead(textureHayTarget, Skills.PIERCING_ARROW, lorePiercingArrow));
        ingredients.put('M', createCustomHead(textureBlackstone, Skills.SEISMIC_IMPACT, loreSeismicImpact));
        ingredients.put('P', createCustomHead(textureHeart, Skills.ADRENALINE_IN_THE_BLOOD, loreAdrenaline));
        ingredients.put('F', createCustomHead(textureLuckyBlock, Skills.STREAK_OF_GOOD_LUCK, loreStreakOfGoodLuck));

        setupLayout(layout, ingredients);
    }

    @Override
    public void handleMenuClick(InventoryClickEvent event, PlayerRepository dataManager) {
        if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

        ItemMeta meta = event.getCurrentItem().getItemMeta();
        String skillID = meta.getPersistentDataContainer().get(skillKey, PersistentDataType.STRING);

        if (skillID != null) {
            Player player = (Player) event.getWhoClicked();
            Skills selectedSkill = Skills.fromId(skillID);

            if (selectedSkill == null) {
                player.sendMessage(Component.text("Error: Skill not found in domain", NamedTextColor.RED));
                return;
            }

            PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
            if (playerData == null) {
                player.sendMessage(Component.text("Error: Player data not loaded yet"));
                return;
            }

            playerData.setEquippedSkill(selectedSkill);
            selectedSkill.execute(player);
            player.closeInventory();
        }
    }
}