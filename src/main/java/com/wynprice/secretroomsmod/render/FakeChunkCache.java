package com.wynprice.secretroomsmod.render;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.proxy.ClientProxy;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.Block;
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
	
	public FakeChunkCache(World world, BlockPos from, BlockPos to, int sub, ChunkCache oldCache)
	{
		super(world, from, to, sub);
		this.oldCache = oldCache;
	}
	
	@Override
	public boolean isEmpty() 
	{
		return oldCache.isEmpty();
	}
	
	@Override
	public Biome getBiome(BlockPos pos) {
		return oldCache.getBiome(pos);
	}
			
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(super.getBlockState(pos).getBlock() instanceof ISecretBlock && ISecretTileEntity.getMirrorState(world, pos) != null) 
		{
			try
			{
				Field field = Class.forName("ChunkCacheOF").getDeclaredField("cacheBlockStates");
				field.setAccessible(true);
				if(field.get(null).getClass() != ClientProxy.secretOptifine.getClass())
					try {
						field.set(null, ClientProxy.secretOptifine.getClass().newInstance());
					}
					catch (IllegalAccessException e) {
						SecretRooms5.LOGGER.error("It seems like you're using an unsupported version of Optifine. Please use C6");
					}

				field.setAccessible(false);
				if(!(Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet) && 
						((ISecretBlock)super.getBlockState(pos).getBlock()).phaseModel(new FakeBlockModel(Blocks.STONE.getDefaultState())).getClass() != FakeBlockModel.class &&
						(Thread.currentThread().getStackTrace()[3].getClassName().equals(RenderChunk.class.getName())) || 
						Arrays.asList("func_187491_a", "func_175626_b").contains(Thread.currentThread().getStackTrace()[3].getMethodName())) {
					return oldCache.getBlockState(pos);
				}
			}
			catch (Throwable t) {
				if(Thread.currentThread().getStackTrace()[2].getClassName().equals(RenderChunk.class.getName()))
					return oldCache.getBlockState(pos);
			}
			return ISecretTileEntity.getMirrorState(world, pos);
		}
		return oldCache.getBlockState(pos);
	}	
	
	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		return oldCache.getCombinedLight(pos, lightValue);
	}
	
	@Override
	public int getLightFor(EnumSkyBlock type, BlockPos pos) {
		return oldCache.getLightFor(type, pos);
	}
	
	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return oldCache.getStrongPower(pos, direction);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return oldCache.getTileEntity(pos);
	}
	
	@Override
	public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_190300_2_) {
		return oldCache.getTileEntity(pos, p_190300_2_);
	}
	
	@Override
	public WorldType getWorldType() {
		return oldCache.getWorldType();
	}
	
	@Override
	public boolean isAirBlock(BlockPos pos) {
		return oldCache.isAirBlock(pos);
	}
	
	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return oldCache.isSideSolid(pos, side, _default);
	}
}