package com.wynprice.secretroomsmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketSyncEnergizedPaste;
import com.wynprice.secretroomsmod.proxy.CommonProxy;

import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
//TODO, remove SecretModelHelper. Try and use a ClassLoader to override:
/**
 * {@link FaceBakery.java:115}
 * @author Wyn Price
 *
 */
//As to save the vertex data and the BlockFaceUV into a simple list
@Mod(
		modid = SecretRooms5.MODID,
		name = SecretRooms5.MODNAME,
		version = SecretRooms5.VERSION,
		acceptedMinecraftVersions = "[1.12.2,1.13]",
		dependencies = "required-after:forge@[14.23.0.2502,)")
public class SecretRooms5
{
    public static final String MODID = "secretroomsmod";
    public static final String MODNAME = "Secret Rooms 5";
    public static final String VERSION = "5.2.5";
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
    
    @EventHandler
    public void onServerLoaded(FMLServerStartingEvent event)
    {
    	//Debugging command to clean if it gets too laggs
    	event.registerServerCommand(new CommandBase() {@Override public int getRequiredPermissionLevel(){return 2;}@Override public String getUsage(ICommandSender sender) {return "Resets the energized blocks";}@Override public String getName() {return "resetenergized";}@Override public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {EnergizedPasteHandler.getEnergized_map().clear();SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(EnergizedPasteHandler.saveToNBT(), null));}});
    }
}
