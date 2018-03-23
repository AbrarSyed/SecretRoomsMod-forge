package com.wynprice.secretroomsmod.tileentity;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.ParticleHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

/**
 * The TileEntity for The SecretDispenser. This is currently (5.3.2) the only instance where it does not extend {@link TileEntityInfomationHolder}
 * @author Wyn Price
 *
 */
public class TileEntitySecretDispenser extends TileEntityDispenser implements ISecretTileEntity
{
	protected IBlockState mirrorState;
	
	private boolean locked;
	
	@Override
	public void loadFromNBT(NBTTagCompound compound) {
		readFromNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		TileEntityData data = ISecretTileEntity.super.readDataFromNBT(compound);
		mirrorState = data.getMirroredState();
		locked = data.isLocked();
	}
	
	@Override
	public double getMaxRenderDistanceSquared() 
	{
		return Double.MAX_VALUE;
	}
	
	@Override
	public void update() {
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
	
	public void setMirrorState(IBlockState mirrorState)
	{
		if(!locked)
			setMirrorStateForcable(mirrorState);
		locked = true;
	}
	
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
