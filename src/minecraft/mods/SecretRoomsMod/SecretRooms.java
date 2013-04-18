package mods.SecretRoomsMod;

import java.util.logging.Logger;

import mods.SecretRoomsMod.blocks.BlockCamoButton;
import mods.SecretRoomsMod.blocks.BlockCamoChest;
import mods.SecretRoomsMod.blocks.BlockCamoDoor;
import mods.SecretRoomsMod.blocks.BlockCamoGate;
import mods.SecretRoomsMod.blocks.BlockCamoGateExt;
import mods.SecretRoomsMod.blocks.BlockCamoGhost;
import mods.SecretRoomsMod.blocks.BlockCamoLever;
import mods.SecretRoomsMod.blocks.BlockCamoPlate;
import mods.SecretRoomsMod.blocks.BlockCamoStair;
import mods.SecretRoomsMod.blocks.BlockCamoTrapDoor;
import mods.SecretRoomsMod.blocks.BlockCamoWire;
import mods.SecretRoomsMod.blocks.BlockOneWay;
import mods.SecretRoomsMod.blocks.BlockTorchLever;
import mods.SecretRoomsMod.blocks.TileEntityCamoChest;
import mods.SecretRoomsMod.blocks.TileEntityFull;
import mods.SecretRoomsMod.client.CreativeTabCamo;
import mods.SecretRoomsMod.common.CommandShow;
import mods.SecretRoomsMod.common.ProxyCommon;
import mods.SecretRoomsMod.items.ItemCamoDoor;
import mods.SecretRoomsMod.items.ItemCamoPaste;
import mods.SecretRoomsMod.network.HandlerClient;
import mods.SecretRoomsMod.network.HandlerServer;
import mods.SecretRoomsMod.network.PacketSRMBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * @author AbrarSyed
 */
@NetworkMod(clientSideRequired = true, serverSideRequired = true, versionBounds = "[4.6,)",
		clientPacketHandlerSpec = @SidedPacketHandler(channels = { PacketSRMBase.CHANNEL }, packetHandler = HandlerClient.class),
		serverPacketHandlerSpec = @SidedPacketHandler(channels = { PacketSRMBase.CHANNEL }, packetHandler = HandlerServer.class))
@Mod(modid = SecretRooms.MODID, name = "SecretRoomsMod", version = "4.6.0")
public class SecretRooms
{

	@SidedProxy(clientSide = "mods.SecretRoomsMod.client.ProxyClient", serverSide = "mods.SecretRoomsMod.common.ProxyCommon")
	public static ProxyCommon	proxy;

	public static final String	MODID					= "SecretRoomsMod";
	
	@Instance(value = MODID)
	public static SecretRooms	instance;

	public static Logger		logger;

	// textures
	public static final String	TEXTURE_ITEM_PASTE		= MODID + ":CamoPaste";
	public static final String	TEXTURE_ITEM_DOOR_WOOD	= MODID + ":CamoDoorWood";
	public static final String	TEXTURE_ITEM_DOOR_STEEL	= MODID + ":CamoDoorSteel";
	public static final String	TEXTURE_BLOCK_BASE		= MODID + ":CamoBase";
	public static final String	TEXTURE_BLOCK_STAIR		= MODID + ":CamoStair";
	public static final String	TEXTURE_BLOCK_CHEST		= MODID + ":CamoChest";
	
	// render IDs
	public static boolean		displayCamo				= true;
	public static int			camoRenderId;
	public static int			torchRenderId;

	// misc
	public static Block			torchLever;
	public static Block			oneWay;

	// doors and Trap-Doors
	public static Block			camoTrapDoor;
	public static Block			camoDoorWood;
	public static Item			camoDoorWoodItem;
	public static Block			camoDoorIron;
	public static Item			camoDoorIronItem;

	// Camo Paste
	public static Item			camoPaste;

	// FullCamo Stuff
	public static Block			camoGhost;
	public static Block			camoLever;
	public static Block			camoCurrent;
	public static Block			camoButton;
	public static Block			camoGate;
	public static Block			camoGateExt;
	public static Block			camoPlateAll;
	public static Block			camoPlatePlayer;
	public static Block			camoStairs;										// thanks Alexbegt
	public static Block			camoChest;											// thanks alexbegt

	// creative tab
	public static CreativeTabs	tab;

	// ids
	private int[]				ids;

