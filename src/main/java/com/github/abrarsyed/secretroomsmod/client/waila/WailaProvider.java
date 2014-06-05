package com.github.abrarsyed.secretroomsmod.client.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.github.abrarsyed.secretroomsmod.blocks.BlockCamoFull;
import com.github.abrarsyed.secretroomsmod.blocks.BlockSolidAir;
import com.github.abrarsyed.secretroomsmod.blocks.BlockTorchLever;
import com.github.abrarsyed.secretroomsmod.client.ClientBlockLocation;
import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

public class WailaProvider implements IWailaDataProvider
{
    
    public static void register(IWailaRegistrar registrar)
    {
        WailaProvider provider = new WailaProvider();
        
        registrar.registerStackProvider(provider, BlockSolidAir.class);
        
        registrar.registerBodyProvider(provider, BlockCamoFull.class);
//        registrar.registerStackProvider(provider, BlockCamoFull.class);
        registrar.registerBodyProvider(provider, BlockTorchLever.class);
//        registrar.registerStackProvider(provider, BlockCamoDoor.class);
//        registrar.registerStackProvider(provider, BlockCamoTrapDoor.class);
    }
    
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler configHandler)
    {
        MovingObjectPosition pos = accessor.getPosition();
        BlockLocation loc = new ClientBlockLocation(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ);
        boolean owner = OwnershipManager.isOwner(accessor.getPlayer().getUniqueID(), loc);
        
        Block block = accessor.getBlock();
        if (block == SecretRooms.solidAir)
        {
            if (owner)
            {
                return new ItemStack(SecretRooms.solidAir);
            }
            else
            {
                return new ItemStack(Blocks.air);
            }
        }
        
        return null;
    }
    
    
    @Override
    public List<String> getWailaBody(ItemStack stack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        MovingObjectPosition pos = accessor.getPosition();
        BlockLocation loc = new ClientBlockLocation(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ);
        boolean owner = OwnershipManager.isOwner(accessor.getPlayer().getUniqueID(), loc);
        
        Block block = accessor.getBlock();
        
//        if (!owner)
//            return list;
        
        if (block instanceof BlockCamoFull)
        {
            ((BlockCamoFull)block).addWailaBody(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ, list);
        }
        else if (block instanceof BlockTorchLever)
        {
            ((BlockCamoFull)block).addWailaBody(accessor.getWorld(), pos.blockX, pos.blockY, pos.blockZ, list);
        }
        
        return list;
    }

    @Override
    public List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) { return paramList; }

    @Override
    public List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) { return paramList; }
}
