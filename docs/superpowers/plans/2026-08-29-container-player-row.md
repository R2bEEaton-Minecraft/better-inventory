# Container Player-Inventory Row Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add Better Inventory's persistent fourth player row to every vanilla container UI, use it as ground-item overflow, and supply editable extended container textures.

**Architecture:** `ExtraRowContainer` remains the backing state. A common menu mixin appends its slots immediately before each vanilla hotbar and moves that hotbar down 18 pixels. Core layout services define slot and transfer ranges; client mixins extend screen hit bounds and render the texture extension without changing immutable screen dimensions.

**Tech Stack:** Java 25, Fabric Loader 0.19.3, Architectury Loom, Sponge Mixin/MixinExtras, JUnit 5, AssertJ, Minecraft 26.2.

**Spec:** `docs/superpowers/specs/2026-08-29-container-player-row-design.md`

## Global Constraints

- Keep vanilla container storage sizes and vanilla stack limits unchanged.
- Add one 9-slot player-inventory row above the normal hotbar in all vanilla player-inventory menus except Creative's separate picker handling.
- Make the row a ground-pickup overflow only after normal 36 non-equipment player slots.
- Move the vanilla hotbar and bottom click bounds down exactly 18 pixels.
- Copy matching vanilla GUI PNGs into `fabric/src/client/resources/assets/minecraft/textures/gui/container/` as editable local overrides.
- Preserve save, death-drop, and `keepInventory` behavior already implemented for `ExpandedInventoryAccess`.

---

### Task 1: Common player-row layout and pickup-overflow policy

**Files:**
- Create: `common/src/main/java/cc/spea/betterinventory/core/PlayerRowLayout.java`
- Create: `common/src/main/java/cc/spea/betterinventory/core/PickupOverflowService.java`
- Create: `common/src/test/java/cc/spea/betterinventory/core/PlayerRowLayoutTest.java`
- Create: `common/src/test/java/cc/spea/betterinventory/core/PickupOverflowServiceTest.java`

**Interfaces:**
- Produces: `PlayerRowLayout.extraRowY(int hotbarY): int`, returning `hotbarY - 4`.
- Produces: `PlayerRowLayout.movedHotbarY(int hotbarY): int`, returning `hotbarY + 18`.
- Produces: `PickupOverflowService.findDestination(List<T> mainSlots, List<T> extraSlots, T incoming, Predicate<T> isEmpty, BiPredicate<T,T> canMerge): SlotDestination`.
- `SlotDestination` is `record SlotDestination(boolean extraRow, int index)`; `index == -1` means no destination.

- [ ] **Step 1: Write the failing layout tests**

```java
@Test
void places_extra_row_in_the_four_pixel_gap_before_the_hotbar() {
    assertThat(PlayerRowLayout.extraRowY(142)).isEqualTo(138);
    assertThat(PlayerRowLayout.movedHotbarY(142)).isEqualTo(160);
}
```

- [ ] **Step 2: Run the layout test to verify it fails**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test --tests cc.spea.betterinventory.core.PlayerRowLayoutTest`

Expected: compilation failure because `PlayerRowLayout` does not exist.

- [ ] **Step 3: Implement the minimal layout service**

```java
public final class PlayerRowLayout {
    public static final int ROW_HEIGHT = 18;
    public static final int HOTBAR_GAP = 4;
    public static int extraRowY(int hotbarY) { return hotbarY - HOTBAR_GAP; }
    public static int movedHotbarY(int hotbarY) { return hotbarY + ROW_HEIGHT; }
}
```

- [ ] **Step 4: Write the failing pickup-priority tests**

```java
@Test
void chooses_a_normal_inventory_slot_before_the_extra_row() {
    assertThat(PickupOverflowService.findDestination(main, extra, dirt, Stack::empty, Stack::canMerge))
        .isEqualTo(new SlotDestination(false, 35));
}

@Test
void chooses_the_extra_row_when_all_normal_slots_are_full() {
    assertThat(PickupOverflowService.findDestination(fullMain, extraWithEmptyAtFour, dirt, Stack::empty, Stack::canMerge))
        .isEqualTo(new SlotDestination(true, 4));
}
```

- [ ] **Step 5: Run the pickup test to verify it fails**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test --tests cc.spea.betterinventory.core.PickupOverflowServiceTest`

Expected: compilation failure because `PickupOverflowService` and `SlotDestination` do not exist.

- [ ] **Step 6: Implement pickup destination selection**

Implement normal-slot merge/empty checks first, then the same checks over `extraSlots`; return `new SlotDestination(false, -1)` only when neither list can accept the stack.

