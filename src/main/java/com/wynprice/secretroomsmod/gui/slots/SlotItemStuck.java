package com.wynprice.secretroomsmod.gui.slots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotItemStuck extends Slot{

	private final ItemStack stack;
	
	public SlotItemStuck(ItemStack stack, int xPosition, int yPosition) {
		super(new InventoryBasic("[Null]", true, 0), 0, xPosition, yPosition);
		this.stack = stack;
	}
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return false;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack) {
		
	}
	
	@Override
	public ItemStack getStack() {
		return stack;
	}

}
