package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.CreativeInventoryLayout;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CreativeModeInventoryScreen.class)
abstract class CreativeModeInventoryScreenMixin {
	@Shadow
	private static CreativeModeTab selectedTab;

	@ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 45))
	private static int betterinventory$expandCreativeCatalogContainer(int originalCapacity) {
		return CreativeInventoryLayout.catalogSlotCount();
	}

	@ModifyConstant(method = {"insideScrollbar", "mouseDragged", "extractBackground"}, constant = @Constant(intValue = 112))
	private int betterinventory$extendCreativeScrollbarTrack(int originalHeight) {
		return CreativeInventoryLayout.extendedScrollTrackHeight(originalHeight);
	}

	@ModifyExpressionValue(
		method = {"getTabY", "extractTabButton", "hasClickedOutside"},
		at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen;imageHeight:I")
	)
	private int betterinventory$extendCreativeBottomBounds(int originalHeight) {
		return CreativeInventoryLayout.extendedHeight(originalHeight);
	}

	@Inject(
		method = "extractBackground",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V",
			shift = At.Shift.AFTER
		)
	)
	private void betterinventory$renderSixthCreativeRowBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
		AbstractContainerScreenAccessor screen = (AbstractContainerScreenAccessor) (Object) this;
		graphics.blit(
			RenderPipelines.GUI_TEXTURED,
			selectedTab.getBackgroundTexture(),
			screen.betterinventory$getLeftPos(),
			screen.betterinventory$getTopPos() + 136,
			0.0F,
			136.0F,
			195,
			CreativeInventoryLayout.SLOT_SIZE,
			256,
			256
		);
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
			args.set(3, CreativeInventoryLayout.fourthInventoryRowY());
		}
	}
}
