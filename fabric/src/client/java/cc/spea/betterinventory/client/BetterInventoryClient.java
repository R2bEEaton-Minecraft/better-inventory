package cc.spea.betterinventory.client;

import com.mojang.blaze3d.platform.InputConstants;
import cc.spea.betterinventory.network.SwapHotbarPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import cc.spea.betterinventory.core.HotbarSwapPolicy;
import org.lwjgl.glfw.GLFW;

public class BetterInventoryClient implements ClientModInitializer {
	private static final KeyMapping SWAP_HOTBAR = KeyMappingHelper.registerKeyMapping(
		new KeyMapping("key.better-inventory.swap_hotbar", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KeyMapping.Category.INVENTORY)
	);

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (SWAP_HOTBAR.consumeClick()) {
				if (client.player != null && HotbarSwapPolicy.shouldSendSwap(client.gui.screen() instanceof CreativeModeInventoryScreen)) {
					ClientPlayNetworking.send(SwapHotbarPayload.INSTANCE);
				}
			}
		});
	}
}
