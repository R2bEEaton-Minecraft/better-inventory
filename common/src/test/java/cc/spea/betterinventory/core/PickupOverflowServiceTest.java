package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PickupOverflowServiceTest {
	@Test
	void chooses_a_normal_inventory_slot_before_the_extra_row() {
		List<Stack> main = List.of(Stack.filled("stone"), Stack.EMPTY, Stack.filled("dirt"));
		List<Stack> extra = List.of(Stack.EMPTY, Stack.EMPTY);

		assertThat(PickupOverflowService.findDestination(main, extra, Stack.filled("dirt"), Stack::empty, Stack::canMerge))
			.isEqualTo(new SlotDestination(false, 2));
	}

	@Test
	void chooses_the_extra_row_when_all_normal_slots_are_full() {
		List<Stack> main = List.of(Stack.filled("stone"), Stack.filled("sand"), Stack.filled("gravel"));
		List<Stack> extra = List.of(Stack.filled("stone"), Stack.EMPTY);

		assertThat(PickupOverflowService.findDestination(main, extra, Stack.filled("dirt"), Stack::empty, Stack::canMerge))
			.isEqualTo(new SlotDestination(true, 1));
	}

	private record Stack(String item, boolean empty) {
		private static final Stack EMPTY = new Stack("", true);

		private static Stack filled(String item) {
			return new Stack(item, false);
		}

		private boolean canMerge(Stack other) {
			return !this.empty && !other.empty && this.item.equals(other.item);
		}
	}
}
