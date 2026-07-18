package com.zabrek.rpgplugin.infraestructure.minecraft.skills;

import com.zabrek.rpgplugin.domain.model.SkillProperties;
import com.zabrek.rpgplugin.infraestructure.minecraft.controllers.VisualController;
import com.zabrek.rpgplugin.domain.model.Mana;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.Skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public abstract class SkillActive implements SkillBehavior {

    @Override
    public final void onCommandExecute(Player player, PlayerData playerData, SkillProperties props) {
        Skills skill = getSkillType();
        int costMana = props.getManaCost();
        long cooldownSeconds = props.getCooldownTime();

        Mana playerMana = playerData.getMana();

        if (playerMana.getMana() < costMana) {
            player.sendMessage(Component.text("You don't have enough mana!", NamedTextColor.RED));
            return;
        }

        if (playerData.isOnCooldown(skill.name())) {
            long actualTime = System.currentTimeMillis();
            int segRemain = (int) (playerData.getCooldowns().get(skill.name()) - actualTime) / 1000;
            player.sendMessage(Component.text("Your ability is still on cooldown! Remaining... " + segRemain + "s...", NamedTextColor.YELLOW));
            return;
        }

        if (executeActiveEffect(player, playerData, props)) {
            playerData.addCooldown(skill.name(), cooldownSeconds);
            playerMana.subMana(costMana);
            VisualController.sendManaBar(player, playerMana);
        }
    }

    /**
     * Defines the physical or logical effect of the active ability
     * @return true if the ability was executed successfully and a cooldown should be applied; false otherwise.
     */
    protected abstract boolean executeActiveEffect(Player player, PlayerData playerData, SkillProperties props);
}
