package cc.spea.betterinventory.core;

/** Menu indices assigned to Better Inventory's row after vanilla's offhand slot. */
public final class CreativeExtraRowSlots {
	public static final int FIRST_MENU_SLOT = 46;
	public static final int LAST_MENU_SLOT = FIRST_MENU_SLOT + InventorySlots.EXTRA_ROW_SIZE - 1;

	private CreativeExtraRowSlots() {
	}

	public static boolean isExtraRowMenuSlot(int menuSlot) {
		return menuSlot >= FIRST_MENU_SLOT && menuSlot <= LAST_MENU_SLOT;
	}
}
