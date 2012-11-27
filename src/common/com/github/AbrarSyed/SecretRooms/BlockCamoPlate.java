package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoPlate extends BlockCamoFull
{
    /** The mob type that can trigger this pressure plate. */
    private EnumMobType triggerMobType;

    protected BlockCamoPlate(int par1, EnumMobType par3EnumMobType)
    {
        super(par1, Material.circuits);
        triggerMobType = par3EnumMobType;
        setTickRandomly(true);
        this.setHardness(0.5F);
        this.setRequiresSelfNotify();
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
    public int tickRate()
    {
        return 20;
    }
    
    @Override
    public int getBlockTextureFromSide(int i)
    {
    	if (i == 1)
    		return Block.planks.blockIndexInTexture;
    	
    	if ((triggerMobType.equals(EnumMobType.players) || blockID == SecretRooms.camoPlatePlayer.blockID) && i == 3)
    		return Block.oreDiamond.blockIndexInTexture;
    	else if (i == 3)
    		return Block.planks.blockIndexInTexture;
    	
    	return blockIndexInTexture;
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
        if (par1World.isRemote)
        {
        	par1World.scheduleBlockUpdate(par2, par3, par4, blockID, 0);
            return;
        }
        
        setStateIfMobInteractsWithPlate(par1World, par2, par3, par4);
        if (par1World.getBlockMetadata(par2, par3, par4) == 1)
        	par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate());
        par1World.scheduleBlockUpdate(par2, par3, par4, blockID, 0);
        return;
    }

    /**
     * Checks if there are mobs on the plate. If a mob is on the plate and it is off, it turns it on, and vice versa.
     */
    private void setStateIfMobInteractsWithPlate(World par1World, int par2, int par3, int par4)
    {
        boolean flag = par1World.getBlockMetadata(par2, par3, par4) == 1;
        boolean flag1 = false;
        float f = 0.125F;
        List list = null;

        if (triggerMobType == EnumMobType.everything)
        {
            list = par1World.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox((float)par2 + f, par3+1, (float)par4 + f, (float)(par2 + 1) - f, (double)par3 + 1.25D, (float)(par4 + 1) - f));
        }

        if (triggerMobType == EnumMobType.mobs)
        {
            list = par1World.getEntitiesWithinAABB(net.minecraft.src.EntityLiving.class, AxisAlignedBB.getBoundingBox((float)par2 + f, par3+1, (float)par4 + f, (float)(par2 + 1) - f, (double)par3 + 1.25D, (float)(par4 + 1) - f));
        }

        if (triggerMobType == EnumMobType.players)
        {
            list = par1World.getEntitiesWithinAABB(net.minecraft.src.EntityPlayer.class, AxisAlignedBB.getBoundingBox((float)par2 + f, par3+1, (float)par4 + f, (float)(par2 + 1) - f, (double)par3 + 1.25D, (float)(par4 + 1) - f));
        }

        if (list.size() > 0)
        {
            flag1 = true;
        }

        if (flag1 && !flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, blockID);
            par1World.markBlockForUpdate(par2, par3, par4);
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!flag1 && flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0);
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, blockID);
            par1World.markBlockForUpdate(par2, par3, par4);
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate());
        }
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
        }

        super.breakBlock(par1World, par2, par3, par4, something, metadata);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side
     */
    @Override
    public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par1IBlockAccess.getBlockMetadata(par2, par3, par4) > 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side
     */
    @Override
    public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 0 ? false : par5 == 1;
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
