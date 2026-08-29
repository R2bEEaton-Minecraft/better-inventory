package cc.spea.betterinventory.core;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** Applies death and respawn transitions to Better Inventory's separate extra row. */
public final class ExtraRowDeathService {
	private ExtraRowDeathService() {
	}

	public static <T> void dropAndClear(List<T> row, Predicate<T> isEmpty, T emptyStack, Consumer<T> dropper) {
		for (int slot = 0; slot < row.size(); slot++) {
			T stack = row.get(slot);
			if (!isEmpty.test(stack)) {
				dropper.accept(stack);
				row.set(slot, emptyStack);
			}
		}
	}

	public static <T> void copy(List<T> source, List<T> destination) {
		if (source.size() != destination.size()) {
			throw new IllegalArgumentException("Extra rows must have the same number of slots");
		}

		for (int slot = 0; slot < source.size(); slot++) {
			destination.set(slot, source.get(slot));
		}
	}
}
