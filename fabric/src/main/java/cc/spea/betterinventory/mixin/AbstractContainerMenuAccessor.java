package cc.spea.betterinventory.mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractContainerMenu.class)
interface AbstractContainerMenuAccessor {
	@Invoker("addSlot")
	Slot betterinventory$addSlot(Slot slot);
}
