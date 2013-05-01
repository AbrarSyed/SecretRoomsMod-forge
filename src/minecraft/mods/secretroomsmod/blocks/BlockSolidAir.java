package mods.secretroomsmod.blocks;

import java.util.Random;

import mods.secretroomsmod.SecretRooms;
import mods.secretroomsmod.common.fake.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSolidAir extends Block
{

	private Icon	clear;

	public BlockSolidAir(int par1)
	{
		super(par1, Material.air);
		this.setCreativeTab(SecretRooms.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_SOLID_AIR);
		clear = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_CLEAR); 
	}
	
	@Override
	@SideOnly(value = Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
	{
		if (SecretRooms.displayCamo)
			return clear;
		else
			return blockIcon;
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
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
	public boolean isBlockNormalCube(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

	@Override
	public boolean isAirBlock(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean isGenMineableReplaceable(World world, int x, int y, int z, int target)
	{
		return true;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		return 0;
	}

}
