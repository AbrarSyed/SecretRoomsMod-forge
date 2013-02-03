package com.github.AbrarSyed.SecretRooms.common;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
	public int tickRate()
	{
		return 20;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		if (i == 3 || i == 1)
			return Block.cobblestone.blockIndexInTexture;
		else
			return blockIndexInTexture;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
	{
		if (l == 1 && world.isBlockNormalCube(i, j - 1, k))
			return true;

		if (l == 2 && world.isBlockNormalCube(i, j, k + 1))
			return true;

		if (l == 3 && world.isBlockNormalCube(i, j, k - 1))
			return true;

		if (l == 4 && world.isBlockNormalCube(i + 1, j, k))
			return true;

		return l == 5 && world.isBlockNormalCube(i - 1, j, k);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		if (world.isBlockNormalCube(i - 1, j, k))
			return true;

		if (world.isBlockNormalCube(i + 1, j, k))
			return true;

		if (world.isBlockNormalCube(i, j, k - 1))
			return true;

		if (world.isBlockNormalCube(i, j, k + 1))
			return true;

		return world.isBlockNormalCube(i, j - 1, k);
	}

	/**
	 * Get side which this button is facing.
	 */
	private int getOrientation(World par1World, int par2, int par3, int par4)
	{
		if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
			return 1;
		if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
			return 2;
		if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
			return 3;
		if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
			return 4;
		return 1;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ, int currentMeta)
	{
		int var11 = currentMeta & 8;
		int direction = currentMeta & 7;
		direction = -1;

		if (side == 0 && world.isBlockSolidOnSide(x, y + 1, z, DOWN))
			direction = world.rand.nextBoolean() ? 0 : 7;

		if (side == 1 && world.isBlockSolidOnSide(x, y - 1, z, UP))
			direction = 5 + world.rand.nextInt(2);

		if (side == 2 && world.isBlockSolidOnSide(x, y, z + 1, NORTH))
			direction = 4;

		if (side == 3 && world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
			direction = 3;

		if (side == 4 && world.isBlockSolidOnSide(x + 1, y, z, WEST))
			direction = 2;

		if (side == 5 && world.isBlockSolidOnSide(x - 1, y, z, EAST))
			direction = 1;

		return direction + var11;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if (checkIfAttachedToBlock(world, i, j, k))
		{
			int i1 = world.getBlockMetadata(i, j, k) & 7;
			boolean flag = false;

			if (!world.isBlockNormalCube(i - 1, j, k) && i1 == 1)
				flag = true;

			if (!world.isBlockNormalCube(i + 1, j, k) && i1 == 2)
				flag = true;

			if (!world.isBlockNormalCube(i, j, k - 1) && i1 == 3)
				flag = true;

			if (!world.isBlockNormalCube(i, j, k + 1) && i1 == 4)
				flag = true;

			if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 5)
				flag = true;

			if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 6)
				flag = true;

			if (flag)
			{
				dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
				world.setBlockWithNotify(i, j, k, 0);
			}
		}
	}

	private boolean checkIfAttachedToBlock(World world, int i, int j, int k)
	{
		if (!canPlaceBlockAt(world, i, j, k))
		{
			dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
			world.setBlockWithNotify(i, j, k, 0);
			return false;
		}
		else
			return true;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something, float something1, float soemthin2, float soemthing3)
	{
		if (world.isRemote)
		{
			world.markBlockForUpdate(i, j, k);
			return true;
		}

		int meta = world.getBlockMetadata(i, j, k);
		int sideMeta = meta & 7;
		int poweredMeta = 8 - (meta & 8);
		world.setBlockMetadataWithNotify(i, j, k, sideMeta + poweredMeta);
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, poweredMeta <= 0 ? 0.5F : 0.6F);
		world.markBlockForUpdate(i, j, k);
		updateArround(world, i, j, k, ForgeDirection.getOrientation(sideMeta));
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
		return true;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{

		if (world.isRemote)
			return;

		int meta = world.getBlockMetadata(i, j, k);

		if ((meta & 8) == 0)
			return;

		int sideMeta = meta & 7;
		int powerredMeta = 8 - (meta & 8);
		world.setBlockMetadataWithNotify(i, j, k, sideMeta + powerredMeta);
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
		updateArround(world, i, j, k, ForgeDirection.getOrientation(sideMeta));
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{
		if ((metadata & 8) > 0)
			updateArround(world, i, j, k, ForgeDirection.getOrientation(metadata & 7));
		super.breakBlock(world, i, j, k, something, metadata);
	}

	@Override
	public boolean isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		return (iblockaccess.getBlockMetadata(i, j, k) & 8) > 0;
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		int var6 = world.getBlockMetadata(x, y, z);

		if ((var6 & 8) == 0)
			return false;
		else
		{
			int var7 = var6 & 7;
			return var7 == 5 && side == 1 ? true : var7 == 4 && side == 2 ? true : var7 == 3 && side == 3 ? true : var7 == 2 && side == 4 ? true : var7 == 1 && side == 5;
		}
	}

	private void updateArround(World world, int x, int y, int z, ForgeDirection side)
	{
		world.notifyBlocksOfNeighborChange(x + side.offsetX, y + side.offsetY, z + side.offsetZ, blockID);
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
}