package cc.spea.betterinventory.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin {
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 166))
	private int betterinventory$extendSurvivalInventoryHeight(int originalHeight) {
		return (Object) this instanceof InventoryScreen ? 184 : originalHeight;
	}
}
