package com.wynprice.secretroomsmod.tileentity;

import com.wynprice.secretroomsmod.base.BaseFakePressurePlate;

public class TileEntitySecretPressurePlate extends TileEntityInfomationHolder
{
	@Override
	public void update() 
	{
		((BaseFakePressurePlate)world.getBlockState(pos).getBlock()).calculateState(world, pos);
		super.update();
	}
}
