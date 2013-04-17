package com.github.AbrarSyed.SecretRooms.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockCamoGate extends BlockCamoFull
{
	private static final int	maxSize	= 10;

	protected BlockCamoGate(int i)
	{
		super(i);
		setHardness(1.5F);
		setStepSound(Block.soundWoodFootstep);
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
	public Icon getIcon(int side, int meta)
	{
		if (side <= 1)
			return Block.wood.getBlockTextureFromSide(side);
		else
			return this.blockIcon;
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entity, ItemStack stack)
	{
		int metadata = setDefaultDirection(world, x, y, z, (EntityPlayer) entity);
		world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
	}

	private static int setDefaultDirection(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int l = MathHelper.floor_double(entityplayer.rotationYaw * 4F / 360F + 0.5D) & 3;
		double d = entityplayer.posY + 1.82D - entityplayer.yOffset;

		if (MathHelper.abs((float) entityplayer.posX - i) < 2.0F && MathHelper.abs((float) entityplayer.posZ - k) < 2.0F)
		{
			if (d - j > 2D)
				return 1;

			if (j - d > 0.0D)
				return 0;
		}

		if (l == 0)
			return 2;

		if (l == 1)
			return 5;

		if (l == 2)
			return 3;

		if (l == 3)
			return 4;
		else
			return 0;
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
		ForgeDirection dir = ForgeDirection.getOrientation(data);
		int xOffset, yOffset, zOffset;

		for (int i = 1; i <= maxSize && stop == false; i++)
		{
			xOffset = x + (dir.offsetX * i);
			yOffset = y + (dir.offsetY * i);
			zOffset = z + (dir.offsetZ * i);

			if (world.isAirBlock(xOffset, yOffset, zOffset) || isBreakable(world, xOffset, yOffset, zOffset))
			{
				world.setBlock(yOffset, xOffset, zOffset, SecretRooms.camoGateExt.blockID);
			}
			else
			{
				break;
			}
		}
	}

	public void destroyGate(World world, int x, int y, int z)
	{
		int data = world.getBlockMetadata(x, y, z);
		ForgeDirection dir = ForgeDirection.getOrientation(data);
		int xOffset, yOffset, zOffset;

		for (int i = 1; i <= maxSize; i++)
		{
			xOffset = x + (dir.offsetX * i);
			yOffset = y + (dir.offsetY * i);
			zOffset = z + (dir.offsetZ * i);

			if (world.getBlockId(xOffset, yOffset, zOffset) == SecretRooms.camoGateExt.blockID)
			{
				world.setBlockToAir(xOffset, yOffset, zOffset);
			}
		}
	}

	public boolean isBreakable(World world, int x, int y, int z)
	{
		int id = world.getBlockId(x, y, z);

		if (id == 0)
			return true;

		if (id == SecretRooms.oneWay.blockID || Block.blocksList[id] instanceof BlockCamoFull)
			return false;

		if (world.isBlockNormalCube(x, y, z))
			return false;

		return true;
	}
}
