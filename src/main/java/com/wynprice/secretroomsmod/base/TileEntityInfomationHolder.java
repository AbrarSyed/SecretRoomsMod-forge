package com.wynprice.secretroomsmod.base;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.handler.ParticleHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TileEntityInfomationHolder extends TileEntity implements ITickable
{
	protected IBlockState mirrorState;
		
	private boolean locked;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		locked = getTileData().getBoolean("locked");
		Block testBlock = Block.REGISTRY.getObject(new ResourceLocation(getTileData().getString("MirrorBlock")));
		if(testBlock != Blocks.AIR)
			mirrorState = testBlock.getStateFromMeta(getTileData().getInteger("MirrorMeta"));
		if(mirrorState != null && mirrorState.getBlock() instanceof ISecretBlock)
			mirrorState = null;
	}
	
	@Override
	public void update() {
		if(mirrorState != null)
			ParticleHandler.BLOCKBRAKERENDERMAP.put(pos, mirrorState.getBlock().getStateFromMeta(mirrorState.getBlock().getMetaFromState(mirrorState)));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(mirrorState != null)
		{
			getTileData().setString("MirrorBlock", mirrorState.getBlock().getRegistryName().toString());
			getTileData().setInteger("MirrorMeta", mirrorState.getBlock().getMetaFromState(mirrorState));
		}
		getTileData().setBoolean("locked", locked);
		return super.writeToNBT(compound);
	}
	
	public IBlockState getMirrorState() {
		if(mirrorState == null && ParticleHandler.BLOCKBRAKERENDERMAP.containsKey(pos))
			mirrorState = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos);
		return mirrorState;
	}
	
	public void setMirrorState(IBlockState mirrorState, @Nullable BlockPos pos)
	{
		if(!locked)
			setMirrorStateForcable(mirrorState, pos);
		locked = true;
	}
	
	public void setMirrorStateForcable(IBlockState mirrorState, @Nullable BlockPos pos)
	{
		if(mirrorState.getBlock() instanceof ISecretBlock && ((ISecretBlock)mirrorState.getBlock()).getState(world, pos) != null)
			mirrorState = ((ISecretBlock)mirrorState.getBlock()).getState(world, pos);
		this.mirrorState = mirrorState.getBlock().getStateFromMeta(mirrorState.getBlock().getMetaFromState(mirrorState));
	}
	
}
