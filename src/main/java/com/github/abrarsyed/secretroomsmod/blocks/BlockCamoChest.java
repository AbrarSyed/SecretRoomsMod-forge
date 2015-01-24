package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Alexbegt, AbrarSyed
 */
public class BlockCamoChest extends BlockCamoFull
{
    private Random       random = new Random();
    public final boolean isTrapped;

    public BlockCamoChest(boolean trapped)
    {
        super();
        isTrapped = trapped;
        setHardness(1.5F);
        setCreativeTab(SecretRooms.tab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_CHEST);
    }

    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, Block block, int par6)
    {
        TileEntityCamoChest var7 = (TileEntityCamoChest) par1World.getTileEntity(par2, par3, par4);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = random.nextFloat() * 0.8F + 0.1F;
                    float var11 = random.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
                    {
                        int var13 = random.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
                        float var15 = 0.05F;
                        var14.motionX = (float) random.nextGaussian() * var15;
                        var14.motionY = (float) random.nextGaussian() * var15 + 0.2F;
                        var14.motionZ = (float) random.nextGaussian() * var15;

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound) var9.getTagCompound().copy());
                        }
                    }
                }
            }
        }

        super.breakBlock(par1World, par2, par3, par4, block, par6);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            IInventory iinventory = this.getInventory(par1World, par2, par3, par4);

            if (iinventory != null)
            {
                par5EntityPlayer.displayGUIChest(iinventory);
            }

            return true;
        }
    }

    /**
     * each class overrdies this to return a new <className>
     */
    @Override
    public TileEntity createNewTileEntity(World par1World, int i)
    {
        return new TileEntityCamoChest();
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    @Override
    public boolean canProvidePower()
    {
        return isTrapped;
    }

    /**
     * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
     * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
     * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!canProvidePower())
            return 0;
        else
        {
            int i1 = ((TileEntityCamoChest) par1IBlockAccess.getTileEntity(par2, par3, par4)).numUsingPlayers;
            return MathHelper.clamp_int(i1, 0, 15);
            //return i1 > 0 ? 15 : 0;
        }
    }

    /**
     * Returns true if the block is emitting direct/strong redstone power on the specified side. Args: World, X, Y, Z,
     * side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
     */
    @Override
    public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return isProvidingWeakPower(par1IBlockAccess, par2, par3, par4, par5);
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    @Override
    public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5)
    {
        return Container.calcRedstoneFromInventory(this.getInventory(par1World, par2, par3, par4));
    }

    /**
     * Looks for a sitting ocelot within certain bounds. Such an ocelot is considered to be blocking access to the
     * chest.
     * @param par0World 
     * @param par1 
     * @param par2 
     * @param par3 
     * @return if an ocelot is blocking a chest
     */
    @SuppressWarnings("rawtypes")
    public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3)
    {
        Iterator iterator = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1)).iterator();
        EntityOcelot entityocelot;

        do
        {
            if (!iterator.hasNext())
                return false;

            EntityOcelot entityocelot1 = (EntityOcelot) iterator.next();
            entityocelot = entityocelot1;
        } while (!entityocelot.isSitting());

        return true;
    }

    /**
     * Gets the inventory of the chest at the specified coords, accounting for blocks or ocelots on top of the chest,
     * and double chests.
     * @param par1World 
     * @param par2 
     * @param par3 
     * @param par4 
     * @return NULL if the inventory cannot be openned
     */
    public IInventory getInventory(World par1World, int par2, int par3, int par4)
    {
        IInventory object = (TileEntityCamoChest) par1World.getTileEntity(par2, par3, par4);

        if (object == null)
        {
            return null;
        }
        else if (isOcelotBlockingChest(par1World, par2, par3, par4))
        {
            return null;
        }
        else
        {
            return (IInventory) object;
        }
    }

}
