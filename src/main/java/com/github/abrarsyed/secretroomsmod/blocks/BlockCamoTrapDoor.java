package com.github.abrarsyed.secretroomsmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockCamoTrapDoor extends Block
{
    public BlockCamoTrapDoor()
    {
        super(Material.wood);

        disableStats();
        setHardness(3.0F);
        setCreativeTab(SecretRooms.tab);
        setStepSound(soundTypeWood);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BASE);
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        
        if (entity instanceof EntityPlayer)
        {
            OwnershipManager.setOwnership(entity.getUniqueID(), new BlockLocation(world, x, y, z));
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        super.breakBlock(world, x, y, z, block, metadata);
        
        // remove ownership
        OwnershipManager.removeBlock(new BlockLocation(world, x, y, z));
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return !isTrapdoorOpen(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 0;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int dir)
    {
        if (!SecretRooms.displayCamo && SecretRooms.proxy.isOwner(world, x, y, z))
        {
            return blockIcon;
        }
        
        // modify coordinates to get hinge Block.
        int i = world.getBlockMetadata(x, y, z);
        int j = x;
        int k = z;

        switch(i & 3)
        {
            case 0:
                k++;
                break;
            case 1:
                k--;
                break;
            case 2:
                j++;
                break;
            case 3:
                j--;
                break;
        }

        // actually get the texture.
        Block block = world.getBlock(j, y, k);

        if (block == null)
            return blockIcon;

        return block.getIcon(world, j, y, k, dir);
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        if (!SecretRooms.displayCamo && SecretRooms.proxy.isOwner(world, x, y, z))
            return super.colorMultiplier(world, x, y, z);

        if (Blocks.grass.getBlockTextureFromSide(1).equals(getIcon(world, x, y, z, 1)))
        {
            int red = 0;
            int green = 0;
            int blue = 0;

            for (int offsetX = -1; offsetX <= 1; ++offsetX)
            {
                for (int offsetZ = -1; offsetZ <= 1; ++offsetZ)
                {
                    int color = world.getBiomeGenForCoords(x + offsetZ, z + offsetX).getBiomeGrassColor(x + offsetZ, y, z + offsetX);
                    red += (color & 16711680) >> 16;
                    green += (color & 65280) >> 8;
                    blue += color & 255;
                }
            }

            return (red / 9 & 0xff) << 16 | (green / 9 & 0xff) << 8 | blue / 9 & 0xff;
        }

        return super.colorMultiplier(world, x, y, z); // white
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        setBlockBoundsForBlockRender(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    @Override
    public void setBlockBoundsForItemRender()
    {
        float f = 0.1875F;
        setBlockBounds(0.0F, 0.5F - f / 2.0F, 0.0F, 1.0F, 0.5F + f / 2.0F, 1.0F);
    }

    public void setBlockBoundsForBlockRender(int par1)
    {
        float var2 = 0.1875F;

        if ((par1 & 8) != 0)
        {
            setBlockBounds(0.0F, 1.0F - var2, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var2, 1.0F);
        }

        if (isTrapdoorOpen(par1))
        {
            if ((par1 & 3) == 0)
            {
                setBlockBounds(0.0F, 0.0F, 1.0F - var2, 1.0F, 1.0F, 1.0F);
            }

            if ((par1 & 3) == 1)
            {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var2);
            }

            if ((par1 & 3) == 2)
            {
                setBlockBounds(1.0F - var2, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if ((par1 & 3) == 3)
            {
                setBlockBounds(0.0F, 0.0F, 0.0F, var2, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
    {
        int i = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, i ^ 4, 2);
        world.playAuxSFXAtEntity(entityplayer, 1003, x, y, z, 0);
        return true;
    }

    public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 4) > 0;

        if (flag == par5)
            return;
        else
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i ^ 4, 2);
            par1World.playAuxSFXAtEntity(null, 1003, par2, par3, par4, 0);
            return;
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5)
    {
        if (!par1World.isRemote)
        {
            int i1 = par1World.getBlockMetadata(par2, par3, par4);
            int j1 = par2;
            int k1 = par4;

            if ((i1 & 3) == 0)
            {
                k1 = par4 + 1;
            }

            if ((i1 & 3) == 1)
            {
                --k1;
            }

            if ((i1 & 3) == 2)
            {
                j1 = par2 + 1;
            }

            if ((i1 & 3) == 3)
            {
                --j1;
            }

            if (!(isValidSupportBlock(par1World.getBlock(j1, par3, k1)) || par1World.isSideSolid(j1, par3, k1, ForgeDirection.getOrientation((i1 & 3) + 2))))
            {
                par1World.setBlockToAir(par2, par3, par4);
                dropBlockAsItem(par1World, par2, par3, par4, i1, 0);
            }

            boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);

            if (flag || ( par5 != null && par5.canProvidePower()))
            {
                onPoweredBlockChange(par1World, par2, par3, par4, flag);
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    @Override
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3D, Vec3 par6Vec3D)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3D, par6Vec3D);
    }

    /**
     * Called when a block is placed using an item. Used often for taking the facing and figuring out how to position
     * the item. Args: x, y, z, facing
     */
    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ, int currentMeta)
    {
        int newMeta = 0;

        if (side == 2)
        {
            newMeta = 0;
        }

        if (side == 3)
        {
            newMeta = 1;
        }

        if (side == 4)
        {
            newMeta = 2;
        }

        if (side == 5)
        {
            newMeta = 3;
        }

        if (side != 1 && side != 0 && clickY > 0.5F)
        {
            newMeta |= 8;
        }

        return newMeta;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    @Override
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int meta)
    {
        if (meta == 0)
            return false;
        else if (meta == 1)
            return false;
        else
        {
            if (meta == 2)
            {
                ++z;
            }

            if (meta == 3)
            {
                --z;
            }

            if (meta == 4)
            {
                ++x;
            }

            if (meta == 5)
            {
                --x;
            }

            return isValidSupportBlock(world.getBlock(x, y, z)) || world.isSideSolid(x, y, z, ForgeDirection.UP);
        }
    }

    public static boolean isTrapdoorOpen(int par0)
    {
        return (par0 & 4) != 0;
    }

    /**
     * Checks if the block ID is a valid support block for the trap door to connect with. If it is not the trapdoor is
     * dropped into the world.
     */
    private static boolean isValidSupportBlock(Block block)
    {
        if (block == null)
            return false;
        else
        {
            return block.getMaterial().isOpaque() && block.renderAsNormalBlock() || block == Blocks.glowstone || block instanceof BlockSlab || block instanceof BlockStairs;
        }
    }
    
    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        if (OwnershipManager.isOwner(player.getPersistentID(), new BlockLocation(world, x, y, z)))
        {
            return new ItemStack(this);
        }

        // modify coordinates to get hinge Block.
        int i = world.getBlockMetadata(x, y, z);
        int j = x;
        int k = z;

        switch(i & 3)
        {
            case 0:
                k++;
                break;
            case 1:
                k--;
                break;
            case 2:
                j++;
                break;
            case 3:
                j--;
                break;
        }

        // actually get the texture.
        Block block = world.getBlock(j, y, k);
        
        if (block == null)
            return null;

        // actually get the texture.
        return block.getPickBlock(target, world, x, y, z, player);
    }
    
    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        if (SecretRooms.proxy.isOwner(world, x, y, z))
        {
            return new ItemStack(this);
        }
        
        // modify coordinates to get hinge Block.
        int i = world.getBlockMetadata(x, y, z);
        int j = x;
        int k = z;

        switch(i & 3)
        {
            case 0:
                k++;
                break;
            case 1:
                k--;
                break;
            case 2:
                j++;
                break;
            case 3:
                j--;
                break;
        }

        // actually get the texture.
        Block block = world.getBlock(j, y, k);
        
        if (block == null)
            return null;

        // actually get the texture.
        return block.getPickBlock(target, world, x, y, z);
    }

}
