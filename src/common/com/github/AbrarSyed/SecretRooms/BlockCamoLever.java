package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;


// Referenced classes of package net.minecraft.src:
//            Block, Material, World, AxisAlignedBB,
//            Vec3D, MovingObjectPosition

public class BlockCamoLever extends BlockCamoFull
{
    protected BlockCamoLever(int i)
    {
        super(i, Material.circuits);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if (i == 1)
        {
            return Block.lever.blockIndexInTexture;
        }
        else if (i == 3)
        	return Block.planks.blockIndexInTexture;
        else
        {
            return blockIndexInTexture;
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int l)
    {
        if (l == 1 && world.isBlockNormalCube(i, j - 1, k))
        {
            return true;
        }

        if (l == 2 && world.isBlockNormalCube(i, j, k + 1))
        {
            return true;
        }

        if (l == 3 && world.isBlockNormalCube(i, j, k - 1))
        {
            return true;
        }

        if (l == 4 && world.isBlockNormalCube(i + 1, j, k))
        {
            return true;
        }

        return l == 5 && world.isBlockNormalCube(i - 1, j, k);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if (world.isBlockNormalCube(i - 1, j, k))
        {
            return true;
        }

        if (world.isBlockNormalCube(i + 1, j, k))
        {
            return true;
        }

        if (world.isBlockNormalCube(i, j, k - 1))
        {
            return true;
        }

        if (world.isBlockNormalCube(i, j, k + 1))
        {
            return true;
        }

        return world.isBlockNormalCube(i, j - 1, k);
    }

    @Override
    public void updateBlockMetadata(World world, int i, int j, int k, int l, float something1, float something2, float something3)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        int j1 = i1 & 8;
        i1 &= 7;
        i1 = -1;

        if (l == 1 && world.isBlockNormalCube(i, j - 1, k))
        {
            i1 = 5 + world.rand.nextInt(2);
        }

        if (l == 2 && world.isBlockNormalCube(i, j, k + 1))
        {
            i1 = 4;
        }

        if (l == 3 && world.isBlockNormalCube(i, j, k - 1))
        {
            i1 = 3;
        }

        if (l == 4 && world.isBlockNormalCube(i + 1, j, k))
        {
            i1 = 2;
        }

        if (l == 5 && world.isBlockNormalCube(i - 1, j, k))
        {
            i1 = 1;
        }

        if (i1 == -1)
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
        }
        else
        {
        	System.out.println("metadata = "+(i1));
            world.setBlockMetadataWithNotify(i, j, k, i1 + j1);
        }
        
        super.updateBlockMetadata(world, i,j, k, l, something1, something2, something3);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
    {
        if (world.isRemote)
        {
            return true;
        }
        
        TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(i, j, k);
        System.out.println("ACTIVATED: "+entity.getCopyID());

        int l = world.getBlockMetadata(i, j, k);
        int i1 = l & 7;
        int j1 = 8 - (l & 8);
        world.setBlockMetadataWithNotify(i, j, k, i1 + j1);
        world.markBlocksDirty(i, j, k, i, j, k);
        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, j1 <= 0 ? 0.5F : 0.6F);
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);
        world.markBlockNeedsUpdate(i, j, k);

        if (i1 == 1)
        {
            world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
        }
        else if (i1 == 2)
        {
            world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
        }
        else if (i1 == 3)
        {
            world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
        }
        else if (i1 == 4)
        {
            world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
        }
        else
        {
            world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int something, int metadata)
    {

        if ((metadata & 8) > 0)
        {
            world.notifyBlocksOfNeighborChange(i, j, k, blockID);
            int i1 = metadata & 7;

            if (i1 == 1)
            {
                world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
            }
            else if (i1 == 2)
            {
                world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
            }
            else if (i1 == 3)
            {
                world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
            }
            else if (i1 == 4)
            {
                world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
            }
            else
            {
                world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
            }
        }

        super.breakBlock(world, i, j, k, something, metadata);
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        return (iblockaccess.getBlockMetadata(i, j, k) & 8) > 0;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);

        if ((i1 & 8) == 0)
        {
            return false;
        }

        int j1 = i1 & 7;

        if (j1 == 6 && l == 1)
        {
            return true;
        }

        if (j1 == 5 && l == 1)
        {
            return true;
        }

        if (j1 == 4 && l == 2)
        {
            return true;
        }

        if (j1 == 3 && l == 3)
        {
            return true;
        }

        if (j1 == 2 && l == 4)
        {
            return true;
        }

        return j1 == 1 && l == 5;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }
}
