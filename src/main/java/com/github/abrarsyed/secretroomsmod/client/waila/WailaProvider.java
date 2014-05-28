package com.github.abrarsyed.secretroomsmod.client.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.github.abrarsyed.secretroomsmod.blocks.BlockCamoTrapDoor;
import com.github.abrarsyed.secretroomsmod.blocks.BlockSolidAir;
import com.github.abrarsyed.secretroomsmod.blocks.BlockTorchLever;
import com.github.abrarsyed.secretroomsmod.blocks.TileEntityCamo;
import com.github.abrarsyed.secretroomsmod.client.ClientBlockLocation;
import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;

public class WailaProvider implements IWailaDataProvider
{
    
    public static void register(IWailaRegistrar registrar)
    {
        WailaProvider provider = new WailaProvider();
        registrar.registerStackProvider(provider, TileEntityCamo.class);
        registrar.registerStackProvider(provider, BlockTorchLever.class);
        registrar.registerStackProvider(provider, BlockCamoTrapDoor.class);
        registrar.registerStackProvider(provider, BlockSolidAir.class);
    }
    
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler configHandler)
    {
        MovingObjectPosition pos = accessor.getPosition();
        BlockLocation loc = new ClientBlockLocation(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ);
        boolean owner = OwnershipManager.isOwner(accessor.getPlayer().getUniqueID(), loc);
        
        if (owner)
            return new ItemStack(accessor.getBlock());
        else
        {
            // get copy.
        }
        
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public List<String> getWailaBody(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler)
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
