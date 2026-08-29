package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.SurvivalInventoryLayout;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin {
	@ModifyExpressionValue(
		method = "hasClickedOutside",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;imageHeight:I")
	)
	private int betterinventory$includeSurvivalInventoryBottomRow(int originalHeight) {
		return (Object) this instanceof InventoryScreen ? SurvivalInventoryLayout.extendedHeight(originalHeight) : originalHeight;
	}
}
