package com.zabrek.rpgplugin.infraestructure.minecraft.skills.types;

import com.zabrek.rpgplugin.domain.Skills;
import com.zabrek.rpgplugin.domain.model.PlayerData;
import com.zabrek.rpgplugin.domain.model.SkillProperties;
import com.zabrek.rpgplugin.infraestructure.minecraft.skills.SkillBehavior;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class PiercingArrow implements SkillBehavior {
    @Override
    public Skills getSkillType() { return Skills.PIERCING_ARROW; }

    @Override
    public void onDamage(EntityDamageEvent event, Player player, PlayerData playerData) {}

    @Override
    public void onCommandExecute(Player player, PlayerData playerData, SkillProperties props) {
        player.sendMessage(Component.text("Piercing Arrow", NamedTextColor.BLUE));
    }
}
