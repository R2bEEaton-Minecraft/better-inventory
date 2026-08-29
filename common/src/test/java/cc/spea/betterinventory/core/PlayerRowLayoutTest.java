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
	void moves_only_the_survival_inventory_hotbar_twenty_pixels_down() {
		assertThat(PlayerRowLayout.inventoryMenuHotbarY(142)).isEqualTo(162);
	}

	@Test
	void expands_screen_and_generic_player_section_for_the_inserted_row() {
		assertThat(PlayerRowLayout.expandedScreenHeight(166)).isEqualTo(184);
		assertThat(PlayerRowLayout.expandedGenericPlayerSectionHeight(96)).isEqualTo(114);
	}

	@Test
	void keeps_the_inventory_label_above_the_original_first_inventory_row() {
		assertThat(PlayerRowLayout.inventoryLabelY(184)).isEqualTo(72);
	}

	@Test
	void leaves_the_creative_picker_to_its_dedicated_hotbar_layout() {
		assertThat(PlayerRowLayout.shouldAddExtraRow("net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen$ItemPickerMenu"))
			.isFalse();
		assertThat(PlayerRowLayout.shouldAddExtraRow("net.minecraft.world.inventory.InventoryMenu")).isFalse();
		assertThat(PlayerRowLayout.shouldAddExtraRow("net.minecraft.world.inventory.ChestMenu")).isTrue();
	}
}
