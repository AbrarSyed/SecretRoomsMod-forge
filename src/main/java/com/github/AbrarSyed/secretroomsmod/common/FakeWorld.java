package com.github.AbrarSyed.secretroomsmod.common;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class FakeWorld implements IBlockAccess
{
    private final World                               backend;
    private final HashMap<ChunkPosition, BlockHolder> overrideMap;

    public FakeWorld(World toCopy)
    {
        this.backend = toCopy;
        overrideMap = new HashMap<ChunkPosition, BlockHolder>();
    }

    // actual stuff...

    public void addOverrideBlock(int x, int y, int z, BlockHolder holder)
    {
        ChunkPosition position = new ChunkPosition(x, y, z);
        overrideMap.put(position, holder);
    }

    public void removeOverrideBlock(int x, int y, int z)
    {
        ChunkPosition position = new ChunkPosition(x, y, z);
        overrideMap.remove(position);
    }

    // overrides...

    @Override
    public Block getBlock(int x, int y, int z)
    {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (overrideMap.containsKey(pos))
        {
            return overrideMap.get(pos).getBlock();
        }
        else
        {
            return backend.getBlock(x, y, z);
        }
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (overrideMap.containsKey(pos))
        {
            return overrideMap.get(pos).getTileEntity(backend, x, y, z);
        }
        else
        {
            return backend.getTileEntity(x, y, z);
        }
    }

    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (overrideMap.containsKey(pos))
        {
            return overrideMap.get(pos).metadata;
        }
        else
        {
            return backend.getBlockMetadata(x, y, z);
        }
    }

    @Override
    public boolean isAirBlock(int x, int y, int z)
    {
        ChunkPosition pos = new ChunkPosition(x, y, z);
        if (overrideMap.containsKey(pos))
        {
            return overrideMap.get(pos).getBlock().isAir(backend, x, y, z);
        }
        else
        {
            return backend.isAirBlock(x, y, z);
        }
    }
    
    // unnecessary overrides
    
    @Override
    public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4)
    {
        return backend.getLightBrightnessForSkyBlocks(var1, var2, var3, var4);
    }


    @Override
    public BiomeGenBase getBiomeGenForCoords(int var1, int var2)
    {
        return backend.getBiomeGenForCoords(var1, var2);
    }

    @Override
    public int getHeight()
    {
        return backend.getHeight();
    }

    @Override
    public boolean extendedLevelsInChunkCache()
    {
        return backend.extendedLevelsInChunkCache();
    }

    @Override
    public Vec3Pool getWorldVec3Pool()
    {
        return backend.getWorldVec3Pool();
    }

    @Override
    public int isBlockProvidingPowerTo(int var1, int var2, int var3, int var4)
    {
        return backend.isBlockProvidingPowerTo(var1, var2, var3, var4);
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
    {
        return backend.isSideSolid(x, y, z, side, _default);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((backend == null) ? 0 : backend.hashCode());
        result = prime * result + ((overrideMap == null) ? 0 : overrideMap.hashCode());
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
        FakeWorld other = (FakeWorld) obj;
        if (backend == null)
        {
            if (other.backend != null)
                return false;
        }
        else if (!backend.equals(other.backend))
            return false;
        if (overrideMap == null)
        {
            if (other.overrideMap != null)
                return false;
        }
        else if (!overrideMap.equals(other.overrideMap))
            return false;
        return true;
    }
}
