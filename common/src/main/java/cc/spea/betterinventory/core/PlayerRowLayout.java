package cc.spea.betterinventory.core;

/** Shared geometry for Better Inventory's player row in container menus. */
public final class PlayerRowLayout {
	public static final int ROW_HEIGHT = 18;
	public static final int HOTBAR_GAP = 4;

	private PlayerRowLayout() {
	}

	public static int extraRowY(int hotbarY) {
		return hotbarY - HOTBAR_GAP;
	}

	public static int movedHotbarY(int hotbarY) {
		return hotbarY + ROW_HEIGHT;
	}

	public static int expandedScreenHeight(int originalHeight) {
		return originalHeight + ROW_HEIGHT;
	}

	public static int expandedGenericPlayerSectionHeight(int originalHeight) {
		return originalHeight + ROW_HEIGHT;
	}
}
