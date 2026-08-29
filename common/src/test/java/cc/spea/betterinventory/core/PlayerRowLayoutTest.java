package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlayerRowLayoutTest {
	@Test
	void places_extra_row_in_the_four_pixel_gap_before_the_hotbar() {
		assertThat(PlayerRowLayout.extraRowY(142)).isEqualTo(138);
		assertThat(PlayerRowLayout.movedHotbarY(142)).isEqualTo(160);
	}

	@Test
	void expands_screen_and_generic_player_section_for_the_inserted_row() {
		assertThat(PlayerRowLayout.expandedScreenHeight(166)).isEqualTo(184);
		assertThat(PlayerRowLayout.expandedGenericPlayerSectionHeight(96)).isEqualTo(114);
	}
}
