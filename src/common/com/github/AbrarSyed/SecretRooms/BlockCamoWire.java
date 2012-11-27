package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author AbrarSyed
 */
public class BlockCamoWire extends BlockCamoFull
{
	public BlockCamoWire(int i)
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
		if (i == 3)
			return Block.redstoneWire.blockIndexInTexture;
		else
			return blockIndexInTexture;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if (l == 0)
			return;

		if (Block.blocksList[l].canProvidePower() || l == blockID)
		{
			// System.out.println(i+" "+j+" "+k+"  notified");
			boolean isPoweredOld = getPoweredState(world, i, j, k) > 0;
			boolean isPowered = world.isBlockGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j, k);

			// old and new different?
			if (isPowered != isPoweredOld)
			{
				// if new is powerred.. turn on.
				if (isPowered)
					turnOn(world, i, j, k);
				// else turnoff
				else
					turnOff(world, i, j, k);
			}
			else
			{
				// old and new are the same.

				// if its off, leave it off.
				if (!isPowered)
					return;

				// otherwise.. lets see if we have a new power-er.
				if (!isPoweredFromAllowedDir(world, i, j, k))
					turnOff(world, i, j, k);
			}
		}
	}

	@Override
	public boolean isProvidingWeakPower(IBlockAccess world, int i, int j, int k, int l)
	{
		if (getPoweredState(world, i, j, k) == 0)
			return false;
		else
			return isProvidingStrongPower(world, i, j, k, l);
	}

	@Override
	public boolean isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		if (iblockaccess.getBlockMetadata(i, j, k) == 0)
			return false;

		boolean worked = (iblockaccess.getBlockMetadata(i, j, k) & 7) != getOppositeSide(l);
		// System.out.println("check power to "+l+"  is "+worked);
		return worked;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return Block.blocksList[blockID].canProvidePower() && side != -1;
	}

	private void turnOff(World world, int i, int j, int k)
	{
		world.setBlockMetadataWithNotify(i, j, k, 0);
		notifyWireNeighborsOfNeighborChange(world, i, j, k);
		world.markBlockForUpdate(i, j, k);
		// System.out.println("Turned off");
	}

	private void turnOn(World world, int i, int j, int k)
	{
		boolean[] flags = new boolean[6];

		for (int x = 0; x < 6; x++)
			flags[x] = isBlockPoweredBy(world, i, j, k, x);

		byte poweredSide = -1;

		for (byte x = 0; x < 6; x++)
			if (flags[x])
				poweredSide = x;

		if (poweredSide >= 0)
		{
			world.setBlockMetadataWithNotify(i, j, k, 8 + poweredSide);
			notifyWireNeighborsOfNeighborChange(world, i, j, k);
			world.markBlockForUpdate(i, j, k);
			// System.out.println(i+" "+j+" "+k+" Turned on: direction is "+poweredSide);
		}
	}

	private boolean isPoweredFromAllowedDir(World world, int i, int j, int k)
	{
		byte dir = (byte) getPoweredDirectionFromMetadata(world, i, j, k);
		boolean[] flags = new boolean[6];

		for (int x = 0; x < 6; x++)
			flags[x] = isBlockPoweredBy(world, i, j, k, x);

		if (flags[dir])
			// System.out.println("It is bieng powered by "+dir);
			return true;

		// System.out.println("It is NOT bieng powered by "+dir);
		return false;
	}

	private boolean isBlockPoweredBy(World world, int i, int j, int k, int l)
	{
		int newX, newY, newZ;

		ForgeDirection dir = ForgeDirection.getOrientation(l);
		int opposite = dir.getOpposite().ordinal();
		newX = i + dir.offsetX;
		newY = j + dir.offsetY;
		newZ = k + dir.offsetZ;

		int id = world.getBlockId(newX, newY, newZ);

		if (id == 0)
			return false;

		if (id == Block.redstoneWire.blockID && world.getBlockMetadata(newX, newY, newZ) > 0)
			return true;
		else if (id == blockID && opposite != (world.getBlockMetadata(newX, newY, newZ) & 7) && (world.getBlockMetadata(newX, newY, newZ) & 8) > 0)
			// System.out.println("Checked as Block");
			return true;

		Block block = Block.blocksList[id];

		if (block.canProvidePower() && (block.isProvidingStrongPower(world, newX, newY, newZ, opposite) || block.isProvidingWeakPower(world, newX, newY, newZ, opposite)))
			return true;

		return false;
	}

	private void notifyWireNeighborsOfNeighborChange(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		return;
	}

	private int getPoweredDirectionFromMetadata(World world, int i, int j, int k)
	{
		return world.getBlockMetadata(i, j, k) & 7;
	}

	private int getPoweredState(IBlockAccess world, int i, int j, int k)
	{
		return world.getBlockMetadata(i, j, k) & 8;
	}

	private int getOppositeSide(int i)
	{
		switch (i)
			{
				case 0:
					return 1;

				case 1:
					return 0;

				case 2:
					return 3;

				case 3:
					return 2;

				case 4:
					return 5;

				default:
					return 4;
			}
	}
}