	@PreInit
	public void preLoad(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();

		Configuration config = new Configuration(
				e.getSuggestedConfigurationFile());
		ids = new int[] {
				config.getBlock("CamoBlocks", "torchLever",           200 ).getInt(),
				config.getBlock("CamoBlocks", "oneWay",               201 ).getInt(),
				config.getBlock("CamoBlocks", "camoGate",             202 ).getInt(),
				config.getBlock("CamoBlocks", "camoGateExt",          203 ).getInt(),
				config.getBlock("CamoBlocks", "camoTrapDoor",         204 ).getInt(),
				config.getItem( "CamoItems",  "camoWoodDoor",         4106).getInt(),
				config.getBlock("CamoBlocks", "camoWoodDoor",         205 ).getInt(),
				config.getItem( "CamoItems",  "camoIronDoor",         4107).getInt(),
				config.getBlock("CamoBlocks", "camoIronDoor",         206 ).getInt(),
				config.getItem( "CamoItems",  "camoPasteID",          4108).getInt(),
				config.getBlock("CamoBlocks", "ghostBlock",           207 ).getInt(),
				config.getBlock("CamoBlocks", "camoLeverBlock",       208 ).getInt(),
				config.getBlock("CamoBlocks", "camoRedstoneBlock",    209 ).getInt(),
				config.getBlock("CamoBlocks", "camoButtonBlock",      210 ).getInt(),
				config.getBlock("CamoBlocks", "camoPlateAllBlock",    211 ).getInt(),
				config.getBlock("CamoBlocks", "camoPlatePlayerBlock", 212 ).getInt(),
				config.getBlock("CamoBlocks", "camoStairBlock",       213 ).getInt(),
				config.getBlock("CamoBlocks", "camoChestBlock",       214 ).getInt()
				};
		config.save();

		MinecraftForge.EVENT_BUS.register(proxy);
	}

	@Init
	public void load(FMLInitializationEvent e)
	{
		// make creative tab.
		tab = new CreativeTabCamo();

		torchLever = new BlockTorchLever(ids[0], 80).setUnlocalizedName("mod_SRM.TorchLever");

		// Camo oneWay
		oneWay = new BlockOneWay(ids[1]).setUnlocalizedName("mod_SRM.OneWayGlass");

		// gates
		camoGate = new BlockCamoGate(ids[2]).setUnlocalizedName("mod_SRM.CamoGate");
		camoGateExt = new BlockCamoGateExt(ids[3]).setUnlocalizedName("mod_SRM.CamoGateExtension");

		// TrapDoor
		camoTrapDoor = new BlockCamoTrapDoor(ids[4]).setUnlocalizedName("mod_SRM.SecretTrapDoor");

		// doors, Iron AND Wood
		camoDoorWoodItem = new ItemCamoDoor(ids[5], Material.wood).setUnlocalizedName("mod_SRM.SecretWoodenDoorItem");
		camoDoorWood = new BlockCamoDoor(ids[6] - 256, Material.wood).setUnlocalizedName("mod_SRM.SecretWoodenDoorBlock");
		camoDoorIronItem = new ItemCamoDoor(ids[7] - 256, Material.iron).setUnlocalizedName("mod_SRM.SecretIronDoorItem");
		camoDoorIron = new BlockCamoDoor(ids[8], Material.iron).setUnlocalizedName("mod_SRM.SecretIronDoorBlock");

		// Camo Paste
		camoPaste = new ItemCamoPaste(ids[9] - 256).setUnlocalizedName("mod_SRM.CamoflaugePaste");

		// FullCamoBlocks
		camoGhost = new BlockCamoGhost(ids[10]).setUnlocalizedName("mod_SRM.GhostBlock");
		camoLever = new BlockCamoLever(ids[11]).setUnlocalizedName("mod_SRM.SecretCamoLever");
		camoCurrent = new BlockCamoWire(ids[12]).setUnlocalizedName("mod_SRM.SecretCamoRedstone");
		camoButton = new BlockCamoButton(ids[13]).setUnlocalizedName("mod_SRM.SecretCamoButton");

		camoPlateAll = new BlockCamoPlate(ids[14], false).setUnlocalizedName("mod_SRM.SecretPressurePlate");
		camoPlatePlayer = new BlockCamoPlate(ids[15], true).setUnlocalizedName("mod_SRM.SecretPlayerPlate");

		camoStairs = new BlockCamoStair(ids[16]).setUnlocalizedName("mod_SRM.SecretCamoStair");

		camoChest = new BlockCamoChest(ids[17]).setUnlocalizedName("mod_SRM.SecretCamoChest");

		// key Events
		proxy.loadKeyStuff();

		// registers
		GameRegistry.registerBlock(torchLever, "mod_SRM.TorchLever");
		GameRegistry.registerBlock(oneWay, "mod_SRM.OneWayGlass");
		GameRegistry.registerBlock(camoGate, "mod_SRM.CamoGate");
		GameRegistry.registerBlock(camoGateExt, "mod_SRM.CamoGateExtension");

		GameRegistry.registerBlock(camoTrapDoor, "mod_SRM.SecretTrapDoor");

		GameRegistry.registerBlock(camoDoorWood, "mod_SRM.SecretWoodenDoorBlock");
		GameRegistry.registerItem(camoDoorWoodItem, "mod_SRM.SecretWoodenDoorItem");
		GameRegistry.registerBlock(camoDoorIron, "mod_SRM.SecretIronDoorBlock");
		GameRegistry.registerItem(camoDoorIronItem, "mod_SRM.SecretWoodenIronItem");

		GameRegistry.registerItem(camoPaste, "mod_SRM.CamoflaugePaste");

		GameRegistry.registerBlock(camoGhost, "mod_SRM.GhostBlock");
		GameRegistry.registerBlock(camoLever, "mod_SRM.SecretCamoLever");
		GameRegistry.registerBlock(camoCurrent, "mod_SRM.SecretCamoRedstone");
		GameRegistry.registerBlock(camoButton, "mod_SRM.SecretCamoButton");

		GameRegistry.registerBlock(camoPlateAll, "mod_SRM.SecretPressurePlate");
		GameRegistry.registerBlock(camoPlatePlayer, "mod_SRM.SecretPlayerPlate");

		GameRegistry.registerBlock(camoStairs, "mod_SRM.SecretCamoStair");

		GameRegistry.registerBlock(camoChest, "mod_SRM.SecretCamoChest");

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityFull.class, "mod_SRM.TE_CamoFull");
		GameRegistry.registerTileEntity(TileEntityCamoChest.class, "mod_SRM.TE_CamoChest");

