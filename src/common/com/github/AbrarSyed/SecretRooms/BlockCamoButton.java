package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoButton extends BlockCamoFull
{
    protected BlockCamoButton(int i)
    {
    	super(i, Material.circuits);
    	this.setCreativeTab(SecretRooms.tab);
    }
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
    }

    @Override
    public int tickRate()
    {
        return 20;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        if (i == 3 || i == 1)
        {
            return Block.cobblestone.blockIndexInTexture;
        }
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
    public void updateBlockMetadata(World world, int i, int j, int k, int l, float something, float something2, float something3)
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
            world.setBlockMetadataWithNotify(i, j, k, i1 + j1);
        }
        
        super.updateBlockMetadata(world, i,j, k, l, something, something2, something3);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (checkIfAttachedToBlock(world, i, j, k))
        {
            int i1 = world.getBlockMetadata(i, j, k) & 7;
            boolean flag = false;

            if (!world.isBlockNormalCube(i - 1, j, k) && i1 == 1)
            {
                flag = true;
            }

            if (!world.isBlockNormalCube(i + 1, j, k) && i1 == 2)
            {
                flag = true;
            }

            if (!world.isBlockNormalCube(i, j, k - 1) && i1 == 3)
            {
                flag = true;
            }

            if (!world.isBlockNormalCube(i, j, k + 1) && i1 == 4)
            {
                flag = true;
            }

            if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 5)
            {
                flag = true;
            }

            if (!world.isBlockNormalCube(i, j - 1, k) && i1 == 6)
            {
                flag = true;
            }

            if (flag)
            {
                dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
                world.setBlockWithNotify(i, j, k, 0);
            }
        }
    }

    private boolean checkIfAttachedToBlock(World world, int i, int j, int k)
    {
        if (!canPlaceBlockAt(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something, float something1, float soemthin2, float soemthing3)
    {
        if (world.isRemote)
        {
            return true;
        }

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

        world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
        return true;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        if (world.isRemote)
        {
            return;
        }

        int l = world.getBlockMetadata(i, j, k);

        if ((l & 8) == 0)
        {
            return;
        }

        int i1 = l & 7;
        int j1 = 8 - (l & 8);
        world.setBlockMetadataWithNotify(i, j, k, i1 + j1);
        world.markBlocksDirty(i, j, k, i, j, k);
        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, j1 <= 0 ? 0.5F : 0.6F);
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);

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

        world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.click", 0.3F, 0.5F);
        world.markBlocksDirty(i, j, k, i, j, k);
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
    public boolean isIndirectlyPoweringTo(IBlockAccess world, int i, int j, int k, int l)
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