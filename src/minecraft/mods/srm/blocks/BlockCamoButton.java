package mods.SecretRoomsMod.blocks;

import java.util.ArrayList;
import java.util.Random;

import mods.SecretRoomsMod.SecretRooms;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
	public int tickRate(World world)
	{
		return 20;
	}

	@Override
	public Icon getIcon(int i, int j)
	{
		if (i == 3 || i == 1)
			return Block.cobblestone.getBlockTextureFromSide(i);
		else
			return blockIcon;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something, float something1, float soemthin2, float soemthing3)
	{
		int isActive = world.getBlockMetadata(i, j, k);

		if (isActive > 0)
			return true;

		world.setBlockMetadataWithNotify(i, j, k, 1, 3);
		notifyArround(world, i, j, k);
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
		world.markBlockForUpdate(i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
		return true;
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		int isActive = world.getBlockMetadata(i, j, k);

		if (isActive == 0)
			return;

		world.setBlockMetadataWithNotify(i, j, k, 0, 3);
		notifyArround(world, i, j, k);
		world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
		world.markBlockForUpdate(i, j, k);
	}

	@Override
	public void breakBlock(World world, int i, int j, int k, int something, int metadata)
	{
		super.breakBlock(world, i, j, k, something, metadata);
	}

	protected void notifyArround(World world, int x, int y, int z)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
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

	@Override
	public boolean canProvidePower()
	{
		return true;
	}
}
