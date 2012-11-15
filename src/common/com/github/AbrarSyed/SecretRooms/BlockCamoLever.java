package com.github.AbrarSyed.SecretRooms;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

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


/**
 * @author AbrarSyed
 */
public class BlockCamoLever extends BlockCamoFull
{
    protected BlockCamoLever(int i)
    {
        super(i, Material.circuits);
        this.setHardness(1.5F);
        this.setStepSound(Block.soundWoodFootstep);
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
    
    public int func_85104_a(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ, int currentMeta)
    {
    	super.func_85104_a(world, x,y, z, side, clickX, clickY, clickZ, currentMeta);
        int var11 = currentMeta & 8;
        int var10 = currentMeta & 7;
        var10 = -1;

        if (side == 0 && world.isBlockSolidOnSide(x, y + 1, z, DOWN))
        {
            var10 = world.rand.nextBoolean() ? 0 : 7;
        }

        if (side == 1 && world.isBlockSolidOnSide(x, y - 1, z, UP))
        {
            var10 = 5 + world.rand.nextInt(2);
        }

        if (side == 2 && world.isBlockSolidOnSide(x, y, z + 1, NORTH))
        {
            var10 = 4;
        }

        if (side == 3 && world.isBlockSolidOnSide(x, y, z - 1, SOUTH))
        {
            var10 = 3;
        }

        if (side == 4 && world.isBlockSolidOnSide(x + 1, y, z, WEST))
        {
            var10 = 2;
        }

        if (side == 5 && world.isBlockSolidOnSide(x - 1, y, z, EAST))
        {
            var10 = 1;
        }

        return var10 + var11;
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
