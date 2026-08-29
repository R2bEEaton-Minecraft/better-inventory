package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.inventory.ExtraRowContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
abstract class InventoryMenuMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void betterinventory$addExtraInventoryRow(Inventory inventory, boolean active, Player owner, CallbackInfo ci) {
		ExtraRowContainer extraRow = new ExtraRowContainer(inventory);
		for (int column = 0; column < 9; column++) {
			((AbstractContainerMenuAccessor) (Object) this).betterinventory$addSlot(
				new Slot(extraRow, column, 8 + column * 18, 138)
			);
		}
	}
}
