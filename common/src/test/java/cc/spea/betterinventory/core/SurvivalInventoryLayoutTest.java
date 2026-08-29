package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SurvivalInventoryLayoutTest {
	@Test
	void adds_one_slot_height_to_the_vanilla_inventory_background() {
		assertThat(SurvivalInventoryLayout.extendedHeight(166)).isEqualTo(184);
	}
}
