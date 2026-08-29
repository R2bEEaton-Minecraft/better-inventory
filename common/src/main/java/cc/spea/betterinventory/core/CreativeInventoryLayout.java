package cc.spea.betterinventory.core;

public final class CreativeInventoryLayout {
	public static final int SLOT_SIZE = 18;
	public static final int CATALOG_ROWS = 6;
	public static final int CATALOG_COLUMNS = 9;

	private CreativeInventoryLayout() {
	}

	public static int catalogSlotCount() {
		return CATALOG_ROWS * CATALOG_COLUMNS;
	}

	public static int catalogSlotY(int row) {
		return SLOT_SIZE + row * SLOT_SIZE;
	}

	public static boolean needsCatalogScroll(int itemCount) {
		return itemCount > catalogSlotCount();
	}

	public static int extendedHeight(int originalHeight) {
		return originalHeight + SLOT_SIZE;
	}

	public static int extendedScrollTrackHeight(int originalHeight) {
		return originalHeight + SLOT_SIZE;
	}

	public static int catalogHotbarY(int originalY) {
		return originalY + SLOT_SIZE;
	}

	public static int destroySlotY() {
		return 130;
	}

	public static int inventoryTabSlotY(int menuSlotIndex, int originalY) {
		if (menuSlotIndex >= 36 && menuSlotIndex <= 44) {
			return catalogHotbarY(originalY);
		}
		return menuSlotIndex >= 46 && menuSlotIndex <= 54 ? 108 : originalY;
	}

	public static int inventoryTabSlotX(int menuSlotIndex, int originalX) {
		return menuSlotIndex >= 46 && menuSlotIndex <= 54
			? 9 + (menuSlotIndex - 46) * SLOT_SIZE
			: originalX;
	}
}
