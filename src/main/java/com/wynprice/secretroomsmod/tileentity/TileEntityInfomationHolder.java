package com.wynprice.secretroomsmod.tileentity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity.TileEntityData;
import com.wynprice.secretroomsmod.handler.ParticleHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

/**
 * Basic TileEntity for SRM blocks. Note that all SRM blocks will not extend this. They will however impiment {@link ISecretTileEntity}
 * @author Wyn Price
 *
 */
public class TileEntityInfomationHolder extends TileEntity implements ITickable, ISecretTileEntity
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
		if(mirrorState != null)
			ParticleHandler.BLOCKBRAKERENDERMAP.put(pos, mirrorState.getBlock().getStateFromMeta(mirrorState.getBlock().getMetaFromState(mirrorState)));
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(getPos().add(-1, -1, -1), getPos().add(1, 1, 1));
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
		if(mirrorState.getBlock() == null) {
			SecretRooms5.LOGGER.error("Null BlockState passed in at: {}, {}, {}. This is most likely doue to world corruption. Setting mirrored state to {}", getPos().getX(), getPos().getY(), getPos().getZ(), this.mirrorState == null ? "default (stone)" : this.mirrorState);
			if(this.mirrorState == null) {
				mirrorState = Blocks.STONE.getDefaultState();
			} else {
				return;
			}
		}
		if(mirrorState.getBlock() instanceof ISecretBlock) {
			mirrorState = Blocks.STONE.getDefaultState();
		}
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
