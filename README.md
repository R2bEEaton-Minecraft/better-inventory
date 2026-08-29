# Better Inventory

Better Inventory adds a fourth, nine-slot main-inventory row to Minecraft 26.2. That row doubles as an alternate hotbar: press the configurable `H` key to swap it with the active hotbar.

## Requirements

- Minecraft Java Edition 26.2
- Fabric Loader 0.19.3 or newer
- Fabric API 0.158.0+26.2
- Java 25

Install the generated JAR on both the client and the server. This is required because player inventory size and its synchronization are server-authoritative.

## Development

Use JDK 25 and run `gradlew.bat :fabric:build`. The distributable Fabric JAR is written to `fabric/build/libs`.

## Compatibility

Better Inventory does not change stack limits or chest/container sizes. Mods that replace or assume vanilla player-inventory slot counts may require their own compatibility support.

## License

The project template is CC0-1.0. Better Inventory source additions are authored by R2bEEaton.
