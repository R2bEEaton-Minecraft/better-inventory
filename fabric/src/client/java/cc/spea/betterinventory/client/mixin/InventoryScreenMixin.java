package cc.spea.betterinventory.client.mixin;

import cc.spea.betterinventory.core.PlayerRowLayout;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Positions the survival inventory's recipe-book button above the added row. */
@Mixin(InventoryScreen.class)
abstract class InventoryScreenMixin {
	@ModifyArg(
		method = "getRecipeBookButtonPosition",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/navigation/ScreenPosition;<init>(II)V"),
		index = 1
	)
	private int betterinventory$moveRecipeBookButtonUp(int originalY) {
		return PlayerRowLayout.inventoryRecipeBookButtonY(originalY);
	}
}
