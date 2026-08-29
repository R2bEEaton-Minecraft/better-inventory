package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class ExtraRowDeathServiceTest {
	@Test
	void drops_each_non_empty_stack_and_clears_the_row() {
		List<StackState> row = new ArrayList<>(List.of(new StackState("diamond"), StackState.EMPTY, new StackState("pickaxe")));
		List<StackState> dropped = new ArrayList<>();

		ExtraRowDeathService.dropAndClear(row, StackState::isEmpty, StackState.EMPTY, dropped::add);

		assertThat(dropped).containsExactly(new StackState("diamond"), new StackState("pickaxe"));
		assertThat(row).containsExactly(StackState.EMPTY, StackState.EMPTY, StackState.EMPTY);
	}

	@Test
	void copies_the_old_players_extra_row_to_the_replacement_player() {
		List<StackState> oldRow = List.of(new StackState("diamond"), StackState.EMPTY, new StackState("pickaxe"));
		List<StackState> replacementRow = new ArrayList<>(List.of(new StackState("dirt"), new StackState("stone"), new StackState("sand")));

		ExtraRowDeathService.copy(oldRow, replacementRow);

		assertThat(replacementRow).containsExactlyElementsOf(oldRow);
	}

	private record StackState(String name) {
		private static final StackState EMPTY = new StackState("empty");

		private boolean isEmpty() {
			return this.equals(EMPTY);
		}
	}
}
