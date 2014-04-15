package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockTorchLever extends BlockTorch
{
    public BlockTorchLever(int j)
    {
        super();
        setTickRandomly(true);
        setHardness(0);
        setLightLevel(0.9375F);
        setStepSound(Block.soundTypeWood);
        setCreativeTab(SecretRooms.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_TORCH);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (SecretRooms.displayCamo)
            return Blocks.torch.getIcon(par5, 0);
        else
            return super.getIcon(par1iBlockAccess, par2, par3, par4, par5);
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
        return SecretRooms.renderFlatId;
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if (!SecretRooms.displayCamo)
            return;

        int l = world.getBlockMetadata(i, j, k) & 7;
        double d = i + 0.5F;
        double d1 = j + 0.7F;
        double d2 = k + 0.5F;
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

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
    {
        if (world.isRemote)
            return true;

        int l = world.getBlockMetadata(i, j, k);
        int i1 = l & 7;
        int j1 = 8 - (l & 8);
        world.setBlockMetadataWithNotify(i, j, k, i1 + j1, 2);
        world.markBlockForUpdate(i, j, k);
        world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, j1 <= 0 ? 0.5F : 0.6F);
        world.notifyBlocksOfNeighborChange(i, j, k, this);

        if (i1 == 1)
        {
            world.notifyBlocksOfNeighborChange(i - 1, j, k, this);
        }
        else if (i1 == 2)
        {
            world.notifyBlocksOfNeighborChange(i + 1, j, k, this);
        }
        else if (i1 == 3)
        {
            world.notifyBlocksOfNeighborChange(i, j, k - 1, this);
        }
        else if (i1 == 4)
        {
            world.notifyBlocksOfNeighborChange(i, j, k + 1, this);
        }
        else if (i1 == 5)
        {
            world.notifyBlocksOfNeighborChange(i, j - 1, k, this);
        }

        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        if ((metadata & 8) > 0)
        {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            int i1 = metadata & 7;

            if (i1 == 1)
            {
                world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            }
            else if (i1 == 2)
            {
                world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            }
            else if (i1 == 3)
            {
                world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            }
            else if (i1 == 4)
            {
                world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            }
            else
            {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            }
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side
     */
    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) > 0 ? 15 : 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side
     */
    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if ((var6 & 8) == 0)
            return 0;
        else
        {
            int var7 = var6 & 7;
            if (var7 == 0 && par5 == 0 ? true : var7 == 7 && par5 == 0 ? true : var7 == 6 && par5 == 1 ? true : var7 == 5 && par5 == 1 ? true : var7 == 4 && par5 == 2 ? true : var7 == 3 && par5 == 3 ? true : var7 == 2 && par5 == 4 ? true : var7 == 1 && par5 == 5)
                return 15;

            return 0;
        }
    }
}
