package com.wynprice.secretroomsmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wynprice.secretroomsmod.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
		modid = SecretRooms5.MODID,
		name = SecretRooms5.MODNAME,
		version = SecretRooms5.VERSION,
		acceptedMinecraftVersions = "1.11.2")
public class SecretRooms5
{
    public static final String MODID = "secretroomsmod";
    public static final String MODNAME = "Secret Rooms 5";
    public static final String VERSION = "5.1.9";
    public static final String UPDATE_URL = "http://www.wynprice.com/update_jsons/secretroomsmod.json";
	
    
    @SidedProxy(modId = MODID, clientSide = "com.wynprice.secretroomsmod.proxy.ClientProxy", serverSide = "com.wynprice.secretroomsmod.proxy.ServerProxy")
    public static CommonProxy proxy;
    
    @Instance(MODID)
    public static SecretRooms5 instance;
    public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(SecretItems.CAMOUFLAGE_PASTE);
		}
	};
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
    @EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    }
}
