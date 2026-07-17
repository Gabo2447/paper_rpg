package com.zabrek.rpgplugin.skills;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.controllers.VisualController;
import com.zabrek.rpgplugin.database.Mana;
import com.zabrek.rpgplugin.database.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public abstract class SkillActive implements SkillBehavior {
    private final long cooldownSeconds;
    protected int COST_MANA = 20;

    protected SkillActive(long cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    @Override
    public final void onCommandExecute(Player player, PlayerData playerData) {
        Skills skill = getSkillType();
        Mana playerMana = playerData.getMana();

        if (playerMana.getMana() < COST_MANA) {
            player.sendMessage(Component.text("You don't have enough mana!", NamedTextColor.RED));
            return;
        }

        if (playerData.isOnCooldown(skill.name())) {
            long actualTime = System.currentTimeMillis();
            int segRemain = (int) (playerData.getCooldowns().get(skill.name()) - actualTime) / 1000;
            player.sendMessage(Component.text("Your ability is still on cooldown! Remaining... " + segRemain + "s...", NamedTextColor.YELLOW));
            return;
        }

        if (executeActiveEffect(player, playerData)) {
            playerData.addCooldown(skill.name(), cooldownSeconds);

            playerMana.subMana(COST_MANA);
            VisualController.sendManaBar(player, playerMana);
        }
    }

    /**
     * Defines the physical or logical effect of the active ability
     * @return true if the ability was executed successfully and a cooldown should be applied; false otherwise.
     */
    protected abstract boolean executeActiveEffect(Player player, PlayerData playerData);
}
