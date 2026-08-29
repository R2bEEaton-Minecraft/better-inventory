package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.PlayerRowLayout;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractContainerScreen.class)
abstract class AbstractContainerScreenMixin {
	@ModifyConstant(method = "<init>", constant = @Constant(intValue = 166))
	private static int betterinventory$includeInsertedPlayerRowInDefaultScreenHeight(int originalHeight) {
		return PlayerRowLayout.expandedScreenHeight(originalHeight);
	}
}
