package com.wynprice.secretroomsmod.render.fakemodels;

import java.lang.reflect.Field;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.proxy.ClientProxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.chunk.RenderChunk;
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
	
	//return oldCache.getBlockState(pos) if called by the rendering of its-self
	@Override
	public IBlockState getBlockState(BlockPos pos) 
	{
		if(super.getBlockState(pos).getBlock() instanceof ISecretBlock) 
		{
			new Exception().printStackTrace();
			try
			{
				Field field = Class.forName("ChunkCacheOF").getDeclaredField("cacheBlockStates");
				field.setAccessible(true);
				field.set(null, ClientProxy.secretOptifine.getClass().newInstance());
				field.setAccessible(false);
				if(Thread.currentThread().getStackTrace()[3].getClassName().equals(RenderChunk.class.getName()))
					return oldCache.getBlockState(pos);
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