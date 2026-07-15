package com.zabrek.rpgplugin.commands;

import com.zabrek.rpgplugin.Skills;
import com.zabrek.rpgplugin.database.DataManager;
import com.zabrek.rpgplugin.database.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class JumpCommand implements CommandExecutor {
    private final DataManager dataManager;

    public JumpCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players in the game"));
            return true;
        }

        PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
        Skills playerAbility = playerData.getEquippedSkill();

        if (playerAbility != null) {
            if (playerAbility != Skills.WIND_LEAP) {
                player.sendMessage(Component.text("You don't have the Wind Leap ability equipped", NamedTextColor.RED));
                return true;
            }

            if (player.isGliding()) {
                player.sendMessage(Component.text("You can't use Wind Leap while gliding", NamedTextColor.YELLOW));
                return true;
            }

            if (playerData.isOnCooldown(playerAbility.name())) {
                player.sendMessage(Component.text("Your ability is still on cooldown!", NamedTextColor.YELLOW));
                return true;
            } else {
                playerData.addCooldown(playerAbility.name(), 10);
            }
        }

        Vector currentVelocity = player.getLocation().getDirection();

        Vector impulseJump = currentVelocity.multiply(1.5);
        impulseJump.setY(0.8);

        Vector finalVelocity = currentVelocity.add(impulseJump);
        player.setVelocity(finalVelocity);
        return true;
    }
}
