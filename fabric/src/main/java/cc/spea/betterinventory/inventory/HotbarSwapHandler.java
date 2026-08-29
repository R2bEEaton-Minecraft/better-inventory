package cc.spea.betterinventory.inventory;

import cc.spea.betterinventory.core.InventorySlots;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

/** Performs the server-authoritative exchange of the normal and alternate hotbars. */
public final class HotbarSwapHandler {
	private HotbarSwapHandler() {
	}

	public static void swap(Inventory inventory) {
		ExpandedInventoryAccess expanded = (ExpandedInventoryAccess) inventory;
		for (int slot = 0; slot < InventorySlots.HOTBAR_SIZE; slot++) {
			ItemStack current = inventory.getItem(slot);
			inventory.setItem(slot, expanded.betterinventory$getExtraRow().get(slot));
			expanded.betterinventory$getExtraRow().set(slot, current);
		}
		inventory.setChanged();
	}
}
