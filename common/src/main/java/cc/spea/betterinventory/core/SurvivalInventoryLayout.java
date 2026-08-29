package cc.spea.betterinventory.core;

public final class SurvivalInventoryLayout {
	public static final int EXTRA_ROW_HEIGHT = 18;

	private SurvivalInventoryLayout() {
	}

	public static int extendedHeight(int originalHeight) {
		return originalHeight + EXTRA_ROW_HEIGHT;
	}

	public static int outsideClickHeight(int originalHeight) {
		return extendedHeight(originalHeight);
	}
}
