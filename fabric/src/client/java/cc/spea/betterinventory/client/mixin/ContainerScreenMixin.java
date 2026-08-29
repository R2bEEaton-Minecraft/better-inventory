package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.PlayerRowLayout;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ContainerScreen.class)
abstract class ContainerScreenMixin {
	@ModifyArg(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;<init>(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;II)V"
		),
		index = 4
	)
	private static int betterinventory$includeInsertedPlayerRowInContainerHeight(int originalHeight) {
		return PlayerRowLayout.expandedScreenHeight(originalHeight);
	}

	@ModifyArg(
		method = "extractBackground",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIFFIIII)V"
		),
		index = 7
	)
	private int betterinventory$renderInsertedPlayerRowInContainerSection(int originalHeight) {
		return originalHeight == 96 ? PlayerRowLayout.expandedGenericPlayerSectionHeight(originalHeight) : originalHeight;
	}
}
