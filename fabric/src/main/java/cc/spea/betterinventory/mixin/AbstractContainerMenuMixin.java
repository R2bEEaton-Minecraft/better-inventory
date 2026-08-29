package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.core.PlayerRowLayout;
import cc.spea.betterinventory.inventory.ExtraRowContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {
	@ModifyArg(
		method = "addInventoryHotbarSlots",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;<init>(Lnet/minecraft/world/Container;III)V"),
		index = 3
	)
	private int betterinventory$moveInventoryMenuHotbarDown(int y) {
		return PlayerRowLayout.shouldAddExtraRow(this.getClass().getName()) ? PlayerRowLayout.movedHotbarY(y) : y;
	}

	@Inject(method = "addInventoryHotbarSlots", at = @At("TAIL"))
	private void betterinventory$addExtraPlayerRow(Container inventory, int left, int top, CallbackInfo ci) {
		if (inventory instanceof Inventory playerInventory && PlayerRowLayout.shouldAddExtraRow(this.getClass().getName())) {
			ExtraRowContainer extraRow = new ExtraRowContainer(playerInventory);
			for (int column = 0; column < 9; column++) {
				((AbstractContainerMenuAccessor) (Object) this).betterinventory$addSlot(
					new Slot(extraRow, column, left + column * 18, PlayerRowLayout.extraRowY(top))
				);
			}
		}
	}
}
