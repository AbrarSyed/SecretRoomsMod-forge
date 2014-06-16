package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockCamoPlate extends BlockCamoFull
{
    private boolean players;

    public BlockCamoPlate(boolean players)
    {
        super(Material.circuits);
        this.players = players;
        setTickRandomly(true);
        setHardness(0.5F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        if (players)
            blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_PLATE_PLAYER);
        else
            blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_PLATE_WOOD);
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
        world.scheduleBlockUpdate(i, j, k, this, 0);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random)
    {
        if (world.isRemote)
        {
            world.scheduleBlockUpdate(x, y, z, this, 0);
        }

        int power = getPowerFromMeta(world.getBlockMetadata(x, y, z));
        setStateIfMobInteractsWithPlate(world, x, y, z, power);
        if (world.getBlockMetadata(x, y, z) > 0)
        {
            world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }
        else
        {
            world.scheduleBlockUpdate(x, y, z, this, 0);
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
            int power = getPowerFromMeta(par1World.getBlockMetadata(par2, par3, par4));

            if (power == 0)
            {
                setStateIfMobInteractsWithPlate(par1World, par2, par3, par4, power);
            }
        }
    }

    protected AxisAlignedBB getSensetiveAABB(int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox(x, y + 1, z, x + 1, y + 2, z + 1);
    }

    /**
     * Checks if there are mobs on the plate. If a mob is on the plate and it is off, it turns it on, and vice versa.
     */
    private void setStateIfMobInteractsWithPlate(World world, int x, int y, int z, int oldPower)
    {
        int weight = getCurrentWeight(world, x, y, z);
        boolean isPowerred = oldPower > 0;
        boolean hasWeight = weight > 0;

        if (oldPower != weight)
        {
            world.setBlockMetadataWithNotify(x, y, z, getMetaFromWeight(weight), 2);
            notifyArround(world, x, y, z);
            world.markBlockForUpdate(x, y, z);
        }

        if (!hasWeight && isPowerred)
        {
            world.playSoundEffect(x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.5F);
        }
        else if (hasWeight && !isPowerred)
        {
            world.playSoundEffect(x + 0.5D, y + 0.1D, z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (hasWeight)
        {
            world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        }
    }

    @SuppressWarnings("rawtypes")
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
                return 15;
        }

        return 0;
    }

    /**
     * Called whenever the block is removed.
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block block, int metadata)
    {
        if (metadata > 0)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this);
            par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this);
        }

        super.breakBlock(par1World, par2, par3, par4, block, metadata);
    }

    protected void notifyArround(World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, this);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
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
