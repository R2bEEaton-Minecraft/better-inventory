package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CreativeExtraRowSlotsTest {
	@Test
	void maps_the_creative_menu_extra_row_to_its_nine_container_slots() {
		assertThat(CreativeExtraRowSlots.isExtraRowMenuSlot(45)).isFalse();
		assertThat(CreativeExtraRowSlots.isExtraRowMenuSlot(46)).isTrue();
		assertThat(CreativeExtraRowSlots.isExtraRowMenuSlot(54)).isTrue();
		assertThat(CreativeExtraRowSlots.isExtraRowMenuSlot(55)).isFalse();
	}
}
