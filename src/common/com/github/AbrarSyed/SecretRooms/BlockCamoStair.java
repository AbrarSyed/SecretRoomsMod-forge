package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

/**
 * 
 * @author alexbegt
 */
public class BlockCamoStair extends BlockCamoFull {

	protected BlockCamoStair(int par1) {
		super(par1);
		this.setHardness(1.5F);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setLightOpacity(15);
	}
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
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
     * Sets teh special colliding bounding box necessary for stairs
     */
    public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        int var9 = metadata & 3;
        float var10 = 0.0F;
        float var11 = 0.5F;
        float var12 = 0.5F;
        float var13 = 1.0F;

        if ((metadata & 4) != 0)
        {
            var10 = 0.5F;
            var11 = 1.0F;
            var12 = 0.0F;
            var13 = 0.5F;
        }

        this.setBlockBounds(0.0F, var10, 0.0F, 1.0F, var11, 1.0F);
        super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);

        switch(var9)
        {
        case 0:
        	this.setBlockBounds(0.5F, var12, 0.0F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
            break;
        case 1: 
            this.setBlockBounds(0.5F, var12, 0.0F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
            break;
        case 2: 
            this.setBlockBounds(0.0F, var12, 0.5F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
            break;
        case 3:
            this.setBlockBounds(0.0F, var12, 0.0F, 1.0F, var13, 0.5F);
            super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
            break;
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    /**
     * Sets the metadata of the stairs when they are placed
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var7 = par1World.getBlockMetadata(par2, par3, par4) & 4;

        if (var6 == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | var7);
        }

        if (var6 == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | var7);
        }

        if (var6 == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | var7);
        }

        if (var6 == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | var7);
        }
    }
}
