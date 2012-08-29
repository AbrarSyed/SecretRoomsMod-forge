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

public class BlockCamoStair extends BlockCamoFull {

	protected BlockCamoStair(int par1) {
		super(par1);
		this.setHardness(1.5F);
        this.setStepSound(Block.soundWoodFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setLightOpacity(255);
	}
	
	private boolean field_72156_cr = false;
    private int field_72160_cs = 0;
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
    }
    
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (this.field_72156_cr)
        {
            this.setBlockBounds(0.5F * (float)(this.field_72160_cs % 2), 0.5F * (float)(this.field_72160_cs / 2 % 2), 0.5F * (float)(this.field_72160_cs / 4 % 2), 0.5F + 0.5F * (float)(this.field_72160_cs % 2), 0.5F + 0.5F * (float)(this.field_72160_cs / 2 % 2), 0.5F + 0.5F * (float)(this.field_72160_cs / 4 % 2));
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 10;
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
     * if the specified block is in the given AABB, add its collision bounding box to the given list
     */
    public void addCollidingBlockToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        int var8 = par1World.getBlockMetadata(par2, par3, par4);
        int var9 = var8 & 3;
        float var10 = 0.0F;
        float var11 = 0.5F;
        float var12 = 0.5F;
        float var13 = 1.0F;

        if ((var8 & 4) != 0)
        {
            var10 = 0.5F;
            var11 = 1.0F;
            var12 = 0.0F;
            var13 = 0.5F;
        }

        this.setBlockBounds(0.0F, var10, 0.0F, 1.0F, var11, 1.0F);
        super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);

        if (var9 == 0)
        {
            this.setBlockBounds(0.5F, var12, 0.0F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 1)
        {
            this.setBlockBounds(0.0F, var12, 0.0F, 0.5F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 2)
        {
            this.setBlockBounds(0.0F, var12, 0.5F, 1.0F, var13, 1.0F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
        else if (var9 == 3)
        {
            this.setBlockBounds(0.0F, var12, 0.0F, 1.0F, var13, 0.5F);
            super.addCollidingBlockToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
    
    /**
     * Called when the block is placed in the world.
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
    
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        
        if (getBlockTexture(par1IBlockAccess, par2, par3, par4, 1) == 0)
        {
            int i = 0;
            int j = 0;
            int k = 0;

            for (int l = -1; l <= 1; l++)
            {
                for (int i1 = -1; i1 <= 1; i1++)
                {
                    int j1 = par1IBlockAccess.getBiomeGenForCoords(par2 + i1, par4 + l).getBiomeGrassColor();
                    i += (j1 & 0xff0000) >> 16;
                    j += (j1 & 0xff00) >> 8;
                    k += j1 & 0xff;
                }
            }

            return (i / 9 & 0xff) << 16 | (j / 9 & 0xff) << 8 | k / 9 & 0xff;
        }

        return 0xffffff;
    }

	@Override
    public int getBlockTextureFromSide(int i)
    {
    	if (i == 1)
    		return Block.planks.blockIndexInTexture;
    	else if (i == 3)
    		return Block.planks.blockIndexInTexture;
    	
    	return blockIndexInTexture;
    }
}
