package com.wynprice.secretroomsmod.base.interfaces;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public interface ISecretTileEntity extends ITickable
{
	public void setMirrorState(IBlockState mirrorState, @Nullable BlockPos pos);
	
	public void setMirrorStateForcable(IBlockState mirrorState, @Nullable BlockPos pos);
	
	public IBlockState getMirrorState();

	public void readFromNBT(NBTTagCompound compound);
}
