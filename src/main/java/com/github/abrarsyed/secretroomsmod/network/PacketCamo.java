package com.github.abrarsyed.secretroomsmod.network;

import java.io.IOException;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.api.BlockHolder;
import com.github.abrarsyed.secretroomsmod.api.ITileEntityCamo;
import com.github.abrarsyed.secretroomsmod.blocks.TileEntityCamo;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketCamo extends PacketBase
{
    public int         x, y, z;
    public BlockHolder holder;
    public boolean[]   sides = new boolean[6];
    public UUID        owner;

    public PacketCamo()
    {
    }

    public PacketCamo(ITileEntityCamo entity)
    {
        holder = entity.getBlockHolder();
        x = entity.getXCoord();
        y = entity.getYCoord();
        z = entity.getZCoord();

        sides = entity.getIsCamo().clone();
        owner = entity.getOwner();

        if (holder == null)
            throw new IllegalArgumentException("TileEntity data is NULL!");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player)
    {
        if (world == null)
            return;

        ITileEntityCamo entity = (ITileEntityCamo) world.getTileEntity(x, y, z);

        if (entity == null || holder == null)
            return;

        entity.setBlockHolder(holder);
        entity.setIsCamo(sides);
        entity.setOwner(owner);

        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public void actionServer(World world, EntityPlayerMP player)
    {
        if (world == null)
            return;

        TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);

        if (entity == null || holder == null)
            return;

        entity.setBlockHolder(holder);
        entity.setIsCamo(sides);
        entity.setOwner(owner);

        PacketManager.sendToDimension(this, world.provider.dimensionId);
    }

    @Override
    public void encode(ByteArrayDataOutput output)
    {
        output.writeInt(x);
        output.writeInt(y);
        output.writeInt(z);

        try
        {
            NBTTagCompound nbt = new NBTTagCompound();
            holder.writeToNBT(nbt);
            CompressedStreamTools.write(nbt, output);
        }
        catch (IOException e)
        {
            // wont happen
        }

        for (int i = 0; i < 6; i++)
        {
            output.writeBoolean(sides[i]);
        }
        
        output.writeBoolean(owner != null);
        if (owner != null)
        {
            output.writeLong(owner.getMostSignificantBits());
            output.writeLong(owner.getLeastSignificantBits());
        }
    }

    @Override
    public void decode(ByteArrayDataInput input)
    {
        x = input.readInt();
        y = input.readInt();
        z = input.readInt();

        try
        {
            NBTTagCompound nbt = CompressedStreamTools.func_152456_a(input, NBTSizeTracker.field_152451_a);
            holder = BlockHolder.buildFromNBT(nbt);
        }
        catch (IOException e)
        {
            // wont happen
        }

        for (int i = 0; i < 6; i++)
        {
            sides[i] = input.readBoolean();
        }
        
        if (input.readBoolean())
        {
            owner = new UUID(input.readLong(), input.readLong());
        }
    }
}
