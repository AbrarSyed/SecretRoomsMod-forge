package com.wynprice.secretroomsmod.render;

import java.util.ArrayList;
import java.util.Arrays;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketEnergizedPaste;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifineHelper;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

public class FakeChunkCache extends ChunkCache
{
	
	private final ChunkCache oldCache;
	
	private final BlockPos fromPos;
	private final BlockPos toPos;
	private final int sub;
	
	public FakeChunkCache(World world, BlockPos from, BlockPos to, int sub, ChunkCache oldCache)
	{
		super(world, from, to, sub);
		this.oldCache = oldCache;
		this.fromPos = from;
		this.toPos = to;
		this.sub = sub;
	}
	
	@Override
	public boolean isEmpty() 
	{
		return oldCache.isEmpty();
	}
	
	@Override
	public Biome getBiome(BlockPos pos) 
	{
		return oldCache.getBiome(pos);
	}
			
	public int prevInt = -1;
	
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(SecretOptifineHelper.IS_OPTIFINE)
		{
			if(Arrays.asList("C7", "C8").contains(SecretOptifineHelper.version) && prevInt != -1)
			{
				for(Object[] list : new ArrayList<>(SecretOptifineHelper.CURRENT_C7_LIST))
					if(list != null && prevInt >= 0 && prevInt < list.length)
						list[prevInt] = null;
				prevInt = -1;
			}
			if(super.getBlockState(pos).getBlock() instanceof ISecretBlock && ISecretTileEntity.getMirrorState(world, pos) != null) 
			{
				prevInt = SecretOptifineHelper.getPositionIndex(fromPos, toPos, sub, pos);
				if(!SecretOptifineHelper.resetCached())
					return oldCache.getBlockState(pos);
				if(((ISecretBlock)super.getBlockState(pos).getBlock()).phaseModel(new FakeBlockModel(Blocks.STONE.getDefaultState())).getClass() != FakeBlockModel.class &&
						(Thread.currentThread().getStackTrace()[3].getClassName().equals(RenderChunk.class.getName())) || 
						Arrays.asList("func_187491_a", "func_175626_b").contains(Thread.currentThread().getStackTrace()[3].getMethodName()) || 
						Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet) {
					return oldCache.getBlockState(pos);
				}
				return ISecretTileEntity.getMirrorState(world, pos);
			}
		}
		if(!(super.getBlockState(pos).getBlock() instanceof ISecretBlock) && EnergizedPasteHandler.hasReplacedState(world, pos))
		{
			if(EnergizedPasteHandler.getSetBlockState(world, pos).getBlock() != super.getBlockState(pos).getBlock()
					|| EnergizedPasteHandler.getSetBlockState(world, pos).getBlock().getMetaFromState(EnergizedPasteHandler.getSetBlockState(world, pos)) != super.getBlockState(pos).getBlock().getMetaFromState(super.getBlockState(pos)))
			{
				EnergizedPasteHandler.removeReplacedState(world.provider.getDimension(), pos);
				SecretNetwork.sendToServer(new MessagePacketEnergizedPaste(pos, false));
			}
			else
				return EnergizedPasteHandler.getReplacedState(world, pos);
		}

		return oldCache.getBlockState(pos);
	}	
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) 
	{
		return oldCache.getCombinedLight(pos, lightValue);
	}
	
	@Override
	public int getLightFor(EnumSkyBlock type, BlockPos pos) 
	{
		return oldCache.getLightFor(type, pos);
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) 
	{
		return oldCache.getStrongPower(pos, direction);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos) 
	{
		return oldCache.getTileEntity(pos);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_190300_2_)
	{
		return oldCache.getTileEntity(pos, p_190300_2_);
	}
	
	@Override
	public WorldType getWorldType() 
	{
		return oldCache.getWorldType();
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos) 
	{
		return oldCache.isAirBlock(pos);
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) 
	{
		return oldCache.isSideSolid(pos, side, _default);
	}
}