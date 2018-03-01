package com.wynprice.secretroomsmod.tileentity;

import net.minecraft.block.BlockDaylightDetector;

/**
 * The TileEntity for Secret daylight sensors
 * @author Wyn Price
 *
 */
public class TileEntitySecretDaylightSensor extends TileEntityInfomationHolder
{
	public void update()
    {
		super.update();
        if (this.world != null && !this.world.isRemote && this.world.getTotalWorldTime() % 20L == 0L)
        {
            this.blockType = this.getBlockType();

            if (this.blockType instanceof BlockDaylightDetector)
            {
                ((BlockDaylightDetector)this.blockType).updatePower(this.world, this.pos);
            }
        }
    }
}
