package cc.spea.betterinventory.client.mixin;

import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CreativeModeInventoryScreen.class)
abstract class CreativeModeInventoryScreenMixin {
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 136))
	private int betterinventory$extendCreativeInventoryHeight(int originalHeight) {
		return 154;
	}

	@ModifyArgs(
		method = "selectTab",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;<init>(Lnet/minecraft/world/inventory/Slot;III)V"
		)
	)
	private void betterinventory$positionFourthRowInCreativeInventory(Args args) {
		int menuSlotIndex = args.get(1);
		if (menuSlotIndex >= 36 && menuSlotIndex <= 44) {
			args.set(3, 130);
		} else if (menuSlotIndex >= 46 && menuSlotIndex <= 54) {
			args.set(2, 9 + (menuSlotIndex - 46) * 18);
			args.set(3, 112);
		}
	}
}
