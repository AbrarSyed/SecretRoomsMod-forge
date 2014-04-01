package com.github.AbrarSyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSolidAir extends Block
{

	private IIcon	clear;

	public BlockSolidAir(Material mat)
	{
		super(mat);
		this.setCreativeTab(SecretRooms.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_SOLID_AIR);
		clear = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_CLEAR);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int dir)
	{
		if (SecretRooms.displayCamo)
			return clear;
		else
			return blockIcon;
	}
	
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
    }

	@Override
	public boolean getBlocksMovement(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isCollidable()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int par1)
	{
		return 0xffffff;
	}

	@Override
	public int getMobilityFlag()
	{
		return 0;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	@Override
	public boolean isReplaceableOreGen(World world, int x, int y, int z, Block target)
	{
		return true;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
	{
		return 0;
	}

}
