package com.github.AbrarSyed.secretroomsmod.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamo;
import com.github.AbrarSyed.secretroomsmod.common.BlockHolder;
import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.network.PacketCamo;
import com.github.AbrarSyed.secretroomsmod.network.PacketManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */

public class ItemCamoDoor extends Item
{
    private Material doorMaterial;

    public ItemCamoDoor(Material par2Material)
    {
        super();
        doorMaterial = par2Material;
        setCreativeTab(SecretRooms.tab);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        if (doorMaterial.equals(Material.iron))
        {
            itemIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_ITEM_DOOR_STEEL);
        }
        else
        {
            itemIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_ITEM_DOOR_WOOD);
        }
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ)
    {
        if (side != 1)
            return false;

        Block block;
        y++;

        // change based on material.
        if (doorMaterial.equals(Material.iron))
        {
            block = SecretRooms.camoDoorIron;
        }
        else
        {
            block = SecretRooms.camoDoorWood;
        }

        // check permissions
        if (!player.canPlayerEdit(x, y, z, side, stack) || !player.canPlayerEdit(x, y + 1, z, side, stack))
            return false;

        if (!block.canPlaceBlockAt(world, x, y, z))
            return false;
        else
        {
            int i = MathHelper.floor_double((player.rotationYaw + 180F) * 4F / 360F - 0.5D) & 3;
            placeDoorBlock(world, x, y, z, i, block);
            stack.stackSize--;
            return true;
        }
    }

    public static void placeDoorBlock(World world, int x, int y, int z, int meta, Block block)
    {
        byte byte0 = 0;
        byte byte1 = 0;

        if (meta == 0)
        {
            byte1 = 1;
        }

        if (meta == 1)
        {
            byte0 = -1;
        }

        if (meta == 2)
        {
            byte1 = -1;
        }

        if (meta == 3)
        {
            byte0 = 1;
        }

        int i = (world.getBlock(x - byte0, y, z - byte1).isNormalCube() ? 1 : 0) + (world.getBlock(x - byte0, y + 1, z - byte1).isNormalCube() ? 1 : 0);
        int j = (world.isBlockNormalCubeDefault(x + byte0, y, z + byte1, true) ? 1 : 0) + (world.getBlock(x + byte0, y + 1, z + byte1).isNormalCube() ? 1 : 0);
        boolean flag = world.getBlock(x - byte0, y, z - byte1) == block || world.getBlock(x - byte0, y + 1, z - byte1) == block;
        boolean flag1 = world.getBlock(x + byte0, y, z + byte1) == block || world.getBlock(x + byte0, y + 1, z + byte1) == block;
        boolean flag2 = false;

        if (flag && !flag1)
        {
            flag2 = true;
        }
        else if (j > i)
        {
            flag2 = true;
        }

        world.setBlock(x, y, z, block, meta, 2);
        world.setBlock(x, y + 1, z, block, 8 | (flag2 ? 1 : 0), 2);

        if (world.getBlock(x, y - 1, z) == Blocks.grass)
        {
            world.setBlock(x, y - 1, z, Blocks.dirt);
        }

        BlockHolder holder = new BlockHolder(world, x, y - 1, z);

        TileEntityCamo te = (TileEntityCamo) world.getTileEntity(x, y, z);
        te.setBlockHolder(holder);
        
        if (world.isRemote)
        {
            PacketManager.sendToServer(new PacketCamo(te));
        }
        else
        {
            PacketManager.sendToDimension(new PacketCamo(te), world.provider.dimensionId);
        }

        te = (TileEntityCamo) world.getTileEntity(x, y + 1, z);
        te.setBlockHolder(holder);
        if (world.isRemote)
        {
            PacketManager.sendToServer(new PacketCamo(te));
        }
        else
        {
            PacketManager.sendToDimension(new PacketCamo(te), world.provider.dimensionId);
        }

        world.notifyBlocksOfNeighborChange(x, y, z, block);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, block);
    }
}
