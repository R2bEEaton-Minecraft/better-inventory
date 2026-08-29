package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.core.ExtraRowDeathService;
import cc.spea.betterinventory.inventory.ExpandedInventoryAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Drops Better Inventory's extra row alongside the vanilla player inventory. */
@Mixin(Player.class)
abstract class PlayerMixin {
	@Shadow
	@Final
	private Inventory inventory;

	@Inject(method = "dropEquipment", at = @At("TAIL"))
	private void betterinventory$dropExtraRow(ServerLevel level, CallbackInfo ci) {
		if (!level.getGameRules().get(GameRules.KEEP_INVENTORY)) {
			Player player = (Player) (Object) this;
			ExtraRowDeathService.dropAndClear(
				((ExpandedInventoryAccess) this.inventory).betterinventory$getExtraRow(),
				ItemStack::isEmpty,
				ItemStack.EMPTY,
				stack -> player.drop(stack, true, false)
			);
			this.inventory.setChanged();
		}
	}
}
