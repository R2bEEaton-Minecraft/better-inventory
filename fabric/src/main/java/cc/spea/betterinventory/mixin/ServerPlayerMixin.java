package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.core.ExtraRowDeathService;
import cc.spea.betterinventory.inventory.ExpandedInventoryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Carries Better Inventory's extra row through vanilla inventory restoration. */
@Mixin(ServerPlayer.class)
abstract class ServerPlayerMixin {
	@Inject(method = "restoreFrom", at = @At("TAIL"))
	private void betterinventory$restoreExtraRow(ServerPlayer oldPlayer, boolean restoreAll, CallbackInfo ci) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		if (restoreAll || player.level().getGameRules().get(GameRules.KEEP_INVENTORY) || oldPlayer.isSpectator()) {
			ExtraRowDeathService.copy(
				((ExpandedInventoryAccess) oldPlayer.getInventory()).betterinventory$getExtraRow(),
				((ExpandedInventoryAccess) player.getInventory()).betterinventory$getExtraRow()
			);
			player.getInventory().setChanged();
		}
	}
}