		// Names
		LanguageRegistry.instance().addNameForObject(torchLever, "en_US", "Torch Lever");
		LanguageRegistry.instance().addNameForObject(oneWay, "en_US", "One-Way Glass");
		LanguageRegistry.instance().addNameForObject(camoGate, "en_US", "Camo Gate");
		LanguageRegistry.instance().addNameForObject(camoGateExt, "en_US", "Camo Gate Extension");
		LanguageRegistry.instance().addNameForObject(camoTrapDoor, "en_US", "Secret TrapDoor");

		LanguageRegistry.instance().addNameForObject(camoDoorWoodItem, "en_US", "Secret Wooden Door");
		LanguageRegistry.instance().addNameForObject(camoDoorIronItem, "en_US", "Secret Iron Door");

		LanguageRegistry.instance().addNameForObject(camoPaste, "en_US", "Camoflage Paste");

		LanguageRegistry.instance().addNameForObject(camoGhost, "en_US", "Ghost Block");
		LanguageRegistry.instance().addNameForObject(camoLever, "en_US", "Secret Camo lever");
		LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US", "Secret Camo Redstone");
		LanguageRegistry.instance().addNameForObject(camoButton, "en_US", "Secret Camo button");

		LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US", "Secret PressurePlate");
		LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US", "Secret PlayerPlate");

		LanguageRegistry.instance().addNameForObject(camoStairs, "en_US", "Secret Camo Stair");

		// Names -- Added by Alexbegt (Camo Chest)
		LanguageRegistry.instance().addNameForObject(camoChest, "en_US", "Secret Camo Chest");
		LanguageRegistry.instance().addStringLocalization("container.CamoChest", "en_US", "Hidden Chest");

		// Renders
		proxy.loadRenderStuff();

