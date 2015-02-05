package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoGhost extends BlockCamoFull
{

	public BlockCamoGhost()
	{
		super(Material.wood);
		setHardness(1.5F);
		setStepSound(Block.soundTypeWood);
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
	 * cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		int metadata = par1World.getBlockMetadata(par2, par3, par4);

		if (metadata == 0)
			return null;
		else
			return AxisAlignedBB.getBoundingBox(par2 + minX, par3 + minY, par4 + minZ, par2 + maxX, par3 + maxY, par4 + maxZ);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block changed)
	{
		if (changed != null && changed.canProvidePower())
		{
			world.scheduleBlockUpdate(x, y, z, this, 0);
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		boolean flag = !world.isRemote && (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k));

		if (flag)
		{
			world.setBlockMetadataWithNotify(i, j, k, 1, 4);
		}
		else
		{
			world.setBlockMetadataWithNotify(i, j, k, 0, 4);
		}
	}
}
