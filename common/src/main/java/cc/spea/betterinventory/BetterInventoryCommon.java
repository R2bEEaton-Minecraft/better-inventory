package cc.spea.betterinventory;

import net.minecraft.resources.Identifier;

/** Shared mod identity used by all platform modules. */
public final class BetterInventoryCommon {
	public static final String MOD_ID = "better-inventory";

	private BetterInventoryCommon() {
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
