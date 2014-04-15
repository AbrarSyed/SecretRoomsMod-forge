package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockCamoButton extends BlockCamoFull
{

    private IIcon stone;
    private IIcon wood;

    public BlockCamoButton()
    {
        super(Material.circuits);
        setCreativeTab(SecretRooms.tab);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    @Deprecated
    public int tickRate(World world)
    {
        return 20;
    }

    public int tickRate(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta > 0)
            return 30;
        else
            return 20;
    }

    public boolean isSensible(World world, int x, int y, int z)
    {
        return (world.getBlockMetadata(x, y, z) & 7) > 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        stone = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BUTTON + "Stone");
        wood = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BUTTON + "Wood");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        if (par2 == 1)
            return wood;
        else
            return stone;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int something, float something1, float soemthin2, float soemthing3)
    {
        int meta = world.getBlockMetadata(i, j, k);
        int usefullMeta = meta & 7;
        boolean isActive = (world.getBlockMetadata(i, j, k) & 8) > 0;

        if (isActive)
            return true;

        world.setBlockMetadataWithNotify(i, j, k, usefullMeta + 8, 3);
        notifyArround(world, i, j, k);
        world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
        world.markBlockForUpdate(i, j, k);
        world.scheduleBlockUpdate(i, j, k, this, tickRate(world, i, j, k));
        return true;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random)
    {
        int meta = world.getBlockMetadata(i, j, k);
        int usefullMeta = meta & 7;
        boolean isActive = (world.getBlockMetadata(i, j, k) & 8) > 0;

        if (!isActive)
            return;

        world.setBlockMetadataWithNotify(i, j, k, usefullMeta, 3);
        notifyArround(world, i, j, k);
        world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, "random.click", 0.3F, 0.5F);
        world.markBlockForUpdate(i, j, k);
    }

    protected void notifyArround(World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, this);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            int meta = world.getBlockMetadata(x, y, z);

            // sensible and off
            if ((meta & 7) > 0 && (meta & 8) == 0)
            {
                this.checkForArrows(world, x, y, z);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    protected void checkForArrows(World world, int x, int y, int z)
    {
        int meta = world.getBlockMetadata(x, y, z);
        int type = meta & 7;
        boolean on = (meta & 8) != 0;
        this.setBlockBoundsBasedOnState(world, x, y, z);
        List list = world.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ));
        boolean arrowExists = !list.isEmpty();

        if (arrowExists && !on)
        {
            world.setBlockMetadataWithNotify(x, y, z, type + 8, 3);
            this.notifyArround(world, x, y, z);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!arrowExists && on)
        {
            world.setBlockMetadataWithNotify(x, y, z, type, 3);
            this.notifyArround(world, x, y, z);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (arrowExists)
        {
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world, x, y, z));
        }
    }
}