		// Recipes
		/*
		 * TESTING RECIPES GameRegistry.addRecipe(new ItemStack(fullCamoGhost),
		 * new Object[] { "#", '#', Block.dirt }); GameRegistry.addRecipe(new
		 * ItemStack(oneWay), new Object[] { "##", '#', Block.dirt });
		 * GameRegistry.addRecipe(new ItemStack(torchLever), new Object[] { "#",
		 * "#", '#', Block.dirt }); GameRegistry.addRecipe(new
		 * ItemStack(camoCurrent), new Object[] { "##", "##", '#', Block.dirt
		 * });
		 */

		addrecipes();

		ids = null;
	}

	@ServerStarting
	public void registerCommand(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandShow());
	}
	
	@ServerStopping
	public void registerCommand(FMLServerStoppingEvent e)
	{
		proxy.onServerStop(e);
	}

	public static void addrecipes()
	{
		// init recipe..
		ShapedOreRecipe recipe;

		// Camo gate
		GameRegistry.addRecipe(new ItemStack(camoGate, 1), new Object[] {
				"#0#",
				"0A0",
				"#@#",
				'#', Block.planks,
				'0', camoPaste,
				'@', Item.redstone,
				'A', Item.enderPearl });

		// TorchLever
		GameRegistry.addRecipe(new ItemStack(torchLever, 1), new Object[] {
				"#", "X", '#', Block.torchWood, 'X', Item.redstone });

		// CamoDoors
		GameRegistry.addShapelessRecipe(new ItemStack(camoDoorWoodItem, 1),
				new Object[] { camoPaste, Item.doorWood });
		GameRegistry.addShapelessRecipe(new ItemStack(camoDoorIronItem, 1),
				new Object[] { camoPaste, Item.doorIron });
		GameRegistry.addShapelessRecipe(new ItemStack(camoTrapDoor, 1),
				new Object[] { camoPaste, Block.trapdoor });

		// CamoPaste
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X#X", "$0$", "@#@", 'X', "dyeRed", '$', "dyeGreen", '@',
				"dyeYellow", '#', "dyeBlack", '0', Block.dirt });
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X#X", "$0$", "@#@", 'X', "dyeRed", '$', "dyeGreen", '@',
				"dyeYellow", '#', "dyeBlack", '0', Block.sand });
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X#X", "$0$", "@#@", 'X', "dyeRed", '$', "dyeGreen", '@',
				"dyeYellow", '#', "dyeBlack", '0', Item.clay });
		GameRegistry.addRecipe(recipe); // add the recipe..

		// Camo OneWay
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "X00",
				"X00", "X00", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "00X",
				"00X", "00X", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "XXX",
				"000", "000", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "000",
				"000", "XXX", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addShapelessRecipe(new ItemStack(oneWay, 1), new Object[] {
				camoPaste, Block.glass });

		// CamoGhost
		GameRegistry.addRecipe(new ItemStack(camoGhost, 4), new Object[] {
				"X0X", "0 0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh });
		GameRegistry.addRecipe(new ItemStack(camoGhost, 4), new Object[] {
				"X0X", "0 0", "X0X", 'X', camoPaste, '0', Block.cloth });

		// Camo-Redstone
		GameRegistry.addRecipe(new ItemStack(camoCurrent, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Item.redstone });
		GameRegistry.addRecipe(new ItemStack(camoCurrent, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Item.redstone });

		// Camo-Lever
		GameRegistry.addRecipe(new ItemStack(camoLever, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.lever });
		GameRegistry.addRecipe(new ItemStack(camoLever, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.lever });

		// Camo-Button stuff
		GameRegistry.addRecipe(new ItemStack(camoButton, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.stoneButton });
		GameRegistry.addRecipe(new ItemStack(camoButton, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.stoneButton });

		// pressure plates
		GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X@X", "0 0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.pressurePlatePlanks });
		GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X@X", "0 0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.pressurePlatePlanks });

		GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X@X", "0#0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.pressurePlatePlanks, '#', Item.ingotIron });
		GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X@X", "0#0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.pressurePlatePlanks, '#', Item.ingotIron });

		// CamoStairs
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', "stairWood", });
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				"stairWood", });
		GameRegistry.addRecipe(recipe); // add the recipe..
		GameRegistry.addRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.stairsCobblestone });
		GameRegistry.addRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.stairsCobblestone });

		// CamoChests
		GameRegistry.addRecipe(new ItemStack(camoChest, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh,
				'@', Block.chest });
		GameRegistry.addRecipe(new ItemStack(camoChest, 1), new Object[] {
				"X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@',
				Block.chest });
	}
}
