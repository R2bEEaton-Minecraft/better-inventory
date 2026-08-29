package cc.spea.betterinventory.core;

/** Identifies a destination slot for an automatically collected stack. */
public record SlotDestination(boolean extraRow, int index) {
	public static final SlotDestination NONE = new SlotDestination(false, -1);
}
