package com.wynprice.secretroomsmod.tileentity;

import net.minecraft.block.BlockDaylightDetector;

public class TileEntitySecretDaylightSensor extends TileEntityInfomationHolder
{
	public void update()
    {
		super.update();
        if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType instanceof BlockDaylightDetector)
            {
                ((BlockDaylightDetector)this.blockType).updatePower(this.worldObj, this.pos);
            }
        }
    }
}
