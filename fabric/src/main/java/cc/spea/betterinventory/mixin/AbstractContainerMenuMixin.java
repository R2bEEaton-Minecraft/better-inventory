package cc.spea.betterinventory.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {
	@ModifyArg(
		method = "addInventoryHotbarSlots",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;<init>(Lnet/minecraft/world/Container;III)V"),
		index = 3
	)
	private int betterinventory$moveInventoryMenuHotbarDown(int y) {
		return (Object) this instanceof InventoryMenu ? y + 18 : y;
	}
}
