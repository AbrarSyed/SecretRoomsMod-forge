package com.github.AbrarSyed.secretroomsmod.blocks;

public class TileEntityCamoDetector extends TileEntityCamo
{
	@Override
	public void updateEntity()
	{
		if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L)
		{
			this.blockType = this.getBlockType();

			if (this.blockType != null && this.blockType instanceof BlockCamoLightDetector)
			{
				((BlockCamoLightDetector) this.blockType).updateLightLevel(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
		}
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}
}
