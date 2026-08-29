# Better Inventory — Design

## Scope

Build a required client-and-server Fabric mod for Minecraft 26.2. It adds a fourth, nine-slot main-inventory row and lets players swap that row with the normal hotbar using a rebindable `H` key. Stack limits and container sizes remain unchanged.

## Module architecture

The Gradle project will use Architectury Loom with three source modules:

- `common`: shared inventory model, slot constants, server-authoritative swap service, network payload definitions, and platform-neutral interfaces.
- `fabric`: Fabric entrypoints, mixins, packet registration, and Fabric platform bindings.
- Future loader modules: Forge and older-Minecraft modules can reuse the `common` logic while supplying their own bindings.

The mod will require installation on both client and server. The server is authoritative for inventory mutation and synchronizes the result through Minecraft's normal inventory synchronization.

## Inventory and networking

Mixin-backed player-inventory storage will add nine persistent slots after the vanilla main-inventory storage. These slots will be included in NBT serialization, death drops, screen-handler slot construction, quick-move handling, and the vanilla-compatible inventory operations that touch main inventory.

The client keybind defaults to `H` and is rebindable in Controls. It sends a single swap request to the server. The server validates the player state, atomically swaps each hotbar slot with its matching extra-row slot, and broadcasts the resulting authoritative inventory state. Item components, durability, and custom data move with each `ItemStack` unchanged.

## Screen integration

Survival and creative inventory views will be extended by one 18-pixel row. Vanilla slot rendering and background treatment are reused; the hotbar moves down by one row so the four-row grid is visually continuous. No custom GUI art is required for this delivery. Future supplied assets may replace or augment this presentation without changing gameplay code.

## Failure handling and compatibility

The swap packet is accepted only on the logical server and is ignored if the player is unavailable. The mod declares a client-and-server environment requirement. Inventory-altering third-party mods are unsupported unless explicitly validated, because they may assume vanilla inventory sizes.

## Testing and acceptance checks

- Unit tests for slot mapping and all-row hotbar swaps, including preservation of empty stacks and item data.
- Persistence-oriented tests for the extra-row serialization contract where mappings permit it.
- Client and dedicated-server development launches to check mixin application and network registration.
- A production Gradle build, plus manual in-game checks of survival and creative screen placement, rebinding, swapping, save/reload, death drops, and respawn.

## Authorship and delivery

The mod metadata and repository commits identify `R2bEEaton` as author. The delivered JAR is the Fabric 26.2 client-and-server build; CurseForge publication stays with the client.