- [ ] **Step 7: Run all common tests and commit**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test`

Expected: PASS.

```powershell
git add common/src/main common/src/test
git commit -m "feat: define container player-row layout"
```

### Task 2: Server-authoritative row slots and overflow pickup

**Files:**
- Modify: `fabric/src/main/java/cc/spea/betterinventory/mixin/AbstractContainerMenuMixin.java`
- Modify: `fabric/src/main/java/cc/spea/betterinventory/mixin/InventoryMenuMixin.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/InventoryPickupMixin.java`
- Modify: `fabric/src/main/resources/better-inventory.mixins.json`

**Interfaces:**
- Consumes: `PlayerRowLayout.extraRowY`, `PlayerRowLayout.movedHotbarY`, and `ExpandedInventoryAccess.betterinventory$getExtraRow()`.
- Produces: every call to `AbstractContainerMenu.addInventoryHotbarSlots(Inventory,int,int)` has nine `ExtraRowContainer` slots at `extraRowY(top)`, followed by vanilla hotbar slots at `movedHotbarY(top)`.

- [ ] **Step 1: Write a failing menu-layout test using a 45-slot model**

Add a core test that asserts container menu player slots have 27 normal-grid slots, 9 extra-row slots, and 9 hotbar slots in that order; the extra row uses y `hotbarY - 4` and hotbar uses `hotbarY + 18`.

- [ ] **Step 2: Run the new test to verify it fails**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test --tests cc.spea.betterinventory.core.PlayerRowLayoutTest`

Expected: FAIL because the menu-order assertion has no implementation model.

- [ ] **Step 3: Replace the InventoryMenu-only slot injection with a shared hotbar hook**

Inject at the tail of `AbstractContainerMenu.addInventoryHotbarSlots(Container inventory, int left, int top)`. When `inventory instanceof Inventory playerInventory`, create `new ExtraRowContainer(playerInventory)` and add nine slots at `left + column * 18, PlayerRowLayout.extraRowY(top)`. Retain the existing `ModifyArg`, but make it call `PlayerRowLayout.movedHotbarY(y)` for every player `Inventory` hotbar, not only `InventoryMenu`.

Remove the duplicate `InventoryMenuMixin` slot appender and its JSON entry after the shared hook owns this behavior.

- [ ] **Step 4: Add the pickup overflow injection**

Inject at the return of `Inventory.add(int, ItemStack)`. If vanilla leaves a non-empty stack and returns `false`, use `PickupOverflowService` against `betterinventory$getExtraRow()`; merge into a compatible extra stack first, otherwise fill an empty extra slot, update the incoming stack count, call `setChanged()`, and set the callback return value to whether any items were accepted. Do not touch normal-inventory priority.

- [ ] **Step 5: Run focused and full verification**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test :fabric:build`

Expected: PASS.

- [ ] **Step 6: Commit**

```powershell
git add common fabric/src/main
git commit -m "feat: add player row to container menus"
```

### Task 3: Menu-specific transfer ranges

**Files:**
- Create: `common/src/main/java/cc/spea/betterinventory/core/MenuTransferRanges.java`
- Create: `common/src/test/java/cc/spea/betterinventory/core/MenuTransferRangesTest.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/ContainerMenuTransferMixin.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/InventoryMenuTransferMixin.java`
- Modify: `fabric/src/main/resources/better-inventory.mixins.json`

**Interfaces:**
- Produces: `MenuTransferRanges.playerStart(int vanillaPlayerStart): int` and `MenuTransferRanges.playerEndExclusive(int vanillaPlayerEndExclusive): int`, where the end is `vanillaPlayerEndExclusive + 9` after the shared row is appended.
- Container quick-move routes include the extra row whenever they route to player inventory; routes from player inventory treat the added slots as player-origin slots.

- [ ] **Step 1: Write failing transfer-range tests**

```java
@Test
void expands_the_vanilla_player_range_by_nine_slots() {
    assertThat(MenuTransferRanges.playerEndExclusive(63)).isEqualTo(72);
}

