package cc.spea.betterinventory.client.mixin;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
abstract class InventoryScreenMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void betterinventory$extendInventoryBackground(Player player, CallbackInfo ci) {
		((AbstractContainerScreenAccessor) (Object) this).betterinventory$setImageHeight(184);
	}
}
