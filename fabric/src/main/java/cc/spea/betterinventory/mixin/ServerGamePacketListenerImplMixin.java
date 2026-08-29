package cc.spea.betterinventory.mixin;

import cc.spea.betterinventory.core.CreativeExtraRowSlots;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Extends vanilla Creative inventory packet handling to Better Inventory's row. */
@Mixin(ServerGamePacketListenerImpl.class)
abstract class ServerGamePacketListenerImplMixin {
	@Shadow
	@Final
	private ServerPlayer player;

	@Inject(method = "handleSetCreativeModeSlot", at = @At("HEAD"), cancellable = true)
	private void betterinventory$setCreativeExtraRowSlot(ServerboundSetCreativeModeSlotPacket packet, CallbackInfo ci) {
		int menuSlot = packet.slotNum();
		if (!CreativeExtraRowSlots.isExtraRowMenuSlot(menuSlot)) {
			return;
		}

		ItemStack stack = packet.itemStack();
		if (this.player.hasInfiniteMaterials()
			&& stack.isItemEnabled(this.player.level().enabledFeatures())
			&& (stack.isEmpty() || stack.getCount() <= stack.getMaxStackSize())) {
			this.player.inventoryMenu.getSlot(menuSlot).setByPlayer(stack);
			this.player.inventoryMenu.setRemoteSlot(menuSlot, stack);
			this.player.inventoryMenu.broadcastChanges();
		}
		ci.cancel();
	}
}
