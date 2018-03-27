package com.wynprice.secretroomsmod.integration.malisisdoors.registries.tileentities;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity.TileEntityData;
import com.wynprice.secretroomsmod.handler.ParticleHandler;

import net.malisis.core.util.syncer.Syncable;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

@Syncable("TileEntity")
public class SecretMalisisTileEntityDoor extends DoorTileEntity implements ISecretTileEntity
{
	/**
	 * The mirrored state
	 */
	protected IBlockState mirrorState;
		
	
	/**
	 * If the TileEntity is locked then {@link #setMirrorState(IBlockState)} will not work
	 */
	private boolean locked;
	
	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		readFromNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		TileEntityData data = ISecretTileEntity.super.readDataFromNBT(compound, getTileData());
		mirrorState = data.getMirroredState();
		locked = data.isLocked();
	}
	
	@Override
	public void update() {
		super.update();
		if(mirrorState != null)
			ParticleHandler.BLOCKBRAKERENDERMAP.put(pos, mirrorState.getBlock().getStateFromMeta(mirrorState.getBlock().getMetaFromState(mirrorState)));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		ISecretTileEntity.super.writeDataToNBT(compound, new TileEntityData().setMirroredState(getMirrorStateSafely()).setLocked(locked));
		return super.writeToNBT(compound);
	} 
	
	@Override
	public IBlockState getMirrorState() {
		if(mirrorState == null && ParticleHandler.BLOCKBRAKERENDERMAP.containsKey(pos))
			mirrorState = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos);
		if(mirrorState == null && RENDER_MAP.containsKey(pos))
			mirrorState = ISecretTileEntity.getMap(world).get(pos);
		return mirrorState;
	}
	
	@Override
	public double getMaxRenderDistanceSquared() 
	{
		return Double.MAX_VALUE;
	}
	
	public void setMirrorState(IBlockState mirrorState)
	{
		if(!locked)
			setMirrorStateForcable(mirrorState);
		locked = true;
	}
	
	@Override
	public void setMirrorStateForcable(IBlockState mirrorState)
	{
		if(mirrorState.getBlock() instanceof ISecretBlock)
			mirrorState = Blocks.STONE.getDefaultState();
		ISecretTileEntity.getMap(world).put(this.pos, mirrorState);
		this.mirrorState = mirrorState.getBlock().getStateFromMeta(mirrorState.getBlock().getMetaFromState(mirrorState));
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		int metadata = getBlockMetadata();
		return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
}
