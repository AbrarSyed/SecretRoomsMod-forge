package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;


// Referenced classes of package net.minecraft.src:
//            Block, Material, World, AxisAlignedBB,
//            Vec3D, MovingObjectPosition

public class BlockTorchLever extends BlockTorch
{
    protected BlockTorchLever(int i, int j)
    {
        super(i, j);
        setTickRandomly(true);
        this.setHardness(0);
        this.setLightValue(0.9375F);
        this.setStepSound(Block.soundWoodFootstep);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        super.updateTick(world, i, j, k, random);

        if (world.getBlockMetadata(i, j, k) == 0)
        {
            onBlockAdded(world, i, j, k);
        }
    }

    @Override
    public int getRenderType()
    {
        return SecretRooms.torchRenderId;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if (world.isBlockNormalCubeDefault(i - 1, j, k, true))
        {
            return true;
        }

        if (world.isBlockNormalCubeDefault(i + 1, j, k, true))
        {
            return true;
        }

        if (world.isBlockNormalCubeDefault(i, j, k - 1, true))
        {
            return true;
        }

        if (world.isBlockNormalCubeDefault(i, j, k + 1, true))
        {
            return true;
        }

        return canPlaceTorchOn(world, i, j - 1, k);
    }

    @Override
    public void updateBlockMetadata(World world, int x, int y, int z, int side, float something1, float something2, float something3)
    {
        int i1 = world.getBlockMetadata(x, y, z) & 7;

        if (side == 1 && canPlaceTorchOn(world, x, y - 1, z))
        {
            i1 = 5;
        }

        if (side == 2 && world.isBlockNormalCubeDefault(x, y, z + 1, true))
        {
            i1 = 4;
        }

        if (side == 3 && world.isBlockNormalCubeDefault(x, y, z - 1, true))
        {
            i1 = 3;
        }

        if (side == 4 && world.isBlockNormalCubeDefault(x + 1, y, z, true))
        {
            i1 = 2;
        }

        if (side == 5 && world.isBlockNormalCubeDefault(x - 1, y, z, true))
        {
            i1 = 1;
        }

        world.setBlockMetadataWithNotify(x, y, z, i1);
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {}

    @SideOnly(value=Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        int l = world.getBlockMetadata(i, j, k) & 7;
        double d = (float)i + 0.5F;
        double d1 = (float)j + 0.7F;
        double d2 = (float)k + 0.5F;
        double d3 = 0.22D;
        double d4 = 0.27D;

        if (l == 1)
        {
            world.spawnParticle("smoke", d - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d - d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 2)
        {
            world.spawnParticle("smoke", d + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d + d4, d1 + d3, d2, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 3)
        {
            world.spawnParticle("smoke", d, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d, d1 + d3, d2 - d4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 4)
        {
            world.spawnParticle("smoke", d, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d, d1 + d3, d2 + d4, 0.0D, 0.0D, 0.0D);
        }
        else if (l == 5)
        {
            world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1)
    {
        int l = world.getBlockMetadata(i, j, k) & 7;
        float f = 0.15F;

        if (l == 1)
        {
            setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (l == 2)
        {
            setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (l == 3)
        {
            setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (l == 4)
        {
            setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
        else if (l == 4)
        {
            float f1 = 0.1F;
            setBlockBounds(0.5F - f1, 0.0F, 0.5F - f1, 0.5F + f1, 0.6F, 0.5F + f1);
        }

        return super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    private boolean canPlaceTorchOn(World world, int i, int j, int k)
    {
        if (world.isBlockNormalCubeDefault(i, j, k, true))
        {
            return true;
        }
        else
        {
            int l = world.getBlockId(i, j, k);
            return l == Block.fence.blockID || l == Block.netherFence.blockID;
        }
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
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
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
        else if (i1 == 5)
        {
            world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int something, int metadata)
    {
        if ((metadata & 8) > 0)
        {
            world.notifyBlocksOfNeighborChange(x, y, z, blockID);
            int i1 = metadata & 7;

            if (i1 == 1)
            {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, blockID);
            }
            else if (i1 == 2)
            {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, blockID);
            }
            else if (i1 == 3)
            {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, blockID);
            }
            else if (i1 == 4)
            {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, blockID);
            }
            else
            {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, blockID);
            }
        }

        super.breakBlock(world, x, y, z, something, metadata);
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
}
