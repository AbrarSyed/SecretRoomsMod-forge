package com.github.AbrarSyed.secretroomsmod.blocks;

import java.util.ArrayList;

import com.github.AbrarSyed.secretroomsmod.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoLever extends BlockCamoFull
{
	public BlockCamoLever(int i)
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
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_LEVER);
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
	{
		if (world.isRemote)
			return true;
		else
		{
			int meta = world.getBlockMetadata(i, j, k);
			world.setBlockMetadataWithNotify(i, j, k, 1 - meta, 2);
			world.markBlockForUpdate(i, j, k);
			notifyArround(world, i, j, k);
			world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, meta > 0 ? 0.6F : 0.5F);

			return true;
		}
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{
		notifyArround(world, i, j, k);
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
		return world.getBlockMetadata(x, y, z) > 0 ? 15 : 0;
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return world.getBlockMetadata(x, y, z) > 0 ? 15 : 0;
	}

	protected void notifyArround(World world, int x, int y, int z)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
}
