package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.inventory.ExpandedInventoryAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Uses the fourth row only after vanilla inventory pickup finds no destination. */
@Mixin(Inventory.class)
abstract class InventoryPickupMixin {
	@Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
	private void betterinventory$addOverflowToExtraRow(int slot, ItemStack incoming, CallbackInfoReturnable<Boolean> cir) {
		if (slot != -1 || cir.getReturnValue() || incoming.isEmpty()) {
			return;
		}

		NonNullList<ItemStack> extraRow = ((ExpandedInventoryAccess) this).betterinventory$getExtraRow();
		boolean acceptedAny = false;
		for (ItemStack existing : extraRow) {
			if (!incoming.isEmpty() && ItemStack.isSameItemSameComponents(existing, incoming) && existing.isStackable()) {
				int accepted = Math.min(incoming.getCount(), existing.getMaxStackSize() - existing.getCount());
				if (accepted > 0) {
					existing.grow(accepted);
					incoming.shrink(accepted);
					acceptedAny = true;
				}
			}
		}
		for (int index = 0; index < extraRow.size() && !incoming.isEmpty(); index++) {
			if (extraRow.get(index).isEmpty()) {
				extraRow.set(index, incoming.copyAndClear());
				acceptedAny = true;
			}
		}
		if (acceptedAny) {
			((Inventory) (Object) this).setChanged();
			cir.setReturnValue(true);
		}
	}
}
