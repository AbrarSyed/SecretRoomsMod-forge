package com.wynprice.secretroomsmod.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerProgrammableProbe extends Container
{
	
	public ContainerProgrammableProbe() 
	{		}
	
	@Override
	public boolean canDragIntoSlot(Slot slotIn) {
		return false;
	}
	
	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return false;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

}
