package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.CreativeInventoryLayout;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CreativeModeInventoryScreen.ItemPickerMenu.class)
abstract class CreativeItemPickerMenuMixin {
	@ModifyArg(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$ItemPickerMenu;addInventoryHotbarSlots(Lnet/minecraft/world/Container;II)V"
		),
		index = 2
	)
	private int betterinventory$moveCreativeCatalogHotbarBelowSixthRow(int originalY) {
		return CreativeInventoryLayout.catalogHotbarY(originalY);
	}

	@ModifyConstant(method = {"<init>", "scrollTo", "calculateRowCount"}, constant = @Constant(intValue = 5))
	private int betterinventory$useSixCatalogRows(int originalRows) {
		return CreativeInventoryLayout.CATALOG_ROWS;
	}

	@ModifyConstant(method = "canScroll", constant = @Constant(intValue = 45))
	private int betterinventory$scrollAfterSixCatalogRows(int originalCapacity) {
		return CreativeInventoryLayout.catalogSlotCount();
	}
}
