package com.github.abrarsyed.secretroomsmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class PacketChangeOwnership extends PacketBase
{
    private boolean add; // if false, remove.
    private BlockLocation loc;
    
    public PacketChangeOwnership() {}
    
    public PacketChangeOwnership(boolean add, BlockLocation loc)
    {
        this.loc = loc;
        this.add = add;
    }

    @Override
    public void encode(ByteArrayDataOutput output)
    {
        output.writeBoolean(add);
        loc.writeToData(output, true);
    }

    @Override
    public void decode(ByteArrayDataInput input)
    {
        add = input.readBoolean();
        loc = BlockLocation.readFromData(input, true, 0);
    }

    @Override
    public void actionClient(World world, EntityPlayer player)
    {
        if (add) // add
        {
            OwnershipManager.setOwnership(player.getUniqueID(), loc);
        }
        else // remove
        {
            OwnershipManager.removeBlock(loc);
        }
    }

    @Override
    public void actionServer(World world, EntityPlayerMP player)
    {
        // nope.
    }

}
