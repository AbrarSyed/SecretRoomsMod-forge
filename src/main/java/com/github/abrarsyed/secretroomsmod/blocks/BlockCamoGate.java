package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoGate extends BlockCamoFull
{
    private static final int MAX_SIZE = 10;

    public BlockCamoGate()
    {
        super();
        setHardness(1.5F);
        setStepSound(Block.soundTypeWood);
    }
    
    

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
    {
        super.onBlockClicked(world, x, y, z, player);
        
        int metadata = world.getBlockMetadata(x, y, z);
        TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);
        
        // client
        if (world.isRemote)
            player.addChatComponentMessage(new ChatComponentText("CLIENT------------"));
        else
            player.addChatComponentMessage(new ChatComponentText("SERVER------------"));
        player.addChatComponentMessage(new ChatComponentText("BlockCamoGate"));
        player.addChatComponentMessage(new ChatComponentText("direction = "+metadata));
        player.addChatComponentMessage(new ChatComponentText("holder: "+ entity.getBlockHolder()));
    }



    @Override
    public void breakBlock(World world, int i, int j, int k, Block block, int metadata)
    {
        destroyGate(world, i, j, k, ForgeDirection.getOrientation(metadata & 7));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_GATE);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        boolean powerred = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
        boolean oldState = (world.getBlockMetadata(x, y, z) & 8) > 1;

        if (powerred != oldState)
        {
            world.scheduleBlockUpdate(x, y, z, this, 1);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        int metadata = setDefaultDirection(world, x, y, z, (EntityPlayer) entity);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
    }

    private static int setDefaultDirection(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        int l = MathHelper.floor_double(entityplayer.rotationYaw * 4F / 360F + 0.5D) & 3;
        double d = entityplayer.posY + 1.82D - entityplayer.yOffset;

        if (MathHelper.abs((float) entityplayer.posX - i) < 2.0F && MathHelper.abs((float) entityplayer.posZ - k) < 2.0F)
        {
            if (d - j > 2D)
                return 1;

            if (j - d > 0.0D)
                return 0;
        }

        switch(l)
        {
            case 0:
                return 2;
            case 1:
                return 5;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 0;
        }
        
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        if (world.isRemote)
            return;

        boolean isPowerred = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = ForgeDirection.getOrientation(meta & 7);
        
        if (isPowerred)
        {
            buildGate(world, x, y, z, dir);
        }
        else
        {
            destroyGate(world, x, y, z, dir);
        }
        
        // direction + the powerred bit.
        world.setBlockMetadataWithNotify(x, y, z, dir.ordinal() + (isPowerred ? 8 : 0), 2);
    }

    public void buildGate(World world, int x, int y, int z, ForgeDirection dir)
    {
        boolean stop = false;
        int xOffset, yOffset, zOffset;
        
        UUID owner = ((TileEntityCamo)world.getTileEntity(x, y, z)).getOwner();

        for (int i = 1; i <= MAX_SIZE && stop == false; i++)
        {
            xOffset = x + (dir.offsetX * i);
            yOffset = y + (dir.offsetY * i);
            zOffset = z + (dir.offsetZ * i);

            if (!world.isSideSolid(xOffset, yOffset, zOffset, dir.getOpposite()))
            {
                world.setBlockToAir(xOffset, yOffset, zOffset);
                world.setBlock(xOffset, yOffset, zOffset, SecretRooms.camoGateExt);
                
                // set owner
                ((TileEntityCamo)world.getTileEntity(xOffset, yOffset, zOffset)).setOwner(owner);
            }
            else
            {
                stop = true;
            }
        }
    }

    public void destroyGate(World world, int x, int y, int z, ForgeDirection dir)
    {
        int xOffset, yOffset, zOffset;

        for (int i = 1; i <= MAX_SIZE; i++)
        {
            xOffset = x + (dir.offsetX * i);
            yOffset = y + (dir.offsetY * i);
            zOffset = z + (dir.offsetZ * i);

            if (world.getBlock(xOffset, yOffset, zOffset) == SecretRooms.camoGateExt)
            {
                world.setBlockToAir(xOffset, yOffset, zOffset);
            }
        }
    }
}
