package cc.spea.betterinventory.core;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/** Chooses the vanilla inventory before Better Inventory's overflow row. */
public final class PickupOverflowService {
	private PickupOverflowService() {
	}

	public static <T> SlotDestination findDestination(
		List<T> mainSlots,
		List<T> extraSlots,
		T incoming,
		Predicate<T> isEmpty,
		BiPredicate<T, T> canMerge
	) {
		SlotDestination mainDestination = findDestination(mainSlots, incoming, isEmpty, canMerge, false);
		return mainDestination.index() >= 0
			? mainDestination
			: findDestination(extraSlots, incoming, isEmpty, canMerge, true);
	}

	private static <T> SlotDestination findDestination(
		List<T> slots,
		T incoming,
		Predicate<T> isEmpty,
		BiPredicate<T, T> canMerge,
		boolean extraRow
	) {
		for (int index = 0; index < slots.size(); index++) {
			if (canMerge.test(slots.get(index), incoming)) {
				return new SlotDestination(extraRow, index);
			}
		}
		for (int index = 0; index < slots.size(); index++) {
			if (isEmpty.test(slots.get(index))) {
				return new SlotDestination(extraRow, index);
			}
		}
		return SlotDestination.NONE;
	}
}
