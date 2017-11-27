package com.wynprice.secretroomsmod.tileentity;

import com.wynprice.secretroomsmod.base.BaseFakePressurePlate;

public class TileEntitySecretPressurePlate extends TileEntityInfomationHolder
{
	@Override
	public void update() 
	{
		((BaseFakePressurePlate)worldObj.getBlockState(pos).getBlock()).calculateState(worldObj, pos);
		super.update();
	}
}
