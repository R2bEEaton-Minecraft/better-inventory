package cc.spea.betterinventory.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class HotbarSwapPolicyTest {
	@Test
	void blocks_swaps_while_a_creative_screen_is_open() {
		assertThat(HotbarSwapPolicy.shouldSendSwap(false)).isTrue();
		assertThat(HotbarSwapPolicy.shouldSendSwap(true)).isFalse();
	}
}
