package cc.spea.betterinventory.inventory;

import cc.spea.betterinventory.core.InventorySlots;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Container view that lets vanilla menu slots synchronize the added player-inventory row. */
public final class ExtraRowContainer implements Container {
	private final Inventory inventory;

	public ExtraRowContainer(Inventory inventory) {
		this.inventory = inventory;
	}

	private ExpandedInventoryAccess extraRow() {
		return (ExpandedInventoryAccess) this.inventory;
	}

	@Override
	public int getContainerSize() {
		return InventorySlots.EXTRA_ROW_SIZE;
	}

	@Override
	public boolean isEmpty() {
		return this.extraRow().betterinventory$getExtraRow().stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.extraRow().betterinventory$getExtraRow().get(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		ItemStack stack = this.getItem(slot);
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack removed = stack.split(amount);
		if (stack.isEmpty()) {
			this.setItem(slot, ItemStack.EMPTY);
		} else {
			this.setChanged();
		}
		return removed;
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return this.extraRow().betterinventory$getExtraRow().set(slot, ItemStack.EMPTY);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.extraRow().betterinventory$getExtraRow().set(slot, stack);
		this.setChanged();
	}

	@Override
	public void setChanged() {
		this.inventory.setChanged();
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

	@Override
	public void clearContent() {
		this.extraRow().betterinventory$getExtraRow().clear();
	}

}
