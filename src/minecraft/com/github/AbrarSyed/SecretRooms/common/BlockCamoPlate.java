package com.github.AbrarSyed.SecretRooms.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoPlate extends BlockCamoFull
{
	private boolean	players;

	protected BlockCamoPlate(int par1, boolean players)
	{
		super(par1, Material.circuits);
		this.players = players;
		setTickRandomly(true);
		setHardness(0.5F);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World world)
	{
		return 20;
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int meta)
	{
		if (i == 1)
			return Block.planks.getBlockTextureFromSide(i);

		if (players && i == 3)
			return Block.oreDiamond.getBlockTextureFromSide(i);
		else if (i == 3)
			return Block.planks.getBlockTextureFromSide(i);

		return this.blockIcon;
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int i)
	{
		return true;
	}

	/**
	 * Called whenever the block is added into the world. Args: world, x, y, z
	 */
	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		super.onBlockAdded(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID, 0);
	}
	
    /**
     * Ticks the block if it's been scheduled
     */
	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            int power = this.getPowerFromMeta(par1World.getBlockMetadata(par2, par3, par4));

            if (power > 0)
            {
                this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4, power);
            }
        }
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (!par1World.isRemote)
        {
            int power = this.getPowerFromMeta(par1World.getBlockMetadata(par2, par3, par4));

            if (power == 0)
            {
                this.setStateIfMobInteractsWithPlate(par1World, par2, par3, par4, power);
            }
        }
    }

	protected AxisAlignedBB getSensetiveAABB(int x, int y, int z)
	{
		float f = 0.125F;
		return AxisAlignedBB.getAABBPool().getAABB(x + f, y + 1, z + f, x + 1 - f, y + 1.25D, z + 1 - f);
	}

	/**
	 * Checks if there are mobs on the plate. If a mob is on the plate and it is off, it turns it on, and vice versa.
	 */
	private void setStateIfMobInteractsWithPlate(World world, int x, int y, int z, int oldPower)
	{
        int weight = this.getCurrentWeight(world, x, y, z);
        boolean isPowerred = oldPower > 0;
        boolean hasWeight = weight > 0;

        if (oldPower != weight)
        {
            world.setBlockMetadataWithNotify(x, y, z, this.getMetaFromWeight(weight), 2);
            this.notifyArround(world, x, y, z);
            world.markBlockForRenderUpdate(x, y, z);
        }

        if (!hasWeight && isPowerred)
        {
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        }
        else if (hasWeight && !isPowerred)
        {
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (hasWeight)
        {
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
        }
	}

	protected int getCurrentWeight(World par1World, int par2, int par3, int par4)
	{
		List list = null;

		if (players)
		{
			list = par1World.getEntitiesWithinAABB(EntityPlayer.class, getSensetiveAABB(par2, par3, par4));
		}
		else
		{
			list = par1World.getEntitiesWithinAABBExcludingEntity(null, getSensetiveAABB(par2, par3, par4));
		}

		if (list.isEmpty())
			return 0;

		Iterator iterator = list.iterator();

		while (iterator.hasNext())
		{
			Entity entity = (Entity) iterator.next();

			if (!entity.doesEntityNotTriggerPressurePlate())
			{
				return 15;
			}
		}

		return 0;
	}

	/**
	 * Called whenever the block is removed.
	 */
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int something, int metadata)
	{
		if (metadata > 0)
		{
			par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, blockID);
			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, blockID);
		}

		super.breakBlock(par1World, par2, par3, par4, something, metadata);
	}

	protected void notifyArround(World world, int x, int y, int z)
	{
		world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
		world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
	}

	protected int getPowerFromMeta(int meta)
	{
		return meta > 0 ? 15 : 0;
	}

	protected int getMetaFromWeight(int weight)
	{
		return weight > 0 ? 1 : 0;
	}

	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side
	 */
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return getPowerFromMeta(world.getBlockMetadata(x, y, z));
	}

	/**
	 * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
	 * side
	 */
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return getPowerFromMeta(world.getBlockMetadata(x, y, z));
	}

	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	/**
	 * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
	 * and stop pistons
	 */
	@Override
	public int getMobilityFlag()
	{
		return 1;
	}
}
