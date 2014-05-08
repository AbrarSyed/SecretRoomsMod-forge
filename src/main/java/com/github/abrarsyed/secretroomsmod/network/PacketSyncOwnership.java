package com.github.abrarsyed.secretroomsmod.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class PacketSyncOwnership extends PacketBase
{
    int dimension;
    boolean clear = false;
    Collection<BlockLocation> locations;
    
    public PacketSyncOwnership() {}
    
    public PacketSyncOwnership(int dimension, Collection<BlockLocation> locations)
    {
        this.dimension = dimension;
        this.locations = locations;
    }
    
    public PacketSyncOwnership(int dimension)
    {
        this.dimension = dimension;
        this.locations = null;
        clear = true;
    }

    @Override
    public void encode(ByteArrayDataOutput output)
    {
        output.writeInt(dimension);

        if (clear)
        {
            output.writeInt(-1);
        }
        {
            output.writeInt(locations.size());
            for (BlockLocation loc : locations)
            {
                loc.writeToData(output, false);
            }
        }
    }

    @Override
    public void decode(ByteArrayDataInput input)
    {
        dimension = input.readInt();
        int size = input.readInt();
        if (size == -1)
        {
            clear = true;
            return;
        }
        else
        {
            locations = new ArrayList<BlockLocation>(size);
            for (int i = 0; i < size; i++)
            {
                locations.add(BlockLocation.readFromData(input, false, dimension));
            }
        }
    }

    @Override
    public void actionClient(World world, EntityPlayer player)
    {
        if (clear)
        {
            OwnershipManager.clearDimension(dimension);
            return;
        }
        
        Map<BlockLocation, UUID> map = OwnershipManager.getOwnershipMap(dimension);
        for (BlockLocation loc : locations)
        {
            map.put(loc, player.getUniqueID());
        }
    }

    @Override
    public void actionServer(World world, EntityPlayerMP player)
    {
        // nope.
    }

}
