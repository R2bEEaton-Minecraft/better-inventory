package cc.spea.betterinventory.network;

import cc.spea.betterinventory.BetterInventoryCommon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Empty client-to-server request to exchange the normal and alternate hotbars. */
public record SwapHotbarPayload() implements CustomPacketPayload {
	public static final SwapHotbarPayload INSTANCE = new SwapHotbarPayload();
	public static final Type<SwapHotbarPayload> TYPE = new Type<>(BetterInventoryCommon.id("swap_hotbar"));
	public static final StreamCodec<RegistryFriendlyByteBuf, SwapHotbarPayload> CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<SwapHotbarPayload> type() {
		return TYPE;
	}
}
