package com.github.AbrarSyed.secretroomsmod.blocks;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;

import com.github.AbrarSyed.secretroomsmod.common.BlockHolder;
import com.github.AbrarSyed.secretroomsmod.common.FakeWorld;
import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.network.PacketCamo;
import com.github.AbrarSyed.secretroomsmod.network.PacketManager;

public class TileEntityCamo extends TileEntity
{
    private BlockHolder holder;
    public boolean[]    isCamo;

    public TileEntityCamo()
    {
        super();
        holder = null;
        isCamo = new boolean[6];
        Arrays.fill(isCamo, true);
    }

    @Override
    public boolean canUpdate()
    {
        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        holder = BlockHolder.buildFromNBT(nbt);

        for (int i = 0; i < isCamo.length; i++)
        {
            isCamo[i] = nbt.getBoolean("isCamo" + i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        holder.writeToNBT(nbt);
        // if this throws an NPE.. something has gone TERRIBLY wrong...

        for (int i = 0; i < isCamo.length; i++)
        {
            nbt.setBoolean("isCamo" + i, isCamo[i]);
        }
    }

    /**
     * signs and mobSpawners use this to send text and meta-data
     */
    @Override
    public Packet getDescriptionPacket()
    {
        return PacketManager.toMcPacket(new PacketCamo(this));
    }

    public void setBlockHolder(BlockHolder holder)
    {
        if (holder == null)
            return;
        FakeWorld fake = SecretRooms.proxy.getFakeWorld(worldObj);
        if (fake != null)
        {
            fake.addOverrideBlock(xCoord, yCoord, zCoord, holder);
        }

        this.holder = holder;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate()
    {
        FakeWorld fake = SecretRooms.proxy.getFakeWorld(worldObj);
        if (fake != null)
        {
            fake.removeOverrideBlock(xCoord, yCoord, zCoord);
        }
        super.invalidate();
    }

    @Override
    public void validate()
    {
        FakeWorld fake = SecretRooms.proxy.getFakeWorld(worldObj);
        if (fake != null)
        {
            fake.addOverrideBlock(xCoord, yCoord, zCoord, holder);
        }
        super.validate();
    }

    public BlockHolder getBlockHolder()
    {
        return holder;
    }

    public int getCopyID()
    {
        return holder == null ? 0 : holder.blockID;
    }

    public boolean isCamoSide(int side)
    {
        if (side >= 0 && side <= 5)
            return isCamo[side];
        else
            return false;
    }
}
