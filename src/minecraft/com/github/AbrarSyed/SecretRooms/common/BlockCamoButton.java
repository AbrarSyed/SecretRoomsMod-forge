package com.github.AbrarSyed.SecretRooms.common;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author AbrarSyed
 */
public class BlockCamoButton extends BlockCamoFull
{

	public BlockCamoButton(int i)
	{
		super(i, Material.circuits);
		setCreativeTab(SecretRooms.tab);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	@Override
	public int tickRate(World world)
	{
		return 20;
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int j)
	{
		if (i == 3 || i == 1)
			return Block.cobblestone.getBlockTextureFromSide(i);
		else
			return blockIcon;
	}

	/**
	 * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
	 */
	@Override
	public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(side);
		return dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH) ||
				dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH) ||
				dir == WEST && world.isBlockSolidOnSide(x + 1, y, z, WEST) ||
				dir == EAST && world.isBlockSolidOnSide(x - 1, y, z, EAST);
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return world.isBlockSolidOnSide(x - 1, y, z, EAST) ||
				world.isBlockSolidOnSide(x + 1, y, z, WEST) ||
				world.isBlockSolidOnSide(x, y, z - 1, SOUTH) ||
				world.isBlockSolidOnSide(x, y, z + 1, NORTH);
	}

	/**
	 * Get side which this button is facing.
	 */
	private int getOrientation(World world, int x, int y, int z)
	{
		if (world.isBlockSolidOnSide(x - 1, y, z, EAST))
			return 1;
		if (world.isBlockSolidOnSide(x + 1, y, z, WEST))
			return 2;
		if (world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
			return 3;
		if (world.isBlockSolidOnSide(x, y, z + 1, NORTH))
			return 4;
		return 1;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ, int currentMeta)
	{
		int meta = world.getBlockMetadata(x, y, z);
		int isActive = meta & 8;
		meta &= 7;

		ForgeDirection dir = ForgeDirection.getOrientation(side);

		if (dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH))
		{
			meta = 4;
		}
		else if (dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
		{
			meta = 3;
		}
		else if (dir == WEST && world.isBlockSolidOnSide(x + 1, y, z, WEST))
		{
			meta = 2;
		}
		else if (dir == EAST && world.isBlockSolidOnSide(x - 1, y, z, EAST))
		{
			meta = 1;
		}
		else
		{
			meta = getOrientation(world, x, y, z);
		}

		return meta + isActive;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if (checkIfAttachedToBlock(world, i, j, k))
		{
			int i1 = world.getBlockMetadata(i, j, k) & 7;
			boolean flag = false;

			if (!world.isBlockNormalCube(i - 1, j, k) && i1 == 1)
			{
				flag = true;
			}

			if (!world.isBlockNormalCube(i + 1, j, k) && i1 == 2)
			{
				flag = true;
			}

			if (!world.isBlockNormalCube(i, j, k - 1) && i1 == 3)
			{
				flag = true;
			}

			if (!world.isBlockNormalCube(i, j, k + 1) && i1 == 4)
			{
				flag = true;
			}

			if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 5)
			{
				flag = true;
			}

			if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 6)
			{
				flag = true;
			}

			if (flag)
			{
				dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
				world.setBlockToAir(i, j, k);
			}
		}
	}

	private boolean checkIfAttachedToBlock(World world, int i, int j, int k)
	{
		if (!canPlaceBlockAt(world, i, j, k))
		{
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockToAir(i, j, k);
			return false;
		}
		else
			return true;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something, float something1, float soemthin2, float soemthing3)
	{
		int meta = world.getBlockMetadata(i, j, k);
		int dir = meta & 7;
		int isActive = 8 - (meta & 8);

		if (isActive == 0)
			return true;

		world.setBlockMetadataWithNotify(i, j, k, dir + isActive, 3);
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
		world.markBlockForUpdate(i, j, k);
		updateArround(world, i, j, k, ForgeDirection.getOrientation(dir));
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
		return true;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int meta = world.getBlockMetadata(i, j, k);
		int dir = meta & 7;
		int isActive = 8 - (meta & 8);

		if (isActive > 0)
			return;

		world.setBlockMetadataWithNotify(i, j, k, meta & 7, 3);
		updateArround(world, i, j, k, ForgeDirection.getOrientation(dir));
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{
		if ((metadata & 8) > 0)
		{
			updateArround(world, i, j, k, ForgeDirection.getOrientation(metadata & 7));
		}
		super.breakBlock(world, i, j, k, something, metadata);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return (world.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		int i1 = world.getBlockMetadata(x, y, z);

		if ((i1 & 8) == 0)
			return 0;
		else
		{
			int j1 = i1 & 7;
			if (j1 == 5 && side == 1 ||
					j1 == 4 && side == 2 ||
					j1 == 3 && side == 3 ||
					j1 == 2 && side == 4 ||
					j1 == 1 && side == 5)
				return 15;
			else
				return 0;
		}
	}

	private void updateArround(World world, int x, int y, int z, ForgeDirection side)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x + side.offsetX, y + side.offsetY, z + side.offsetZ, blockID);
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
}
