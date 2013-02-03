package com.github.AbrarSyed.SecretRooms.client;

import net.minecraft.block.material.Material;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FakeWorld implements IBlockAccess
{
	WorldClient	world;

	public FakeWorld(WorldClient world)
	{
		this.world = world;
	}

	@Override
	public int getBlockId(int x, int y, int z)
	{
		// TODO check map.
		return 0;
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z)
	{
		// TODO check map.
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int lightValue)
	{
		// TODO check map.
		return 0;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Material getBlockMaterial(int x, int y, int z)
	{
		// TODO Auto-generated method stub
		return null;
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

}
