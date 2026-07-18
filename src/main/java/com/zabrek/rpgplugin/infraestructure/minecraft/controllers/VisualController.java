package com.zabrek.rpgplugin.infraestructure.minecraft.controllers;

import com.zabrek.rpgplugin.domain.model.Mana;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class VisualController {
    public static void sendManaBar(Player player, Mana mana) {
        Component barra = Component.text("Mana: ", NamedTextColor.AQUA)
                .append(Component.text(mana.getMana() + "/" + mana.getMaxMana(), NamedTextColor.DARK_AQUA));

        player.sendActionBar(barra);
    }
}
