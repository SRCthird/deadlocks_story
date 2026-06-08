# Deadlock's Story

Deadlock's Story is a very small Forge mod for the **Deadlock's End** modpack. It adds custom player statistics for progression systems that are important for questline and story flow.

Currently, the mod tracks:

* Gifts successfully given to **Minecraft Comes Alive** villagers
* Bounties successfully completed at a **Bountiful** Bounty Board

These stats can be viewed in the vanilla Statistics screen and used with Minecraft scoreboard objectives, FTB Quests, or other progression systems.

## Requirements

This mod is built for:

* Minecraft `1.20.1`
* Forge `47.x`
* Java `17`

Required mods:

* Minecraft Comes Alive Reborn `7.6.16+1.20.1`
* Bountiful `6.0.4+1.20.1`

This mod uses mixins against MCA and Bountiful internals, so it is intended for the tested versions above. Newer or older versions may require updated mixin targets.

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

MCA and Bountiful are required on the local compile/runtime classpath while developing, but they are not be bundled into the Deadlocks Story jar.

Expected local setup:

```text
DeadlocksStory/
  libs/
    mca.jar
    bountiful.jar
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

Deadlock's Story is designed specifically for the Deadlock's End modpack. You can probably make it work with your own pack, but I wouldn't recommended it. Feel free to try!
