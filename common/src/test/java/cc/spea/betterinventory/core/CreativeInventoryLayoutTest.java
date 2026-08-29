package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CreativeInventoryLayoutTest {
	@Test
	void exposes_a_sixth_creative_catalog_row_before_scrolling() {
		assertThat(CreativeInventoryLayout.catalogSlotCount()).isEqualTo(54);
		assertThat(CreativeInventoryLayout.catalogSlotY(5)).isEqualTo(108);
		assertThat(CreativeInventoryLayout.needsCatalogScroll(54)).isFalse();
		assertThat(CreativeInventoryLayout.needsCatalogScroll(55)).isTrue();
	}

	@Test
	void extends_bottom_creative_geometry_by_one_slot_height() {
		assertThat(CreativeInventoryLayout.extendedHeight(136)).isEqualTo(154);
		assertThat(CreativeInventoryLayout.extendedScrollTrackHeight(112)).isEqualTo(130);
	}
}
