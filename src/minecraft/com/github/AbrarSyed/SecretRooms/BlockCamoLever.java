package com.github.AbrarSyed.SecretRooms;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.ArrayList;

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
public class BlockCamoLever extends BlockCamoFull
{
	protected BlockCamoLever(int i)
	{
		super(i, Material.circuits);
		setHardness(1.5F);
		setStepSound(Block.soundWoodFootstep);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j)
	{
		if (i == 1)
			return Block.lever.blockIndexInTexture;
		else if (i == 3)
			return Block.planks.blockIndexInTexture;
		else
			return blockIndexInTexture;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(l);
		return dir == DOWN && world.isBlockSolidOnSide(i, j + 1, k, DOWN) || dir == UP && world.isBlockSolidOnSide(i, j - 1, k, UP) || dir == NORTH && world.isBlockSolidOnSide(i, j, k + 1, NORTH) || dir == SOUTH && world.isBlockSolidOnSide(i, j, k - 1, SOUTH) || dir == WEST && world.isBlockSolidOnSide(i + 1, j, k, WEST) || dir == EAST && world.isBlockSolidOnSide(i - 1, j, k, EAST);
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k)
	{
		return world.isBlockSolidOnSide(i - 1, j, k, EAST) || world.isBlockSolidOnSide(i + 1, j, k, WEST) || world.isBlockSolidOnSide(i, j, k - 1, SOUTH) || world.isBlockSolidOnSide(i, j, k + 1, NORTH) || world.isBlockSolidOnSide(i, j - 1, k, UP) || world.isBlockSolidOnSide(i, j + 1, k, DOWN);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
	{
		if (world.isRemote)
			return true;
		else
		{
			int meta = world.getBlockMetadata(i, j, k);
			int sideMeta = meta & 7;
			int powerMeta = 8 - (meta & 8);
			world.setBlockMetadataWithNotify(i, j, k, sideMeta + powerMeta);
			world.markBlockForUpdate(i, j, k);
			world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, powerMeta > 0 ? 0.6F : 0.5F);
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);

			if (sideMeta == 1)
				world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
			else if (sideMeta == 2)
				world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
			else if (sideMeta == 3)
				world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
			else if (sideMeta == 4)
				world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
			else if (sideMeta != 5 && sideMeta != 6)
			{
				if (sideMeta == 0 || sideMeta == 7)
					world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
			}
			else
				world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);

			return true;
		}
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{

		if ((metadata & 8) > 0)
		{
			world.notifyBlocksOfNeighborChange(i, j, k, blockID);
			int i1 = metadata & 7;

			if (i1 == 1)
				world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
			else if (i1 == 2)
				world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
			else if (i1 == 3)
				world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
			else if (i1 == 4)
				world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
			else
				world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		}

		super.breakBlock(world, i, j, k, something, metadata);
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side
	 */
	@Override
	public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) > 0;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side
	 */
	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

		if ((var6 & 8) == 0)
			return false;
		else
		{
			int var7 = var6 & 7;
			return var7 == 0 && par5 == 0 ? true : var7 == 7 && par5 == 0 ? true : var7 == 6 && par5 == 1 ? true : var7 == 5 && par5 == 1 ? true : var7 == 4 && par5 == 2 ? true : var7 == 3 && par5 == 3 ? true : var7 == 2 && par5 == 4 ? true : var7 == 1 && par5 == 5;
		}
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
}
