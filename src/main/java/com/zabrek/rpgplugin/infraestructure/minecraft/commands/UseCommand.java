package com.zabrek.rpgplugin.infraestructure.minecraft.commands;

import com.zabrek.rpgplugin.application.ports.out.PlayerRepository;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillBehavior;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillRegistry;
import com.zabrek.rpgplugin.domain.Skills;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UseCommand implements CommandExecutor {
    private final PlayerRepository dataManager;
    private final SkillRegistry skillRegistry;

    public UseCommand(PlayerRepository dataManager, SkillRegistry skillRegistry) {
        this.dataManager = dataManager;
        this.skillRegistry = skillRegistry;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players in the game"));
            return true;
        }

        PlayerData playerData = dataManager.getPlayerData(player.getUniqueId());
        if (playerData == null) return true;

        Skills playerSkill = playerData.getEquippedSkill();
        if (playerSkill == null) {
            sender.sendMessage(Component.text("Select a skill using the “/skills” command to unlock the “/use” command", NamedTextColor.GRAY));
            return true;
        }

        SkillBehavior behavior = skillRegistry.getBehavior(playerSkill);
        if (behavior == null) return true;

        behavior.onCommandExecute(player, playerData);
        return true;
    }
}
