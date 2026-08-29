package cc.spea.betterinventory.core;

import java.util.List;

/** Exchanges the normal hotbar with Better Inventory's alternate row. */
public final class HotbarSwapService {
	private HotbarSwapService() {
	}

	public static <T> void swap(List<T> stacks) {
		if (stacks.size() < InventorySlots.EXPANDED_SIZE) {
			throw new IllegalArgumentException("Expanded inventory must contain 45 slots");
		}

		for (int offset = 0; offset < InventorySlots.HOTBAR_SIZE; offset++) {
			int hotbarIndex = InventorySlots.HOTBAR_START + offset;
			int extraRowIndex = InventorySlots.EXTRA_ROW_START + offset;
			T hotbarStack = stacks.get(hotbarIndex);
			stacks.set(hotbarIndex, stacks.get(extraRowIndex));
			stacks.set(extraRowIndex, hotbarStack);
		}
	}
}
