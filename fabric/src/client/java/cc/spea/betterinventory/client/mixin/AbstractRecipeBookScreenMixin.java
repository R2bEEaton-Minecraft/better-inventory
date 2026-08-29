package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.SurvivalInventoryLayout;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractRecipeBookScreen.class)
abstract class AbstractRecipeBookScreenMixin {
	@ModifyExpressionValue(
		method = "hasClickedOutside",
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractRecipeBookScreen;imageHeight:I")
	)
	private int betterinventory$extendSurvivalRecipeBookBounds(int originalHeight) {
		return (Object) this instanceof InventoryScreen ? SurvivalInventoryLayout.outsideClickHeight(originalHeight) : originalHeight;
	}
}
