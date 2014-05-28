package com.github.abrarsyed.secretroomsmod.client;

import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;

public class ClientBlockLocation extends BlockLocation
{
    private final World world;

    public ClientBlockLocation(World world, int x, int y, int z)
    {
        super(world, x, y, z);
        this.world = world;
    }
    
    public World getWorld()
    {
        return world;
    }
}
