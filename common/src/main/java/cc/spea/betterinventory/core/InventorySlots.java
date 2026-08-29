package cc.spea.betterinventory.core;

/** Canonical player-inventory positions used by Better Inventory. */
public final class InventorySlots {
	public static final int HOTBAR_START = 0;
	public static final int HOTBAR_SIZE = 9;
	public static final int VANILLA_MAIN_SIZE = 36;
	public static final int EXTRA_ROW_START = VANILLA_MAIN_SIZE;
	public static final int EXTRA_ROW_SIZE = 9;
	public static final int EXPANDED_SIZE = EXTRA_ROW_START + EXTRA_ROW_SIZE;

	private InventorySlots() {
	}
}
