package com.wynprice.secretroomsmod.proxy;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.handler.GuiHandler;
import com.wynprice.secretroomsmod.handler.ParticleHandler;
import com.wynprice.secretroomsmod.handler.ServerRecievePacketHandler;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretChest;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretDispenser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	public void preInit(FMLPreInitializationEvent event) 
    {  
		SecretItems.preInit();
		
		SecretBlocks.preInit();
				
		SecretNetwork.preInit();
    }
	
	public void init(FMLInitializationEvent event) 
    {
		Class[] tileEntityClasses = {
    			TileEntityInfomationHolder.class,
    			TileEntitySecretChest.class,
    			TileEntitySecretDispenser.class
    	};
    	for(Class clas : tileEntityClasses)
    		GameRegistry.registerTileEntity(clas, SecretRooms2.MODID + clas.getSimpleName());
    	
    	Object[] handlers = {
    			new ParticleHandler(),
    			new ServerRecievePacketHandler()
    	};
    	for(Object o : handlers)
    		MinecraftForge.EVENT_BUS.register(o);

    	NetworkRegistry.INSTANCE.registerGuiHandler(SecretRooms2.instance, new GuiHandler());
    	
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.wynprice.secretroomsmod.intergration.TheOneProbeSupport");


    }
	
	public void postInit(FMLPostInitializationEvent event) 
    {
		
    }
	
	//used for clients to get an instance of the player that wont crash the game
	public EntityPlayer getPlayer() 
	{
		return null;
	}
}
