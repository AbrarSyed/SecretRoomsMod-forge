package com.github.abrarsyed.secretroomsmod.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.base.Throwables;

public class BlockLocation
{
    public final int x, y, z, dimId;

    public BlockLocation(World world, int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = world.provider.dimensionId;
    }

    public BlockLocation(int x, int y, int z, int dimid)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimId = dimid;
    }

    public World getWorld()
    {
        return DimensionManager.getWorld(dimId);
    }

    public void writeToNbt(NBTTagCompound nbt)
    {
        nbt.setInteger("x", x);
        nbt.setInteger("y", y);
        nbt.setInteger("z", z);
        nbt.setInteger("dim", dimId);
    }

    public static BlockLocation readFromNbt(NBTTagCompound nbt)
    {
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");
        int dim = nbt.getInteger("dim");
        return new BlockLocation(x, y, z, dim);
    }

    public void writeToData(DataOutput out, boolean writeWorld)
    {
        try
        {
            out.writeInt(x);
            out.writeInt(y);
            out.writeInt(z);

            if (writeWorld)
            {
                out.writeInt(dimId);
            }
        }
        catch (IOException e)
        {
            Throwables.propagate(e);
            e.printStackTrace();
        }
    }

    public static BlockLocation readFromData(DataInput in, boolean readWorld, int dim)
    {
        try
        {
            return new BlockLocation(in.readInt(), in.readInt(), in.readInt(), readWorld ? in.readInt() : dim);
        }
        catch (IOException e)
        {
            Throwables.propagate(e);
            return null;
        }
    }
    
    @Override
    public String toString()
    {
        return ""+x+", "+y+", "+z+", DIM"+dimId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + dimId;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        
        BlockLocation other = (BlockLocation) obj;
        
        if (dimId != other.dimId)
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }
}