@Test
void classifies_the_added_slots_as_player_inventory() {
    assertThat(MenuTransferRanges.isPlayerSlot(54, 27, 63)).isTrue();
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test --tests cc.spea.betterinventory.core.MenuTransferRangesTest`

Expected: compilation failure because `MenuTransferRanges` does not exist.

- [ ] **Step 3: Implement the range helpers and mixin redirects**

For each vanilla menu family whose `quickMoveStack` uses an end-exclusive vanilla player range, modify only that range constant through `@ModifyConstant` or `@ModifyArg` to add nine slots. Cover generic `ChestMenu`, `HopperMenu`, `DispenserMenu`, `FurnaceMenu`, `CraftingMenu`, `EnchantmentMenu`, `ItemCombinerMenu`, `LoomMenu`, `StonecutterMenu`, `CartographyTableMenu`, `MerchantMenu`, `BeaconMenu`, and `HorseInventoryMenu`. Keep container-origin ranges unchanged.

- [ ] **Step 4: Run tests and build**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test :fabric:build`

Expected: PASS.

- [ ] **Step 5: Commit**

```powershell
git add common fabric/src/main
git commit -m "feat: include player row in quick move ranges"
```

### Task 4: Client bounds and editable GUI assets

**Files:**
- Create: `fabric/src/client/java/cc/spea/betterinventory/client/mixin/ContainerScreenMixin.java`
- Modify: `fabric/src/client/resources/better-inventory.client.mixins.json`
- Create: `fabric/src/client/resources/assets/minecraft/textures/gui/container/{generic_54,dispenser,hopper,shulker_box,furnace,blast_furnace,smoker,brewing_stand,crafting_table,enchanting_table,anvil,smithing,grindstone,loom,stonecutter,cartography_table,villager,beacon,horse,crafter,nautilus}.png`
- Modify: `fabric/src/client/resources/assets/minecraft/textures/gui/container/README.md`

**Interfaces:**
- Consumes: `PlayerRowLayout.ROW_HEIGHT`.
- Produces: an 18-pixel lower background extension and outside-click bounds for each non-Creative `AbstractContainerScreen` that has a player-inventory section.

- [ ] **Step 1: Write the failing screen-bound model test**

Add `ContainerScreenLayoutTest` asserting `extendedHeight(166) == 184` and that points at y `183` are inside while y `184` is outside.

- [ ] **Step 2: Run it to verify it fails**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test --tests cc.spea.betterinventory.core.ContainerScreenLayoutTest`

Expected: compilation failure because `ContainerScreenLayout` does not exist.

- [ ] **Step 3: Implement the common layout model and client bound mixin**

Create `ContainerScreenLayout.extendedHeight(int height)` returning `height + 18`. Use `@ModifyExpressionValue` at `AbstractContainerScreen.hasClickedOutside` to substitute this height for non-`InventoryScreen` and non-`CreativeModeInventoryScreen` instances. Preserve the existing survival and Creative-specific behavior.

- [ ] **Step 4: Copy vanilla textures and add the editable extension**

Resolve each source texture through the Minecraft 26.2 asset index, copy it to the exact path above, increase its canvas height by 18 pixels, and extend the player-inventory lower background using neighboring vanilla pixels. Do not generate replacement art. Record original source path, original dimensions, and edited dimensions in the README.

- [ ] **Step 5: Render the lower extension**

Inject into each applicable screen's `extractBackground` tail through a grouped client mixin or class-specific mixins. Blit the final 18-pixel strip immediately below the original background using that screen's own texture identifier and dimensions. Do not assign to `imageHeight` or `imageWidth`.

- [ ] **Step 6: Run assets/build verification and commit**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test :fabric:build`

Expected: PASS.

```powershell
git add common fabric/src/client
git commit -m "feat: extend container player inventory UI"
```

### Task 5: Integrated gameplay verification and handoff

**Files:**
- Modify: `README.md`
- Modify: `fabric/src/client/resources/assets/minecraft/textures/gui/container/README.md`

**Interfaces:**
- Documents installation on both client and server and the local editable texture override directory.

- [ ] **Step 1: Launch the client**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :fabric:runClient`

Expected: client reaches an integrated world without a mixin error.

- [ ] **Step 2: Exercise representative menus**

In a survival world, open a chest, furnace, crafting table, enchanting table, and horse inventory. Confirm the fourth row appears above the hotbar, clicking outside the 18-pixel extension does not toss items, and shift-clicking moves items through the new row.

- [ ] **Step 3: Exercise overflow and persistence**

Fill vanilla normal player storage, collect a ground item, and confirm it enters the fourth row. Verify fourth-row items drop with `keepInventory=false` and persist with `keepInventory=true`.

- [ ] **Step 4: Run final automated verification**

Run: `$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot'; .\gradlew.bat :common:test :fabric:build`

Expected: PASS.

- [ ] **Step 5: Commit and publish**

```powershell
git add README.md fabric/src/client/resources/assets/minecraft/textures/gui/container/README.md
git commit -m "docs: document extended container inventory"
git push origin main
```
