package com.wynprice.secretroomsmod.render;

import com.wynprice.secretroomsmod.blocks.SecretChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSecretChest extends Container
{
	
	private final IItemHandler handler;	
	private final BlockPos pos;
	
	public ContainerSecretChest(BlockPos pos, IItemHandler handler, EntityPlayer player) 
	{
		this.handler = handler;
		this.pos = pos;
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
	    ItemStack previous = null;
	    Slot slot = (Slot) this.inventorySlots.get(fromSlot);

	    if (slot != null && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();
	        if (fromSlot < handler.getSlots()) {
	            if (!this.mergeItemStack(current, handler.getSlots(), 63, true))
	                return null;
	        } else {
	            if (!this.mergeItemStack(current, 0, handler.getSlots(), false))
	                return null;
	        }
	        if (current.stackSize == 0)
	            slot.putStack(null);
	        else
	            slot.onSlotChanged();

	        if (current.stackSize == previous.stackSize)
	            return null;
	        slot.onPickupFromSlot(playerIn, current);
	        return previous;
	    }
	    return previous;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) 
	{
		SecretChest.PLAYERS_USING_MAP.get(playerIn.worldObj.isRemote).put(pos, SecretChest.PLAYERS_USING_MAP.get(playerIn.worldObj.isRemote).get(pos) - 1);
		playerIn.worldObj.notifyNeighborsOfStateChange(pos, playerIn.worldObj.getBlockState(pos).getBlock());
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
