# Better Inventory Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Deliver a Fabric 26.2 mod that adds a persistent fourth player-inventory row and swaps it with the hotbar through a server-authoritative rebindable key.

**Architecture:** A multi-module Architectury Loom project keeps pure slot/swap rules and payload definitions in `common`; `fabric` supplies Fabric entrypoints and mixins. Player inventory mixins own the nine extra stacks and expose them to vanilla screen handlers, serialization, death drop handling, and network synchronization. The client sends an Architectury payload; the server performs the whole-row swap.

**Tech Stack:** Java 25, Gradle 9.5.1, Architectury Loom/Plugin/API 26.2, Fabric Loader 0.19.3, Fabric API 0.158.0+26.2, Mixin, JUnit 5.

**Spec:** `docs/superpowers/specs/2026-08-29-better-inventory-design.md`

## Global Constraints

- Target Minecraft Java Edition 26.2 and Fabric; require the mod on both client and server.
- Identify the mod and every commit author as `R2bEEaton`.
- Keep one 9-slot hotbar and add exactly one 9-slot main-inventory row; do not change stack limits or containers.
- The new row must be attached to the normal inventory grid in survival and creative views.
- The swap key defaults to `H`, remains rebindable, and server-side swapping preserves complete `ItemStack` data.
- Use Architectury common/fabric separation so future platforms reuse core behavior.
- Use JDK 25 explicitly for Gradle: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot`.

---

## File structure

| Path | Responsibility |
| --- | --- |
| `settings.gradle`, root `build.gradle`, `gradle.properties` | Architectury multi-project configuration and version coordinates. |
| `common/src/main/java/cc/spea/betterinventory/core/InventorySlots.java` | Canonical vanilla and extra-row index constants. |
| `common/src/main/java/cc/spea/betterinventory/core/HotbarSwapService.java` | Pure, atomic 9-pair exchange logic. |
| `common/src/main/java/cc/spea/betterinventory/network/SwapHotbarPayload.java` | Cross-platform request payload and server dispatch contract. |
| `common/src/test/java/...` | JUnit tests for slot mapping and the swap service. |
| `fabric/src/main/java/.../mixin/*` | Fabric 26.2 mapped hooks for storage, NBT, drop, menu slots, and screen rendering. |
| `fabric/src/main/java/.../BetterInventoryFabric.java` | Common initialization and Fabric server payload handler. |
| `fabric/src/client/java/.../BetterInventoryFabricClient.java` | Keybind and client payload sender. |
| `fabric/src/*/resources` | Mod metadata, mixin declarations, and assets. |

### Task 1: Convert the template to an Architectury Fabric project

**Files:**
- Modify: `settings.gradle`, `build.gradle`, `gradle.properties`, `README.md`
- Create: `common/build.gradle`, `fabric/build.gradle`, `common/src/main/resources/.gitkeep`
- Move: current `src/main/**` and `src/client/**` into the corresponding `fabric/src/**` roots
- Modify: `fabric/src/main/resources/fabric.mod.json`

**Interfaces:**
- Produces Gradle projects `:common` and `:fabric`; `:fabric:build` writes the distributable remapped JAR.

- [ ] **Step 1: Add the multi-module Gradle configuration**

Configure `settings.gradle` to include `common` and `fabric`; apply Architectury Plugin/Loom from `https://maven.architectury.dev/`; use `architectury { platformSetupLoomIde(); fabric() }` in `common` and `architectury { platformSetupLoomIde(); fabric() }` plus Fabric Loom in `fabric`. Keep Minecraft/Fabric versions centralized in `gradle.properties` and add the Architectury API 26.2 coordinate.

- [ ] **Step 2: Compile the untouched entrypoints against the new layout**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:compileJava --no-daemon`

Expected: initial failure until source roots and dependency wiring are correct; then successful compilation.

- [ ] **Step 3: Move entrypoints/resources and add Architectury dependencies**

Place common code and assets in `common`; place `fabric.mod.json`, Fabric entrypoints, and Fabric mixins in `fabric`. Make `fabric` depend on `common` via `namedElements`/transformed common dependency and include Architectury API as a mod dependency. Add JUnit Jupiter and AssertJ test dependencies, configure `useJUnitPlatform()`, and set the metadata source URL to `https://github.com/R2bEEaton-Minecraft/better-inventory`.

- [ ] **Step 4: Verify the new base build**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:build --no-daemon`

Expected: `BUILD SUCCESSFUL`, with a remapped Fabric JAR under `fabric/build/libs`.

- [ ] **Step 5: Set product metadata**

Replace template wording with Better Inventory’s description, author `R2bEEaton`, client/server environment, source URL `https://github.com/R2bEEaton-Minecraft/better-inventory`, and a README covering both-side installation.

- [ ] **Step 6: Commit the platform scaffold**

Run: `git add settings.gradle build.gradle gradle.properties common fabric README.md; git commit -m "build: establish Architectury Fabric project"`

### Task 2: Create and test the shared row/swap model

**Files:**
- Create: `common/src/main/java/cc/spea/betterinventory/core/InventorySlots.java`
- Create: `common/src/main/java/cc/spea/betterinventory/core/HotbarSwapService.java`
- Create: `common/src/test/java/cc/spea/betterinventory/core/HotbarSwapServiceTest.java`
- Modify: `common/build.gradle`

**Interfaces:**
- Produces `InventorySlots.HOTBAR_START`, `InventorySlots.EXTRA_ROW_START`, `InventorySlots.EXTRA_ROW_SIZE`.
- Produces `HotbarSwapService.swap(List<ItemStack> inventory)` which exchanges indices `0..8` and `36..44` without copying or changing stacks.

- [ ] **Step 1: Write the failing hotbar-swap tests**

In the test class, define `testInventoryWithNamedStacks()` as a local helper that creates an `ArrayList` of 45 `ItemStack.EMPTY` entries, places `CustomName`-labelled stacks `hotbar-0` through `hotbar-8` at indices 0–8 and `extra-0` through `extra-8` at indices 36–44, and returns the list. Define `testInventoryWithEmptyHotbarSlotAndDamagedExtraStack()` identically except index 4 stays empty and index 36 contains a damageable stack with damage 17.

```java
@Test
void swaps_each_hotbar_slot_with_its_matching_extra_row_slot() {
    List<ItemStack> stacks = testInventoryWithNamedStacks();
    HotbarSwapService.swap(stacks);
    assertThat(stacks.get(0).getCustomName().getString()).isEqualTo("extra-0");
    assertThat(stacks.get(InventorySlots.EXTRA_ROW_START).getCustomName().getString()).isEqualTo("hotbar-0");
}

@Test
void preserves_empty_stacks_and_item_component_data() {
    List<ItemStack> stacks = testInventoryWithEmptyHotbarSlotAndDamagedExtraStack();
    HotbarSwapService.swap(stacks);
    assertThat(stacks.get(0).isEmpty()).isFalse();
    assertThat(stacks.get(0).getDamage()).isEqualTo(17);
    assertThat(stacks.get(4).isEmpty()).isTrue();
}
```

- [ ] **Step 2: Run the test to verify it fails**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test --tests "cc.spea.betterinventory.core.HotbarSwapServiceTest" --no-daemon`

Expected: FAIL because `InventorySlots` and `HotbarSwapService` do not exist.

- [ ] **Step 3: Implement only the shared constants and exchange loop**

```java
public static void swap(List<ItemStack> stacks) {
    for (int offset = 0; offset < InventorySlots.EXTRA_ROW_SIZE; offset++) {
        int hotbar = InventorySlots.HOTBAR_START + offset;
        int extra = InventorySlots.EXTRA_ROW_START + offset;
        ItemStack held = stacks.get(hotbar);
        stacks.set(hotbar, stacks.get(extra));
        stacks.set(extra, held);
    }
}
```

- [ ] **Step 4: Run the focused test and full common test suite**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test --no-daemon`

Expected: PASS with zero failed tests.

- [ ] **Step 5: Commit the verified shared model**

Run: `git add common; git commit -m "feat: add shared hotbar swap model"`

### Task 3: Add persistent expanded player inventory storage

**Files:**
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/PlayerInventoryMixin.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/access/ExpandedPlayerInventory.java`
- Modify: `fabric/src/main/resources/better-inventory.mixins.json`
- Create: `fabric/src/test/java/cc/spea/betterinventory/core/ExpandedInventoryPersistenceTest.java`

**Interfaces:**
- Produces `ExpandedPlayerInventory.betterinventory$getExtraStacks(): DefaultedList<ItemStack>`.
- Produces an extra row of nine `ItemStack`s integrated into total inventory size, indexed after vanilla inventory slots.

- [ ] **Step 1: Write failing storage-contract tests**

```java
@Test
void expanded_inventory_has_nine_extra_main_inventory_slots() {
    PlayerInventory inventory = testPlayerInventory();
    assertThat(((ExpandedPlayerInventory) inventory).betterinventory$getExtraStacks()).hasSize(9);
}

@Test
void extra_row_survives_inventory_nbt_round_trip() {
    PlayerInventory original = testPlayerInventory();
    setExtraRowStack(original, 0, namedStack("persisted"));
    NbtList saved = original.writeNbt(new NbtList());
    PlayerInventory restored = testPlayerInventory();
    restored.readNbt(saved);
    assertThat(getExtraRowStack(restored, 0).getCustomName().getString()).isEqualTo("persisted");
}
```

- [ ] **Step 2: Run tests to verify the missing mixin contract**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:test --tests "*ExpandedInventoryPersistenceTest" --no-daemon`

Expected: FAIL because the cast/interface and extra-row NBT encoding do not exist.

- [ ] **Step 3: Implement player-inventory mixins against generated 26.2 sources**

Add exactly nine `DefaultedList` stacks. Extend the mapped `size`, `isEmpty`, `getStack`, `removeStack`, `setStack`, `clear`, NBT write/read, and drop iteration hooks so each extra slot has vanilla semantics. Encode these slots with their real post-vanilla indices, and accept only the extra-row range during read. The test fixture methods `testPlayerInventory()`, `setExtraRowStack`, and `getExtraRowStack` are package-private test helpers backed by a fake player/world created with Fabric’s game-test bootstrap; they do not belong in production sources.

- [ ] **Step 4: Run persistence and shared-model tests**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test :fabric:test --no-daemon`

Expected: PASS with nine extra slots preserved by NBT.

- [ ] **Step 5: Commit expanded storage**

Run: `git add fabric; git commit -m "feat: persist expanded player inventory row"`

### Task 4: Expose the row to menus, transfers, and deaths

**Files:**
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/PlayerScreenHandlerMixin.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/mixin/ServerPlayerEntityMixin.java`
- Modify: `fabric/src/main/resources/better-inventory.mixins.json`
- Create: `fabric/src/test/java/cc/spea/betterinventory/core/ExpandedSlotMappingTest.java`

**Interfaces:**
- Consumes `ExpandedPlayerInventory` and `InventorySlots.EXTRA_ROW_START`.
- Produces nine ordinary `Slot` instances for the player’s extra row, directly above the hotbar.

- [ ] **Step 1: Write failing slot-layout and transfer tests**

```java
@Test
void player_screen_handler_contains_a_contiguous_nine_slot_extra_row() {
    PlayerScreenHandler handler = testHandlerWithExpandedInventory();
    assertThat(findSlotsBackedByExtraRow(handler)).hasSize(9);
    assertThat(slotXCoordinates(findSlotsBackedByExtraRow(handler))).containsExactly(8, 26, 44, 62, 80, 98, 116, 134, 152);
}

@Test
void quick_move_can_fill_an_empty_extra_row_slot() {
    assertThat(quickMoveIntoExtraRow(testHandlerWithExpandedInventory())).isTrue();
}
```

- [ ] **Step 2: Run the slot tests to verify they fail**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:test --tests "*ExpandedSlotMappingTest" --no-daemon`

Expected: FAIL because the screen handler has no extra slots.

- [ ] **Step 3: Add the nine attached menu slots and extend transfer/drop paths**

Insert nine player-inventory `Slot` objects at the same x coordinates as vanilla rows and y coordinate immediately above the hotbar. Extend mapped quick-move ranges to include these slots; ensure the server’s death-drop iteration includes the extra list once and clears it once. Define the test helpers `testHandlerWithExpandedInventory()`, `findSlotsBackedByExtraRow`, `slotXCoordinates`, and `quickMoveIntoExtraRow` in `ExpandedSlotMappingTest`; each creates an isolated game-test player/handler, filters handler slots by inventory index 36–44, and invokes the handler’s mapped quick-move method.

- [ ] **Step 4: Run full tests**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test :fabric:test --no-daemon`

Expected: PASS with extra-row slots usable by the normal handler.

- [ ] **Step 5: Commit menu integration**

Run: `git add fabric; git commit -m "feat: integrate extra row with player menus"`

### Task 5: Implement server-authoritative swapping and client keybind

**Files:**
- Create: `common/src/main/java/cc/spea/betterinventory/network/SwapHotbarPayload.java`
- Create: `common/src/main/java/cc/spea/betterinventory/network/BetterInventoryNetworking.java`
- Create: `fabric/src/main/java/cc/spea/betterinventory/BetterInventoryFabric.java`
- Create: `fabric/src/client/java/cc/spea/betterinventory/client/BetterInventoryFabricClient.java`
- Create: `fabric/src/test/java/cc/spea/betterinventory/network/SwapPayloadTest.java`

**Interfaces:**
- Produces an empty `SwapHotbarPayload` with a stable `Identifier`.
- Server receiver calls `HotbarSwapService.swap` only on its server executor and then marks/synchronizes the player inventory.

- [ ] **Step 1: Write failing payload and server-dispatch tests**

```java
@Test
void payload_round_trips_through_its_stream_codec() {
    assertThat(decode(encode(SwapHotbarPayload.INSTANCE))).isEqualTo(SwapHotbarPayload.INSTANCE);
}

@Test
void server_request_swaps_all_nine_pairs() {
    List<ItemStack> inventory = testInventoryWithNamedStacks();
    handleServerRequest(inventory);
    assertThat(inventory.get(0).getCustomName().getString()).isEqualTo("extra-0");
}
```

- [ ] **Step 2: Run tests to verify failure**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test :fabric:test --tests "*SwapPayloadTest" --no-daemon`

Expected: FAIL because no payload codec/receiver exists.

- [ ] **Step 3: Implement payload registration and the `H` keybind**

Register the Architectury payload on both sides. Register a standard client key binding in the Better Inventory category with default key `H`; on each client tick, send exactly one payload per press. In the server receiver, queue work on the server thread, reject unavailable players, exchange all nine stacks through `HotbarSwapService`, and invoke the mapped inventory-dirty/sync path. Define `encode`, `decode`, and `handleServerRequest` as package-private test helpers in `SwapPayloadTest`; they use the production stream codec and a test list of 45 stacks.

- [ ] **Step 4: Run payload, common, and Fabric tests**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test :fabric:test --no-daemon`

Expected: PASS; no client-only class appears in common or dedicated-server code.

- [ ] **Step 5: Commit networking and keybind**

Run: `git add common fabric; git commit -m "feat: add server-authoritative hotbar swap"`

### Task 6: Extend survival and creative visual layouts

**Files:**
- Create: `fabric/src/client/java/cc/spea/betterinventory/client/mixin/InventoryScreenMixin.java`
- Create: `fabric/src/client/java/cc/spea/betterinventory/client/mixin/CreativeInventoryScreenMixin.java`
- Modify: `fabric/src/client/resources/better-inventory.client.mixins.json`
- Create: `fabric/src/clientTest/java/cc/spea/betterinventory/client/InventoryLayoutTest.java`

**Interfaces:**
- Produces an 18-pixel taller attached player-grid background and hotbar offset in survival and creative inventory displays.

- [ ] **Step 1: Write failing layout-coordinate tests**

```java
@Test
void fourth_inventory_row_is_directly_above_the_hotbar() {
    InventoryLayout layout = InventoryLayout.forPlayerInventory();
    assertThat(layout.extraRowY()).isEqualTo(layout.hotbarY() - 18);
}

@Test
void creative_layout_uses_the_same_extra_row_offset() {
    assertThat(InventoryLayout.forCreativeInventory().extraRowY())
        .isEqualTo(InventoryLayout.forCreativeInventory().hotbarY() - 18);
}
```

- [ ] **Step 2: Run tests to verify failure**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:clientTest --tests "*InventoryLayoutTest" --no-daemon`

Expected: FAIL because no layout helper/mixins exist.

- [ ] **Step 3: Implement attached vanilla-style screen extension**

Redirect the mapped background draw dimensions and slot y positions for player inventory and the creative inventory tab. Reuse vanilla inventory texture/render calls; make the added row adjacent to the existing grid and move the hotbar down one 18-pixel row. Do not add a bordered panel, custom stack limit behavior, or custom GUI art.

- [ ] **Step 4: Run client tests and launch the client**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:clientTest :fabric:runClient --no-daemon`

Expected: tests PASS; client reaches the title screen without mixin or texture errors. Manually open survival and creative inventory and inspect the attached fourth row.

- [ ] **Step 5: Commit GUI integration**

Run: `git add fabric; git commit -m "feat: render attached fourth inventory row"`

### Task 7: Validate release behavior and publish the repository

**Files:**
- Modify: `README.md`, `fabric/src/main/resources/fabric.mod.json`
- Create: `.github/workflows/build.yml` only if the template workflow cannot build `:fabric:build`

**Interfaces:**
- Produces documented both-side installation instructions and a GitHub source URL.

- [ ] **Step 1: Run the whole automated verification suite**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :common:test :fabric:test :fabric:build --no-daemon`

Expected: `BUILD SUCCESSFUL` and all test reports show zero failures.

- [ ] **Step 2: Run dedicated-server verification**

Run: `set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot; .\gradlew.bat :fabric:runServer --no-daemon`

Expected: server initializes Better Inventory without a missing client class, payload registration error, or mixin error.

- [ ] **Step 3: Perform acceptance smoke tests with both endpoints**

Start the Fabric client and server with the built mod. Verify survival/creative 4×9 presentation, `H` swap while no inventory screen is open, Controls rebinding, save/reload, dimension change, death/respawn drops, and that a chest retains 27 slots and item max counts are unchanged.

- [ ] **Step 4: Update release documentation and commit**

Document Java 25, Fabric 26.2, both-side installation, the default/rebindable `H` control, and current incompatibility caveat. Replace the source URL with `https://github.com/R2bEEaton-Minecraft/better-inventory`.

Run: `git add README.md fabric/src/main/resources/fabric.mod.json .github; git commit -m "docs: prepare Better Inventory release"`

- [ ] **Step 5: Publish the repository**

Run: `gh repo create R2bEEaton-Minecraft/better-inventory --public --source . --remote origin --push`

Expected: GitHub CLI reports the new repository URL and `git status --short` is empty.
