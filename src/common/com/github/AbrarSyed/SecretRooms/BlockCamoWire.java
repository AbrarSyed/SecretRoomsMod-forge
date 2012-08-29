package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class BlockCamoWire extends BlockCamoFull
{
    public BlockCamoWire(int i)
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
        if (i == 3)
        {
            return Block.redstoneWire.blockIndexInTexture;
        }
        else
        {
            return blockIndexInTexture;
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if (l == 0)
        {
            return;
        }

        if (Block.blocksList[l].canProvidePower() || l == blockID)
        {
            //System.out.println(i+" "+j+" "+k+"  notified");
            world.scheduleBlockUpdate(i, j, k, blockID, 0);
        }
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int i, int j, int k, int l)
    {
        if ((getPoweredState(world, i, j, k)) == 0)
        {
            return false;
        }
        else
        {
            return isPoweringTo(world, i, j, k, l);
        }
    }

    @Override
    public boolean isPoweringTo(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if (iblockaccess.getBlockMetadata(i, j, k) == 0)
        {
            return false;
        }

        boolean worked = (iblockaccess.getBlockMetadata(i, j, k) & 7) != getOppositeSide(l);
        //System.out.println("check power to "+l+"  is "+worked);
        return worked;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        boolean isPoweredOld = (getPoweredState(world, i, j, k)) > 0;
        boolean isPowered = !world.isRemote && (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k));

        if (isPowered != isPoweredOld)
        {
            if (isPowered && !isPoweredOld)
            {
                turnOn(world, i, j, k);
            }
            else
            {
                turnOff(world, i, j, k);
            }
        }
        else
        {
            if (!isPowered)
            {
                return;
            }

            boolean[] flags = new boolean[6];

            for (int x = 0; x < 6; x++)
            {
                flags[x] = isBlockPoweredBy(world, i, j, k, x);
            }

            if (!isPoweredFromAllowedDir(world, i, j, k))
            {
                turnOff(world, i, j, k);
            }
        }

        //System.out.println();
    }

    private void turnOff(World world, int i, int j, int k)
    {
        world.setBlockMetadataWithNotify(i, j, k, 0);
        notifyWireNeighborsOfNeighborChange(world, i, j, k);
        world.markBlockAsNeedsUpdate(i, j, k);
        //System.out.println("Turned off");
    }

    private void turnOn(World world, int i, int j, int k)
    {
        boolean[] flags = new boolean[6];

        for (int x = 0; x < 6; x++)
        {
            flags[x] = isBlockPoweredBy(world, i, j, k, x);
        }

        byte poweredSide = -1;

        for (byte x = 0; x < 6; x++)
        {
            if (flags[x])
            {
                poweredSide = x;
            }
        }

        if (poweredSide >= 0)
        {
            world.setBlockMetadataWithNotify(i, j, k, 8 + poweredSide);
            notifyWireNeighborsOfNeighborChange(world, i, j, k);
            world.markBlockAsNeedsUpdate(i, j, k);
            //System.out.println(i+" "+j+" "+k+" Turned on: direction is "+poweredSide);
        }
    }

    private  boolean isPoweredFromAllowedDir(World world, int i, int j, int k)
    {
        byte dir = (byte)(getPoweredDirectionFromMetadata(world, i, j, k));
        boolean[] flags = new boolean[6];

        for (int x = 0; x < 6; x++)
        {
            flags[x] = isBlockPoweredBy(world, i, j, k, x);
        }

        if (flags[dir])
        {
            //System.out.println("It is bieng powered by "+dir);
            return true;
        }

        //System.out.println("It is NOT bieng powered by "+dir);
        return false;
    }

    private boolean isBlockPoweredBy(World world, int i, int j, int k, int l)
    {
        int[] coords = new int[3];

        switch (l)
        {
            case 0:
                coords = new int[] {i, j - 1, k};
                break;
            case 1:
                coords = new int[] {i, j + 1, k};
                break;
            case 2:
                coords = new int[] {i, j, k - 1};
                break;
            case 3:
                coords = new int[] {i, j, k + 1};
                break;
            case 4:
                coords = new int[] {i - 1, j, k};
                break;
            case 5:
                coords = new int[] {i + 1, j, k};
                break;
        }

        int id = world.getBlockId(coords[0], coords[1], coords[2]);

        if (id == 0)
        {
            return false;
        }

        if (id == Block.redstoneWire.blockID && world.getBlockMetadata(coords[0], coords[1], coords[2]) > 0)
        {
            return true;
        }
        else if (id == blockID && l != (world.getBlockMetadata(coords[0], coords[1], coords[2]) & 7) && (world.getBlockMetadata(coords[0], coords[1], coords[2]) & 8) > 0)
        {
            //System.out.println("Checked as Block");
            return true;
        }

        Block block = Block.blocksList[id];

        if (block.canProvidePower() && block.isPoweringTo(world, coords[0], coords[1], coords[2], l))
        {
            return true;
        }

        return false;
    }

    private void notifyWireNeighborsOfNeighborChange(World world, int i, int j, int k)
    {
        world.notifyBlocksOfNeighborChange(i, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
        world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
        world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
        world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
        return;
    }

    private int getPoweredDirectionFromMetadata(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k) & 7;
    }

    private int getPoweredState(World world, int i, int j, int k)
    {
        return world.getBlockMetadata(i, j, k) & 8;
    }

    private int getOppositeSide(int i)
    {
        switch (i)
        {
            case 0:
                return 1;

            case 1:
                return 0;

            case 2:
                return 3;

            case 3:
                return 2;

            case 4:
                return 5;

            default:
                return 4;
        }
    }
}