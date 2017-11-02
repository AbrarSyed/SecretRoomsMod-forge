package com.wynprice.secretroomsmod.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySecretChest extends TileEntityInfomationHolder
{
	private ItemStackHandler handler = new ItemStackHandler(27);
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		handler.deserializeNBT(getTileData().getCompoundTag("handler"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		getTileData().setTag("handler", handler.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	public ItemStackHandler getHandler() {
		return handler;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if (capability  == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) this.handler;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if (capability  == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}
}
