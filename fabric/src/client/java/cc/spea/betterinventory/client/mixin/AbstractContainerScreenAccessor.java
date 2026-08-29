package cc.spea.betterinventory.client.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
interface AbstractContainerScreenAccessor {
	@Accessor("imageHeight")
	void betterinventory$setImageHeight(int imageHeight);
}
