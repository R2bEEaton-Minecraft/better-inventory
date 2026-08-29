# Container Player-Inventory Row Design

## Goal

Show Better Inventory's existing fourth player-inventory row in every vanilla container screen that displays the player inventory. The row is the same persistent player state used by the survival inventory and alternate-hotbar swap. It appears directly above the normal hotbar, which moves down by 18 pixels.

This expands the player inventory UI only. It does not change the storage capacity of chests, barrels, shulkers, furnaces, or any other container.

## Scope

The implementation covers vanilla menus and screens that render player inventory slots: generic container menus (including chest, barrel, shulker box, hopper, dispenser, and dropper), workstations, merchant, beacon, horse inventories, and their associated screens. Creative handling remains separate because its inventory presentation uses a custom picker menu.

Matching vanilla container texture PNGs are copied into the mod's `assets/minecraft/textures/gui/container` namespace, preserving their original paths so users can edit the local copies. Each copied texture has 18 pixels added below the player inventory section with placeholder/extended vanilla pixels. The base inventory and current creative texture overrides remain editable local assets.

## Menu behavior

`ExtraRowContainer` remains the sole backing container for the added player row. A shared menu mixin adds its nine slots after each vanilla player inventory section and shifts vanilla hotbar-slot coordinates down by 18 pixels. Menu-specific transfer ranges are extended to include the extra row so shift-click moves items through it consistently.

Ground-item pickup uses the fourth row only after vanilla's normal 36 non-equipment player slots have no valid destination. Existing hotbar and main-inventory priority therefore remains unchanged.

The row remains server-authoritative, persists in the existing custom save data, drops on death, and transfers under `keepInventory` exactly as verified in the current implementation.

## Screen behavior

Every affected `AbstractContainerScreen` receives an 18-pixel extension to its effective bottom boundary for outside-click handling. Screen-specific render hooks draw the copied texture extension, and player hotbar slots are positioned 18 pixels lower. The added row stays attached to the existing player grid rather than appearing as a separate panel.

## Compatibility and exclusions

Only vanilla menu/screen classes are targeted. Mods that replace or substantially restructure player-inventory menus are outside this change's compatibility guarantee. Container storage size, stack limits, armor/offhand mappings, and the Creative catalog capacity remain unchanged.

## Verification

Unit tests cover fourth-row overflow pickup selection, common menu layout offsets, and representative transfer-range calculations. Integration checks launch the Fabric client and exercise a container menu, a workstation menu, and an overflow pickup scenario. The full `:common:test :fabric:build` command must pass before delivery.
