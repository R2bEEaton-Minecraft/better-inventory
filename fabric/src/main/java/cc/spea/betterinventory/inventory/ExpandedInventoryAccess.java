package cc.spea.betterinventory.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

/** Access implemented by the player inventory mixin for Better Inventory's added row. */
public interface ExpandedInventoryAccess {
	NonNullList<ItemStack> betterinventory$getExtraRow();
}
