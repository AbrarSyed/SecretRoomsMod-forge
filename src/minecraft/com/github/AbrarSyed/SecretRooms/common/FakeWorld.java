package com.github.AbrarSyed.SecretRooms.common;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeWorld extends World implements IBlockAccess
{
	private final World									world;
	private final HashMap<ChunkPosition, BlockHolder>	overrideMap;

	private FakeWorld(World world, WorldSettings settings)
	{
		super(world.getSaveHandler(), world.getWorldInfo().getWorldName(), world.provider, settings, world.theProfiler);
		this.world = world;
		overrideMap = new HashMap<ChunkPosition, BlockHolder>();
		worldInfo = world.getWorldInfo();
	}

	public static FakeWorld getFakeWorldFor(World world)
	{
		WorldSettings settings = new WorldSettings(world.getWorldInfo());
		return new FakeWorld(world, settings);
	}

	public void addOverrideBlock(int x, int y, int z, BlockHolder holder)
	{
		ChunkPosition position = new ChunkPosition(x, y, z);
		overrideMap.put(position, holder);
	}

	public void removeOverrideBlock(int x, int y, int z)
	{
		ChunkPosition position = new ChunkPosition(x, y, z);
		overrideMap.remove(position);
	}

	@Override
	public int getBlockId(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).blockID;
		else
			return world.getBlockId(x, y, z);
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).getTileEntity(this);
		else
			return world.getBlockTileEntity(x, y, z);
	}

	@Override
	public int getBlockMetadata(int x, int y, int z)
	{
		ChunkPosition pos = new ChunkPosition(x, y, z);
		if (overrideMap.containsKey(pos))
			return overrideMap.get(pos).metadata;
		else
			return world.getBlockMetadata(x, y, z);
	}

	@Override
	public Material getBlockMaterial(int x, int y, int z)
	{
		return world.getBlockMaterial(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int lightValue)
	{
		return world.getLightBrightnessForSkyBlocks(x, y, z, lightValue);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getBrightness(int x, int y, int z, int lightValue)
	{
		return world.getBrightness(x, y, z, lightValue);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getLightBrightness(int x, int y, int z)
	{
		return world.getLightBrightness(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isBlockOpaqueCube(int x, int y, int z)
	{
		return world.isBlockOpaqueCube(x, y, z);
	}

	@Override
	public boolean isBlockNormalCube(int x, int y, int z)
	{
		return world.isBlockNormalCube(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isAirBlock(int x, int y, int z)
	{
		return world.isAirBlock(x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BiomeGenBase getBiomeGenForCoords(int x, int z)
	{
		return world.getBiomeGenForCoords(x, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getHeight()
	{
		return world.getHeight();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean extendedLevelsInChunkCache()
	{
		return world.extendedLevelsInChunkCache();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesBlockHaveSolidTopSurface(int x, int y, int z)
	{
		return world.doesBlockHaveSolidTopSurface(x, y, z);
	}

	@Override
	public Vec3Pool getWorldVec3Pool()
	{
		return world.getWorldVec3Pool();
	}

	@Override
	public boolean isBlockProvidingPowerTo(int x, int y, int z, int side)
	{
		return world.isBlockProvidingPowerTo(x, y, z, side);
	}

	// WORLD overrides

	@Override
	public WorldChunkManager getWorldChunkManager()
	{
		return world.getWorldChunkManager();
	}

	@Override
	public void setSpawnLocation()
	{
		world.setSpawnLocation();
	}

	@Override
	public int getFirstUncoveredBlock(int x, int z)
	{
		return world.getFirstUncoveredBlock(x, z);
	}

	@Override
	public int blockGetRenderType(int par1, int par2, int par3)
	{
		int var4 = getBlockId(par1, par2, par3);
		return Block.blocksList[var4] != null ? Block.blocksList[var4].getRenderType() : -1;
	}

	/**
	 * Returns whether a block exists at world coordinates x, y, z
	 */
	@Override
	public boolean blockExists(int x, int y, int z)
	{
		return world.blockExists(x, y, z);
	}

	/**
	 * Checks if any of the chunks within distance (argument 4) blocks of the given block exist
	 */
	@Override
	public boolean doChunksNearChunkExist(int x, int y, int z, int dist)
	{
		return world.doChunksNearChunkExist(x, y, z, dist);
	}

	/**
	 * Checks between a min and max all the chunks inbetween actually exist. Args: minX, minY, minZ, maxX, maxY, maxZ
	 */
	@Override
	public boolean checkChunksExist(int x, int y, int z, int x2, int y2, int z2)
	{
		return world.checkChunksExist(x, y, z, x2, y2, z2);
	}

	/**
	 * Returns a chunk looked up by block coordinates. Args: x, z
	 */
	@Override
	public Chunk getChunkFromBlockCoords(int par1, int par2)
	{
		return world.getChunkFromBlockCoords(par1, par2);
	}

	/**
	 * Returns back a chunk looked up by chunk coordinates Args: x, y
	 */
	@Override
	public Chunk getChunkFromChunkCoords(int par1, int par2)
	{
		return world.getChunkFromChunkCoords(par1, par2);
	}

	/**
	 * Sets the block ID and metadata of a block in global coordinates
	 */
	@Override
	public boolean setBlockAndMetadata(int par1, int par2, int par3, int par4, int par5)
	{
		return world.setBlock(par1, par2, par3, par5);
	}

	@Override
	protected IChunkProvider createChunkProvider()
	{
		return world.getChunkProvider();
	}

	@Override
	public Entity getEntityByID(int var1)
	{
		return world.getEntityByID(var1);
	}

	/**
	 * Sets the block ID and metadata of a block, optionally marking it as needing update. Args: X,Y,Z, blockID,
	 * metadata, needsUpdate
	 */
	@Override
	public boolean setBlockAndMetadataWithUpdate(int par1, int par2, int par3, int par4, int par5, boolean par6)
	{
		return world.setBlockAndMetadataWithUpdate(par1, par2, par3, par4, par5, par6);
	}

	/**
	 * Sets the block to the specified blockID at the block coordinates Args x, y, z, blockID
	 */
	@Override
	public boolean setBlock(int par1, int par2, int par3, int par4)
	{
		return world.setBlock(par1, par2, par3, par4);
	}

}
