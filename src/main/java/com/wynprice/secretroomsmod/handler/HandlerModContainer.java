package com.wynprice.secretroomsmod.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class HandlerModContainer 
{
	public static Map<String, ModContainer> cache = new HashMap<>();

    public static void init() {
        cache.put("minecraft", Loader.instance().getMinecraftModContainer());
        cache.put("forge", ForgeModContainer.getInstance());
    }
    
    public static ModContainer getContainer(String modID) {
    	
    	if(cache.containsKey(modID)) {
    		return cache.get(modID);
    	}

        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getModId().equalsIgnoreCase(modID)) {
                cache.put(modID, mod);
                return mod;
            }
        }

        return Loader.instance().getMinecraftModContainer();
    }
}
