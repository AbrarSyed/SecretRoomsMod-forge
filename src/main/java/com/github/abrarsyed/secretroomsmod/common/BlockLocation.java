package com.github.abrarsyed.secretroomsmod.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.google.common.base.Throwables;

import net.minecraft.dispenser.ILocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockLocation implements ILocation
{
    private final int x, y, z, dimId;

    public BlockLocation(int x, int y, int z, World world)
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

    @Override
    public World getWorld()
    {
        return DimensionManager.getWorld(dimId);
    }
    
    public int getDimension()
    {
        return dimId;
    }

    @Override
    public double getX()
    {
        return x;
    }

    @Override
    public double getY()
    {
        return y;
    }

    @Override
    public double getZ()
    {
        return z;
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
        if (getClass() != obj.getClass())
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
            out.write(x);
            out.write(y);
            out.write(z);
            
            if (writeWorld)
            {
                out.write(dimId);
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
}
