package com.wynprice.secretroomsmod.render;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSecretChest extends Container
{
	
	private final IItemHandler handler;	
	public ContainerSecretChest(IItemHandler handler, EntityPlayer player) 
	{
		this.handler = handler;
		for (int j = 0; j < 3; ++j)
            for (int k = 0; k < 9; ++k)
                this.addSlotToContainer(new SlotItemHandler(handler, k + j * 9, 8 + k * 18, 18 + j * 18));
		for(int y = 0; y < 3; ++y)
			for(int x = 0; x < 9; ++x)
				this.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, 8 + x * 18, 85 + y * 18));
		for(int x = 0; x < 9; ++x) 
			this.addSlotToContainer(new Slot(player.inventory, x, 8 + x * 18, 85 + 58));
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
	    ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < handler.getSlots()) {
	            if (!this.mergeItemStack(current, handler.getSlots(), 63, true))
	                return ItemStack.EMPTY;
	        } else {
	            if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
	                return ItemStack.EMPTY;
	        }
	        if (current.getCount() == 0)
	            slot.putStack(ItemStack.EMPTY);
	        else
	            slot.onSlotChanged();

	        if (current.getCount() == previous.getCount())
	            return null;
	        slot.onTake(playerIn, current);
	    }
	    return previous;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
