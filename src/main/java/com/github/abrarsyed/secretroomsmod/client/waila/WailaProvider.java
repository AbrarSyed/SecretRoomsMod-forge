package com.github.abrarsyed.secretroomsmod.client.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.github.abrarsyed.secretroomsmod.blocks.BlockCamoButton;
import com.github.abrarsyed.secretroomsmod.blocks.BlockCamoLever;
import com.github.abrarsyed.secretroomsmod.blocks.BlockCamoWire;
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
        registrar.registerStackProvider(provider, BlockTorchLever.class);

        registrar.registerBodyProvider(provider, BlockCamoWire.class);
        registrar.registerBodyProvider(provider, BlockCamoButton.class);
        registrar.registerBodyProvider(provider, BlockCamoLever.class);
        registrar.registerBodyProvider(provider, BlockTorchLever.class);
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
        else if (block == SecretRooms.torchLever)
        {
            if (owner)
            {
                return new ItemStack(SecretRooms.torchLever);
            }
            else
            {
                return new ItemStack(Blocks.torch);
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
        
        if (!owner)
            return list;
        
        if (block instanceof BlockTorchLever || block instanceof BlockCamoLever || block instanceof BlockCamoButton)
        {
            boolean off = (accessor.getMetadata() & 8) == 0;
            if (block instanceof BlockCamoLever)
            {
                off = accessor.getMetadata() == 0;
            }
            
            list.add(LangUtil.translateG("hud.msg.state") + " : " + LangUtil.translateG(off ? "hud.msg.off" : "hud.msg.on"));
        }
        else if (block instanceof BlockCamoWire)
        {
            list.add(String.format(LangUtil.translateG("hud.msg.power") + " : " + accessor.getMetadata()));
        }
        
        return list;
    }

    @Override
    public List<String> getWailaHead(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) { return paramList; }

    @Override
    public List<String> getWailaTail(ItemStack paramItemStack, List<String> paramList, IWailaDataAccessor paramIWailaDataAccessor, IWailaConfigHandler paramIWailaConfigHandler) { return paramList; }
}
