package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.SurvivalInventoryLayout;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
abstract class InventoryScreenMixin {
	@Inject(method = "extractBackground", at = @At("TAIL"))
	private void betterinventory$renderExtendedInventoryBackground(
		GuiGraphicsExtractor graphics,
		int mouseX,
		int mouseY,
		float tickDelta,
		CallbackInfo ci
	) {
		AbstractContainerScreenAccessor screen = (AbstractContainerScreenAccessor) (Object) this;
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			AbstractContainerScreen.INVENTORY_LOCATION,
			screen.betterinventory$getLeftPos(),
			screen.betterinventory$getTopPos() + screen.betterinventory$getImageHeight(),
			0.0F,
			screen.betterinventory$getImageHeight(),
			screen.betterinventory$getImageWidth(),
			SurvivalInventoryLayout.EXTRA_ROW_HEIGHT,
			256,
			256
		);
	}
}
