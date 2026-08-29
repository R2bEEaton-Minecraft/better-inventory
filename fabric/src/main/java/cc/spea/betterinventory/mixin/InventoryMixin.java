package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.inventory.ExpandedInventoryAccess;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
abstract class InventoryMixin implements ExpandedInventoryAccess {
	@Unique
	private static final int BETTER_INVENTORY_SAVE_SLOT_START = 100;
	@Unique
	private final NonNullList<ItemStack> betterinventory$extraRow = NonNullList.withSize(9, ItemStack.EMPTY);

	@Override
	public NonNullList<ItemStack> betterinventory$getExtraRow() {
		return this.betterinventory$extraRow;
	}

	@Inject(method = "save", at = @At("TAIL"))
	private void betterinventory$saveExtraRow(ValueOutput.TypedOutputList<ItemStackWithSlot> output, CallbackInfo ci) {
		for (int slot = 0; slot < this.betterinventory$extraRow.size(); slot++) {
			ItemStack stack = this.betterinventory$extraRow.get(slot);
			if (!stack.isEmpty()) {
				output.add(new ItemStackWithSlot(BETTER_INVENTORY_SAVE_SLOT_START + slot, stack));
			}
		}
	}

	@Inject(method = "load", at = @At("TAIL"))
	private void betterinventory$loadExtraRow(ValueInput.TypedInputList<ItemStackWithSlot> input, CallbackInfo ci) {
		this.betterinventory$extraRow.clear();
		for (ItemStackWithSlot item : input) {
			int slot = item.slot() - BETTER_INVENTORY_SAVE_SLOT_START;
			if (slot >= 0 && slot < this.betterinventory$extraRow.size()) {
				this.betterinventory$extraRow.set(slot, item.stack());
			}
		}
	}

}
