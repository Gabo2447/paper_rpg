package com.zabrek.rpgplugin.commands;

import com.zabrek.rpgplugin.commands.inventory.SkillsMenuHolder;
import net.kyori.adventure.text.Component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SkillsCommand implements CommandExecutor {
    private final Plugin plugin;

    public SkillsCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("This command can only be used by players in the game"));
            return true;
        }

        player.openInventory(new SkillsMenuHolder(plugin).getInventory());
        return true;
    }
}
