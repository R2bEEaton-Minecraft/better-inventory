package cc.spea.betterinventory;

import net.fabricmc.api.ModInitializer;
import cc.spea.betterinventory.inventory.HotbarSwapHandler;
import cc.spea.betterinventory.network.SwapHotbarPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterInventory implements ModInitializer {
	public static final String MOD_ID = BetterInventoryCommon.MOD_ID;

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.serverboundPlay().register(SwapHotbarPayload.TYPE, SwapHotbarPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(SwapHotbarPayload.TYPE, (payload, context) -> {
			HotbarSwapHandler.swap(context.player().getInventory());
			context.player().inventoryMenu.broadcastChanges();
		});
	}

	public static Identifier id(String path) {
		return BetterInventoryCommon.id(path);
	}
}
