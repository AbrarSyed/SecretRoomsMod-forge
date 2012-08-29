package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Map;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;


public class BlockCamoTrapDoor extends Block
{
    protected BlockCamoTrapDoor(int par1)
    {
        super(par1, Material.wood);
        blockIndexInTexture = 0;
        
        this.disableStats();
        this.setRequiresSelfNotify();
        this.setHardness(3.0F);
        this.setStepSound(soundWoodFootstep);
        
        float f = 0.5F;
        float f1 = 1.0F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public void addCreativeItems(ArrayList itemList)
    {
    	itemList.add(new ItemStack(this));
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
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
    {
        // modify coordinates to get hinge Block.
        int i = world.getBlockMetadata(x, y, z);
        int j = x;
        int k = z;

        if ((i & 3) == 0)
        {
            k++;
        }

        if ((i & 3) == 1)
        {
            k--;
        }

        if ((i & 3) == 2)
        {
            j++;
        }

        if ((i & 3) == 3)
        {
            j--;
        }
        
        // actually get the texture.
        Block block = Block.blocksList[world.getBlockId(j, y, k)];
        
        int texture = block.getBlockTexture(world, j, y, k, dir);
        
        if (texture == 38 || texture == 3)
        	return 1;
        else
        	return texture;
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
        float f = 0.1875F;
        setBlockBounds(0.0F, 1-f, 0.0F, 1.0F, 1F, 1.0F);

        if (isTrapdoorOpen(par1))
        {
            if ((par1 & 3) == 0)
            {
                setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
            }

            if ((par1 & 3) == 1)
            {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
            }

            if ((par1 & 3) == 2)
            {
                setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if ((par1 & 3) == 3)
            {
                setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        this.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, 0, 0, 0,0);
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityplayer, int something1, float something2, float something3, float something4)
    {
        if (blockMaterial == Material.iron)
        {
            return true;
        }
        else
        {
            int i = world.getBlockMetadata(x, y, z);
            world.setBlockMetadataWithNotify(x, y, z, i ^ 4);
            world.playAuxSFXAtEntity(entityplayer, 1003, x, y, z, 0);
            return true;
        }
    }

    public void onPoweredBlockChange(World par1World, int par2, int par3, int par4, boolean par5)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 4) > 0;

        if (flag == par5)
        {
            return;
        }
        else
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i ^ 4);
            par1World.playAuxSFXAtEntity(null, 1003, par2, par3, par4, 0);
            return;
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par1World.isRemote)
        {
            return;
        }

        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = par2;
        int k = par4;

        if ((i & 3) == 0)
        {
            k++;
        }

        if ((i & 3) == 1)
        {
            k--;
        }

        if ((i & 3) == 2)
        {
            j++;
        }

        if ((i & 3) == 3)
        {
            j--;
        }

        if (!isValidSupportBlock(par1World.getBlockId(j, par3, k)))
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            dropBlockAsItem(par1World, par2, par3, par4, i, 0);
        }

        boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);

        if (flag || par5 > 0 && Block.blocksList[par5].canProvidePower() || par5 == 0)
        {
            onPoweredBlockChange(par1World, par2, par3, par4, flag);
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
    public void updateBlockMetadata(World world, int par2, int par3, int par4, int par5, float something1, float something2, float something3)
    {
    	super.updateBlockMetadata(world, par2, par3, par4, par5, something1, something2, something3);
    	
        byte byte0 = 0;

        if (par5 == 2)
        {
            byte0 = 0;
        }

        if (par5 == 3)
        {
            byte0 = 1;
        }

        if (par5 == 4)
        {
            byte0 = 2;
        }

        if (par5 == 5)
        {
            byte0 = 3;
        }

        world.setBlockMetadataWithNotify(par2, par3, par4, byte0);
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 == 0)
        {
            return false;
        }

        if (par5 == 1)
        {
            return false;
        }

        if (par5 == 2)
        {
            par4++;
        }

        if (par5 == 3)
        {
            par4--;
        }

        if (par5 == 4)
        {
            par2++;
        }

        if (par5 == 5)
        {
            par2--;
        }

        return isValidSupportBlock(par1World.getBlockId(par2, par3, par4));
    }

    public static boolean isTrapdoorOpen(int par0)
    {
        return (par0 & 4) != 0;
    }

    /**
     * Checks if the block ID is a valid support block for the trap door to connect with. If it is not the trapdoor is
     * dropped into the world.
     */
    private static boolean isValidSupportBlock(int par0)
    {
        if (par0 <= 0)
        {
            return false;
        }
        else
        {
            Block block = Block.blocksList[par0];
            return block != null && block.blockMaterial.isOpaque() && block.renderAsNormalBlock() || block == Block.glowStone;
        }
    }

}
