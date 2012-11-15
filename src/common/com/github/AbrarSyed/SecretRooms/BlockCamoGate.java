package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * @author AbrarSyed
 */
public class BlockCamoGate extends BlockCamoFull
{
	protected BlockCamoGate(int i)
	{
		super(i);
		this.setHardness(1.5F);
		this.setStepSound(Block.soundWoodFootstep);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{
		destroyGate(world, i, j, k);
	}

	@Override
	public int getBlockTextureFromSide(int i)
	{
		if (i <= 1)
		{
			return 4;
		}
		else
		{
			return blockIndexInTexture;
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		if (l > 0 && Block.blocksList[l].canProvidePower())
		{
			world.scheduleBlockUpdate(i, j, k, blockID, 0);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
	{
		int metadata = setDefaultDirection(world, i, j, k, (EntityPlayer) entityliving);
		world.setBlockMetadataWithNotify(i, j, k, metadata);
	}

	private static int setDefaultDirection(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int l = MathHelper.floor_double((double) ((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		double d = (entityplayer.posY + 1.82D) - (double) entityplayer.yOffset;

		if (MathHelper.abs((float) entityplayer.posX - (float) i) < 2.0F && MathHelper.abs((float) entityplayer.posZ - (float) k) < 2.0F)
		{
			if (d - (double) j > 2D)
			{
				return 1;
			}

			if ((double) j - d > 0.0D)
			{
				return 0;
			}
		}

		if (l == 0)
		{
			return 2;
		}

		if (l == 1)
		{
			return 5;
		}

		if (l == 2)
		{
			return 3;
		}

		if (l == 3)
		{
			return 4;
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		boolean flag = !world.isRemote && (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k));

		if (flag)
		{
			buildGate(world, i, j, k);
		}
		else
		{
			destroyGate(world, i, j, k);
		}
	}

	public void buildGate(World world, int x, int y, int z)
	{
		int data = world.getBlockMetadata(x, y, z);
		boolean stop = false;
		int i = 1;

		for (; i <= maxSize && stop == false; i++)
		{
			ForgeDirection dir = ForgeDirection.getOrientation(i);
			if (world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) || isBreakable(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))
			{
				world.setBlockWithNotify(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, SecretRooms.camoGateExt.blockID);
				world.setBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, SecretRooms.camoGateExt.func_85104_a(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, 0, 0, 0, 0, 0));
			}
			else
				break;
		}
	}

	public void destroyGate(World world, int x, int y, int z)
	{
		int data = world.getBlockMetadata(x, y, z);

		for (int i = 1; i <= maxSize; i++)
		{
			switch (data)
				{
					case 0:
						if (world.getBlockId(x, y, z) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x, y - i, z, 0);
						}

						break;

					case 1:
						if (world.getBlockId(x, y + i, z) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x, y + i, z, 0);
						}

						break;

					case 2:
						if (world.getBlockId(x, y, z - i) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x, y, z - i, 0);
						}

						break;

					case 3:
						if (world.getBlockId(x, y, z + i) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x, y, z + i, 0);
						}

						break;

					case 4:
						if (world.getBlockId(x - i, y, z) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x - i, y, z, 0);
						}

						break;

					case 5:
						if (world.getBlockId(x + i, y, z) == SecretRooms.camoGateExt.blockID)
						{
							world.setBlockWithNotify(x + i, y, z, 0);
						}

						break;
				}
		}
	}

	public boolean isBreakable(World world, int x, int y, int z)
	{
		int id = world.getBlockId(x, y, z);

		if (id == 0)
			return true;

		if (id == SecretRooms.oneWay.blockID || Block.blocksList[id] instanceof BlockCamoFull)
		{
			return false;
		}

		if ((world.isBlockNormalCube(x, y, z)))
		{
			return false;
		}

		return true;
	}

	private static final int	maxSize	= 10;
}