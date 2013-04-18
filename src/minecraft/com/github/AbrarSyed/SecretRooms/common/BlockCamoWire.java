package com.github.AbrarSyed.SecretRooms.common;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoWire extends BlockCamoFull
{
	private boolean	shouldPower;

	public BlockCamoWire(int id)
	{
		super(id, Material.circuits);
		shouldPower = true;
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		
		if (world.isRemote)
			return;

		calcPower(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		super.onNeighborBlockChange(world, x, y, z, blockID);
		
		if (world.isRemote)
			return;

		calcPower(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
	{
		super.breakBlock(world, x, y, z, par5, par6);
		
		if (world.isRemote)
			return;

		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
	}

	private void calcPower(World world, int x, int y, int z)
	{
		int oldPower = world.getBlockMetadata(x, y, z);

		setRedstoneProvidePower(false);
		shouldPower = false;
		int nonWirePower = world.getStrongestIndirectPower(x, y, z);
		shouldPower = true;
		setRedstoneProvidePower(true);
		int power = getInputPower(world, x, y, z);

		if (nonWirePower > 0 && nonWirePower > power - 1)
			power = nonWirePower;
		else if (power > 0)
			power--;

		if (oldPower != power)
		{
			world.setBlockMetadataWithNotify(x, y, z, power, 2);
			world.notifyBlocksOfNeighborChange(x, y, z, blockID);
		}
	}

	private int getInputPower(World world, int x, int y, int z)
	{
		int[] powers = new int[6];

		int pX, pY, pZ, side, power = 0;
		Block block;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			power = 0;
			pX = dir.offsetX + x;
			pY = dir.offsetY + y;
			pZ = dir.offsetZ + z;

			if (world.isAirBlock(pX, pY, pZ))
				continue;

			block = Block.blocksList[world.getBlockId(pX, pY, pZ)];
			if (block.blockID == Block.redstoneWire.blockID || block.blockID == blockID)
				power = world.getBlockMetadata(pX, pY, pZ);
			else if (block.hasComparatorInputOverride())
				power = block.getComparatorInputOverride(world, pX, pY, pZ, dir.getOpposite().ordinal());
			else if (block.canProvidePower())
				power = Math.max(block.isProvidingStrongPower(world, pX, pY, pZ, dir.getOpposite().ordinal()), block.isProvidingWeakPower(world, pX, pY, pZ, dir.getOpposite().ordinal()));

			powers[dir.ordinal()] = power;
		}

		power = 0;
		for (int p : powers)
			power = Math.max(power, p);

		power = Math.max(power, world.getStrongestIndirectPower(x, y, z));
		return power;
	}

	public static void setRedstoneProvidePower(boolean bool)
	{
		ObfuscationReflectionHelper.setPrivateValue(BlockRedstoneWire.class, Block.redstoneWire, bool, 0);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return canProvidePower() ? world.getBlockMetadata(x, y, z) : 0;
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return isProvidingStrongPower(world, x, y, z, side);
	}

	@Override
	public boolean canProvidePower()
	{
		return shouldPower;
	}
}
