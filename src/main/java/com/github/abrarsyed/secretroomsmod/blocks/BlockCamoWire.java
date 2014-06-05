package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.List;

import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoWire extends BlockCamoFull
{
	private boolean	shouldPower;

	public BlockCamoWire()
	{
		super(Material.circuits);
		shouldPower = true;
	}

	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_REDSTONE);
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
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		super.onNeighborBlockChange(world, x, y, z, block);

		if (world.isRemote)
			return;

		calcPower(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int par6)
	{
		super.breakBlock(world, x, y, z, block, par6);

		if (world.isRemote)
			return;

		world.notifyBlocksOfNeighborChange(x, y, z, this);
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
		{
			power = nonWirePower;
		}
		else if (power > 0)
		{
			power--;
		}

		if (oldPower != power)
		{
			world.setBlockMetadataWithNotify(x, y, z, power, 2);
			world.notifyBlocksOfNeighborChange(x, y, z, this);
		}
	}

	private int getInputPower(World world, int x, int y, int z)
	{
		int[] powers = new int[6];

		int pX, pY, pZ, power = 0;
		Block block;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			power = 0;
			pX = dir.offsetX + x;
			pY = dir.offsetY + y;
			pZ = dir.offsetZ + z;

			if (world.isAirBlock(pX, pY, pZ))
			{
				continue;
			}

			block = world.getBlock(pX, pY, pZ);
			if (block == Blocks.redstone_wire || block == this)
			{
				power = world.getBlockMetadata(pX, pY, pZ);
			}
			else if (block.hasComparatorInputOverride())
			{
				power = block.getComparatorInputOverride(world, pX, pY, pZ, dir.getOpposite().ordinal());
			}
			else if (block.canProvidePower())
			{
				power = Math.max(block.isProvidingStrongPower(world, pX, pY, pZ, dir.getOpposite().ordinal()), block.isProvidingWeakPower(world, pX, pY, pZ, dir.getOpposite().ordinal()));
			}

			powers[dir.ordinal()] = power;
		}

		power = 0;
		for (int p : powers)
		{
			power = Math.max(power, p);
		}

		power = Math.max(power, world.getStrongestIndirectPower(x, y, z));
		return power;
	}

	public static void setRedstoneProvidePower(boolean bool)
	{
		ObfuscationReflectionHelper.setPrivateValue(BlockRedstoneWire.class, Blocks.redstone_wire, bool, 0);
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
    
    public void addWailaBody(World world, int x, int y, int z, List<String> wailaList)
    {
        wailaList.add(String.format("%s : %s", LangUtil.translateG("hud.msg.power", new Object[0]), world.getBlockMetadata(x, y, z)));
    }
}
