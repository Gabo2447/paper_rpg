# RPGPlugin - Minecraft Paper Plugin
A basic RPG plugin for Paper/Spigot version 26.2 that adds a system of active and passive skills with data persistence in YAML.

## Skills Included

#### 1. Seismic Impact (Area of Effect Combat)
When performing a critical hit (falling while jumping) or using a specific tool, the player generates a shockwave on the ground.

#### 2. Streak of Good Luck (Mining & Gathering)
Breaking an ore triggers a dynamic chance (scaling up with the skill level) to instantly mine adjacent blocks of the same type or duplicate the item drops.

#### 3. Adrenaline in the Blood (Survival Passive)
When the player's health drops below 30%, they enter a frenzy state, gaining increased movement speed and bonus damage for a few seconds.

#### 4. Piercing Arrow (Archery Specialist)
Fully charged bow shots have a chance to pass through the first target hit, maintaining their trajectory to damage monsters lined up behind them.

#### 5. Obsidian Skin (Tank Defensive Passive)
Grants a passive statistical chance to completely block an incoming attack's damage or slash negative status effects (like fire or poison) in half.

## Technologies

- Java
- Spigot/Paper API
- Adventure API
- SQlite

## Installation

1. Download the RPGPlugin.jar file attached below in the Assets section.
2. Drag it into the plugins/ folder on your Spigot/Paper (26.2)-compatible server.
3. Start or restart the server.
4. Use /skills in-game to select your first skill