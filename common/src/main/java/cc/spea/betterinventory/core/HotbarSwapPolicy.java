package cc.spea.betterinventory.core;

/** Protects the authoritative player inventory from Creative picker-menu remapping. */
public final class HotbarSwapPolicy {
	private HotbarSwapPolicy() {
	}

	public static boolean shouldSendSwap(boolean creativeScreenOpen) {
		return !creativeScreenOpen;
	}
}
