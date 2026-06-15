# Deadlock's Story

Deadlock's Story is a very small Forge mod for the **Deadlock's End** modpack. It adds custom player statistics for progression systems that are important for questline and story flow.

Currently, the mod tracks:

* Gifts successfully given to **Minecraft Comes Alive** villagers
* MCA rooms created through the **Minecraft Comes Alive** blueprint system
* Bounties successfully completed at a **Bountiful** Bounty Board
* Spells successfully cast with **Iron's Spells 'n Spellbooks**

These stats can be viewed in the vanilla Statistics screen and used with Minecraft scoreboard objectives, FTB Quests, or other progression systems.

## Requirements

This mod is built for:

* Minecraft `1.20.1`
* Forge `47.x`
* Java `17`

Required mods:

* Minecraft Comes Alive Reborn `7.6.16+1.20.1`
* Bountiful `6.0.4+1.20.1`
* Iron's Spells 'n Spellbooks `1.20.1-3.15.6`

This mod uses mixins against MCA and Bountiful internals, so it is intended for the tested versions above. I haven't tested newer or older versions so they may require updated mixin targets.

Iron's Spells 'n Spellbooks is tracked through its Forge spell-cast event, so that integration is less fragile than the MCA and Bountiful mixins.

## Added Statistics

### MCA Gifts Given

Tracks successful gifts given to MCA villagers.

Stat ID:

```mcfunction
minecraft.custom:deadlocksstory.mca_gifts_given
```

Example scoreboard objective:

```mcfunction
/scoreboard objectives add mca_gifts minecraft.custom:deadlocksstory.mca_gifts_given
/scoreboard objectives setdisplay sidebar mca_gifts
```

### MCA Rooms Created

Tracks rooms successfully added through MCA's blueprint system.

This stat only increments when MCA's internal room/building count actually increases. If the blueprint menu incorrectly allows repeated clicks without adding another room, the stat should not increase.

When a room is removed through MCA, this stat is reduced by 1, but it will not go below 0.

Stat ID:

```mcfunction
minecraft.custom:deadlocksstory.mca_rooms_created
```

Example scoreboard objective:

```mcfunction
/scoreboard objectives add mca_rooms minecraft.custom:deadlocksstory.mca_rooms_created
/scoreboard objectives setdisplay sidebar mca_rooms
```

### Bounties Completed

Tracks successful bounty turn-ins at a Bountiful Bounty Board.

Stat ID:

```mcfunction
minecraft.custom:deadlocksstory.bounties_completed
```

Example scoreboard objective:

```mcfunction
/scoreboard objectives add bounties_completed minecraft.custom:deadlocksstory.bounties_completed
/scoreboard objectives setdisplay sidebar bounties_completed
```

### Spells Cast

Tracks successful spell casts from Iron's Spells 'n Spellbooks.

Stat ID:

```mcfunction
minecraft.custom:deadlocksstory.spells_cast
```

Example scoreboard objective:

```mcfunction
/scoreboard objectives add spells_cast minecraft.custom:deadlocksstory.spells_cast
/scoreboard objectives setdisplay sidebar spells_cast
```

## Configuration

A common config file is generated at:

```text
config/deadlocksstory-common.toml
```

Available options:

```toml
[deadlocksstory]
debug = false
```

Set `debug` to `true` to enable additional logging for stat tracking. This is really only useful while testing mixin hooks or verifying that a stat is being awarded correctly. You'll probably never actually need it.

## Development Setup

MCA, Bountiful, and Iron's Spells 'n Spellbooks are required on the local compile/runtime classpath while developing, but they are not bundled into the Deadlock's Story jar.

Expected local setup:

```text
DeadlocksStory/
  libs/
    mca.jar
    bountiful.jar
    irons_spellbooks.jar
  src/
  build.gradle
```

The dependency setup uses local jar files instead of the recommended way (Reason: Skill issues... probably):

```gradle
dependencies {
    minecraft "net.minecraftforge:forge:${minecraft_version}-${forge_version}"

    compileOnly files("libs/mca.jar")
    runtimeOnly files("libs/mca.jar")

    compileOnly files("libs/bountiful.jar")
    runtimeOnly files("libs/bountiful.jar")

    compileOnly files("libs/irons_spellbooks.jar")
    runtimeOnly files("libs/irons_spellbooks.jar")

    annotationProcessor "org.spongepowered:mixin:0.8.5:processor"
}
```

## Building

To build the mod:

```powershell
.\gradlew build
```

The compiled jar will be output to:

```text
build/libs/
```

## Notes

Deadlock's Story is designed specifically for the Deadlock's End modpack. You can probably make it work with your own pack, but I wouldn't recommend it. Feel free to try!
