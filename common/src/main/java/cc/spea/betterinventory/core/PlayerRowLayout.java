package cc.spea.betterinventory.core;

/** Shared geometry for Better Inventory's player row in container menus. */
public final class PlayerRowLayout {
	public static final int ROW_HEIGHT = 18;
	public static final int HOTBAR_GAP = 4;
	private static final String CREATIVE_ITEM_PICKER_MENU = "net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$ItemPickerMenu";
	private static final String INVENTORY_MENU = "net.minecraft.world.inventory.InventoryMenu";

	private PlayerRowLayout() {
	}

	public static int extraRowY(int hotbarY) {
		return hotbarY - HOTBAR_GAP;
	}

	public static int movedHotbarY(int hotbarY) {
		return hotbarY + ROW_HEIGHT;
	}

	public static int inventoryMenuHotbarY(int hotbarY) {
		return hotbarY + ROW_HEIGHT;
	}

	public static int inventoryRecipeBookButtonY(int originalY) {
		return originalY - 10;
	}

	public static int expandedScreenHeight(int originalHeight) {
		return originalHeight + ROW_HEIGHT;
	}

	public static int expandedGenericPlayerSectionHeight(int originalHeight) {
		return originalHeight + ROW_HEIGHT;
	}

	public static int inventoryLabelY(int expandedScreenHeight) {
		return expandedScreenHeight - inventoryLabelBottomMargin();
	}

	public static int inventoryLabelBottomMargin() {
		return 94 + ROW_HEIGHT;
	}

	public static boolean shouldAddExtraRow(String menuClassName) {
		return !CREATIVE_ITEM_PICKER_MENU.equals(menuClassName) && !INVENTORY_MENU.equals(menuClassName);
	}

	public static boolean shouldMoveHotbar(String menuClassName) {
		return shouldAddExtraRow(menuClassName) || INVENTORY_MENU.equals(menuClassName);
	}
}
