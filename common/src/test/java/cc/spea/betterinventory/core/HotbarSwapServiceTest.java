package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class HotbarSwapServiceTest {
	@Test
	void swaps_each_hotbar_slot_with_its_matching_extra_row_slot() {
		List<StackState> stacks = inventoryWithNamedStacks();

		HotbarSwapService.swap(stacks);

		assertThat(stacks.get(0).name()).isEqualTo("extra-0");
		assertThat(stacks.get(8).name()).isEqualTo("extra-8");
		assertThat(stacks.get(InventorySlots.EXTRA_ROW_START).name()).isEqualTo("hotbar-0");
		assertThat(stacks.get(InventorySlots.EXTRA_ROW_START + 8).name()).isEqualTo("hotbar-8");
	}

	@Test
	void moves_the_original_objects_without_copying_their_data() {
		List<StackState> stacks = inventoryWithNamedStacks();
		StackState damagedExtraStack = new StackState("extra-0", 17);
		stacks.set(InventorySlots.EXTRA_ROW_START, damagedExtraStack);

		HotbarSwapService.swap(stacks);

		assertThat(stacks.get(0)).isSameAs(damagedExtraStack);
		assertThat(stacks.get(0).damage()).isEqualTo(17);
	}

	private static List<StackState> inventoryWithNamedStacks() {
		List<StackState> stacks = new ArrayList<>(45);
		for (int index = 0; index < 45; index++) {
			stacks.add(new StackState("main-" + index, 0));
		}
		for (int index = 0; index < 9; index++) {
			stacks.set(index, new StackState("hotbar-" + index, 0));
			stacks.set(36 + index, new StackState("extra-" + index, 0));
		}
		return stacks;
	}

	private record StackState(String name, int damage) {
	}
}
