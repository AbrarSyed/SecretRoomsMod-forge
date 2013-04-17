package com.github.AbrarSyed.SecretRooms.common;

import net.minecraft.block.Block;
import net.minecraft.block.EnumMobType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.github.AbrarSyed.SecretRooms.client.PacketHandlerClient;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
@NetworkMod(
		clientSideRequired = true,
		serverSideRequired = true,
		versionBounds = "[4.5.1]",
		clientPacketHandlerSpec = @SidedPacketHandler(channels =
		{
				"SRM-TE-CamoFull",
				"SRM-TE-Camo",
				"SRM-KeyEvents",
				"SRM-Display"
		},
				packetHandler = PacketHandlerClient.class),
		serverPacketHandlerSpec = @SidedPacketHandler(channels =
		{
				"SRM-TE-CamoFull",
				"SRM-TE-Camo",
				"SRM-KeyEvents",
				"SRM-Display"
		},
				packetHandler = PacketHandlerServer.class))
@Mod(modid = SecretRooms.modid, name = "SecretRoomsMod", version = "4.5.1")
public class SecretRooms
{
	public static final String		modid		= "SecretRoomsMod";

	@SidedProxy(
			clientSide = "com.github.AbrarSyed.SecretRooms.client.SecretRooms_client",
			serverSide = "com.github.AbrarSyed.SecretRooms.common.Proxy")
	public static Proxy				proxy;

	@Instance(value = modid)
	public static SecretRooms		instance;

	// misc
	public static Block				torchLever;
	public static Block				oneWay;

	// doors and Trap-Doors
	public static Block				camoTrapDoor;
	public static Block				camoDoorWood;
	public static Item				camoDoorWoodItem;
	public static Block				camoDoorIron;
	public static Item				camoDoorIronItem;

	// Camo Paste
	public static Item				camoPaste;

	// FullCamo Stuff
	public static Block				camoGhost;
	public static Block				camoLever;
	public static Block				camoCurrent;
	public static Block				camoButton;
	public static Block				camoGate;
	public static Block				camoGateExt;
	public static Block				camoPlateAll;
	public static Block				camoPlatePlayer;
	public static Block				camoStairs;													// thanks Alexbegt
	public static Block				camoChest;														// thanks alexbegt

	// render IDs
	public static int				camoRenderId;
	public static int				torchRenderId;

	public static boolean			displayCamo	= true;

	// creative tab
	protected static CreativeTabs	tab;

	// ids
	private int[]					ids;

	// textures
	@SideOnly(value = Side.CLIENT)
	public static final String		textureFile	= "/com/github/AbrarSyed/SecretRooms/textures.png";

	@PreInit
	public void preLoad(FMLPreInitializationEvent e)
	{
		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		ids = new int[] {
				config.getBlock("CamoBlocks", "torchLever", 200).getInt(),
				config.getBlock("CamoBlocks", "oneWay", 201).getInt(),
				config.getBlock("CamoBlocks", "camoGate", 202).getInt(),
				config.getBlock("CamoBlocks", "camoGateExt", 203).getInt(),
				config.getBlock("CamoBlocks", "camoTrapDoor", 204).getInt(),
				config.getItem("CamoItems", "camoWoodDoor", 3850 + 256).getInt(),
				config.getBlock("CamoBlocks", "camoWoodDoor", 205).getInt(),
				config.getItem("CamoItems", "camoIronDoor", 3851 + 256).getInt(),
				config.getBlock("CamoBlocks", "camoIronDoor", 206).getInt(),
				config.getItem("CamoItems", "camoPasteID", 3852 + 256).getInt(),
				config.getBlock("CamoBlocks", "ghostBlock", 207).getInt(),
				config.getBlock("CamoBlocks", "camoLeverBlock", 208).getInt(),
				config.getBlock("CamoBlocks", "camoRedstoneBlock", 209).getInt(),
				config.getBlock("CamoBlocks", "camoButtonBlock", 210).getInt(),
				config.getBlock("CamoBlocks", "camoPlateAllBlock", 211).getInt(),
				config.getBlock("CamoBlocks", "camoPlatePlayerBlock", 212).getInt(),
				config.getBlock("CamoBlocks", "camoStairBlock", 213).getInt(),
				config.getBlock("CamoBlocks", "camoChestBlock", 214).getInt()
		};
		config.save();

		MinecraftForge.EVENT_BUS.register(proxy);
	}

	@Init
	public void load(FMLInitializationEvent e)
	{
		// make creative tab.
		tab = new CreativeTabCamo();

		torchLever = new BlockTorchLever(ids[0], 80).setBlockName("Torch Lever");

		// Camo oneWay
		oneWay = new BlockOneWay(ids[1], 49).setBlockName("One-Way Glass");

		// gates
		camoGate = new BlockCamoGate(ids[2]).setBlockName("Camo Gate");
		camoGateExt = new BlockCamoGateExt(ids[3]).setBlockName("Camo Gate Extension");

		// TrapDoor
		camoTrapDoor = new BlockCamoTrapDoor(ids[4]).setBlockName("Secret TrapDoor");

		// doors, Iron AND Wood
		camoDoorWoodItem = new ItemCamoDoor(ids[5]-256, Material.wood).setItemName("Secret Wooden Door");
		camoDoorWood = new BlockCamoDoor(ids[6], Material.wood).setBlockName("Secret Wooden Door");
		camoDoorIronItem = new ItemCamoDoor(ids[7]-256, Material.iron).setItemName("Secret Iron Door");
		camoDoorIron = new BlockCamoDoor(ids[8], Material.iron).setBlockName("Secret Iron Door");

		// Camo Paste
		camoPaste = new ItemCamo(ids[9]-256).setItemName("Camoflauge Paste");

		// FullCamoBlocks
		camoGhost = new BlockCamoGhost(ids[10]).setBlockName("Ghost Block");
		camoLever = new BlockCamoLever(ids[11]).setBlockName("Secret Camo Lever");
		camoCurrent = new BlockCamoWire(ids[12]).setBlockName("Secret Camo Redstone");
		camoButton = new BlockCamoButton(ids[13]).setBlockName("Secret Camo Button");

		camoPlateAll = new BlockCamoPlate(ids[14], EnumMobType.everything).setBlockName("Secret PressurePlate");
		camoPlatePlayer = new BlockCamoPlate(ids[15], EnumMobType.players).setBlockName("Secret PlayerPlate");

		camoStairs = new BlockCamoStair(ids[16]).setBlockName("Secret Camo Stair");

		camoChest = new BlockCamoChest(ids[17]).setBlockName("Secret Camo Chest");

		// key Events
		proxy.loadKeyStuff();

		// registers
		GameRegistry.registerBlock(torchLever, "SRM_TorchLever");
		GameRegistry.registerBlock(oneWay, "SRM_OneWay");
		GameRegistry.registerBlock(camoGate, "SRM_CamoGate");
		GameRegistry.registerBlock(camoGateExt, "SRM_CamoGateExt");
		GameRegistry.registerBlock(camoTrapDoor, "SRM_TrapDoor");
		GameRegistry.registerBlock(camoDoorWood, "SRM_CamoDoor_Wood");
		GameRegistry.registerBlock(camoDoorIron, "SRM_CamoDoor_Iron");

		GameRegistry.registerBlock(camoGhost, "SRM_CamoGhost");

		GameRegistry.registerBlock(camoLever, "SRM_CamoLever");
		GameRegistry.registerBlock(camoCurrent, "SRM_CamoRedstone");
		GameRegistry.registerBlock(camoButton, "SRM_CamoButton");

		GameRegistry.registerBlock(camoPlateAll, "SRM_CamoPlate_All");
		GameRegistry.registerBlock(camoPlatePlayer, "SRM_CamoPlate_Player");

		GameRegistry.registerBlock(camoStairs, "SRM_CamoStair");

		GameRegistry.registerBlock(camoChest, "SRM_CamoChest");

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCamo.class, "Camo");
		GameRegistry.registerTileEntity(TileEntityCamoFull.class, "CamoFull");
		GameRegistry.registerTileEntity(TileEntityCamoChest.class, "CamoChest");

		// Names
		LanguageRegistry.instance().addNameForObject(torchLever, "en_US", "Torch Lever");
		LanguageRegistry.instance().addNameForObject(oneWay, "en_US", "One-Way Glass");
		LanguageRegistry.instance().addNameForObject(camoGate, "en_US", "Camo Gate");
		LanguageRegistry.instance().addNameForObject(camoGateExt, "en_US", "Camo Gate Extension");
		LanguageRegistry.instance().addNameForObject(camoTrapDoor, "en_US", "Secret TrapDoor");

		LanguageRegistry.instance().addNameForObject(camoDoorWoodItem, "en_US", "Secret Wooden Door");
		LanguageRegistry.instance().addNameForObject(camoDoorIronItem, "en_US", "Secret Iron Door");

		LanguageRegistry.instance().addNameForObject(camoPaste, "en_US", "Camoflauge Paste");

		LanguageRegistry.instance().addNameForObject(camoGhost, "en_US", "Ghost Block");
		LanguageRegistry.instance().addNameForObject(camoLever, "en_US", "Secret Camo lever");
		LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US", "Secret Camo Redstone");
		LanguageRegistry.instance().addNameForObject(camoButton, "en_US", "Secret Camo button");

		LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US", "Secret PressurePlate");
		LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US", "Secret PlayerPlate");

		LanguageRegistry.instance().addNameForObject(camoStairs, "en_US", "Secret Camo Stair");

		// Names -- Added by Alexbegt (Camo Chest)
		LanguageRegistry.instance().addNameForObject(camoChest, "en_US", "Secret Camo Chest");
		LanguageRegistry.instance().addStringLocalization("container.CamochestDouble", "en_US", "Hidden Large Chest");
		LanguageRegistry.instance().addStringLocalization("container.Camochest", "en_US", "Hidden Chest");

		// Renders
		proxy.loadRenderStuff();

		// Recipes
		/*
		 * TESTING RECIPES
		 * GameRegistry.addRecipe(new ItemStack(fullCamoGhost), new Object[] { "#", '#', Block.dirt }); GameRegistry.addRecipe(new ItemStack(oneWay), new Object[] { "##", '#', Block.dirt }); GameRegistry.addRecipe(new ItemStack(torchLever), new Object[] { "#", "#", '#', Block.dirt }); GameRegistry.addRecipe(new ItemStack(camoCurrent), new Object[] { "##", "##", '#', Block.dirt });
		 */

		addrecipes();

		ids = null;
	}

	@ServerStarting
	public void registerCommand(FMLServerStartingEvent e)
	{
		e.registerServerCommand(new CommandShow());
	}

	public static void addrecipes()
	{
		// init recipe..
		ShapedOreRecipe recipe;

		// Camo gate
		GameRegistry.addRecipe(new ItemStack(camoGate, 1), new Object[] { "#0#", "0A0", "#@#", '#', Block.planks, '0', camoPaste, '@', Item.redstone, 'A', Item.enderPearl });

		// TorchLever
		GameRegistry.addRecipe(new ItemStack(torchLever, 1), new Object[] { "#", "X", '#', Block.torchWood, 'X', Item.redstone });

		// CamoDoors
		GameRegistry.addShapelessRecipe(new ItemStack(camoDoorWoodItem, 1), new Object[] { camoPaste, Item.doorWood });
		GameRegistry.addShapelessRecipe(new ItemStack(camoDoorIronItem, 1), new Object[] { camoPaste, Item.doorSteel });
		GameRegistry.addShapelessRecipe(new ItemStack(camoTrapDoor, 1), new Object[] { camoPaste, Block.trapdoor });

		// CamoPaste
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[]
		{
				"X#X",
				"$0$",
				"@#@",
				'X', "dyeRed",
				'$', "dyeGreen",
				'@', "dyeYellow",
				'#', "dyeBlack",
				'0', Block.dirt
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[]
		{
				"X#X",
				"$0$",
				"@#@",
				'X', "dyeRed",
				'$', "dyeGreen",
				'@', "dyeYellow",
				'#', "dyeBlack",
				'0', Block.sand
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[]
		{
				"X#X",
				"$0$",
				"@#@",
				'X', "dyeRed",
				'$', "dyeGreen",
				'@', "dyeYellow",
				'#', "dyeBlack",
				'0', Item.clay
		});
		GameRegistry.addRecipe(recipe); // add the recipe..

		// Camo OneWay
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "X00", "X00", "X00", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "00X", "00X", "00X", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "XXX", "000", "000", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[] { "000", "000", "XXX", 'X', camoPaste, '0', Block.glass });
		GameRegistry.addShapelessRecipe(new ItemStack(oneWay, 1), new Object[] { camoPaste, Block.glass });

		// CamoGhost
		GameRegistry.addRecipe(new ItemStack(camoGhost, 4), new Object[] { "X0X", "0 0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh });
		GameRegistry.addRecipe(new ItemStack(camoGhost, 4), new Object[] { "X0X", "0 0", "X0X", 'X', camoPaste, '0', Block.cloth });

		// Camo-Redstone
		GameRegistry.addRecipe(new ItemStack(camoCurrent, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Item.redstone });
		GameRegistry.addRecipe(new ItemStack(camoCurrent, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Item.redstone });

		// Camo-Lever
		GameRegistry.addRecipe(new ItemStack(camoLever, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Block.lever });
		GameRegistry.addRecipe(new ItemStack(camoLever, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Block.lever });

		// Camo-Button stuff
		GameRegistry.addRecipe(new ItemStack(camoButton, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Block.stoneButton });
		GameRegistry.addRecipe(new ItemStack(camoButton, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Block.stoneButton });

		// pressure plates
		GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[] { "X@X", "0 0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Block.pressurePlatePlanks });
		GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[] { "X@X", "0 0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Block.pressurePlatePlanks });

		GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] { "X@X", "0#0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Block.pressurePlatePlanks, '#', Item.ingotIron });
		GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] { "X@X", "0#0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Block.pressurePlatePlanks, '#', Item.ingotIron });

		// CamoStairs
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[]
		{
				"X0X",
				"0@0",
				"X0X",
				'X', camoPaste,
				'0', Item.rottenFlesh,
				'@', "stairWood",
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[]
		{
				"X0X",
				"0@0",
				"X0X",
				'X', camoPaste,
				'0', Block.cloth,
				'@', "stairWood",
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		GameRegistry.addRecipe(new ItemStack(camoStairs, 4), new Object[]
		{
				"X0X",
				"0@0",
				"X0X",
				'X', camoPaste,
				'0', Item.rottenFlesh,
				'@', Block.stairCompactCobblestone
		});
		GameRegistry.addRecipe(new ItemStack(camoStairs, 4), new Object[]
		{
				"X0X",
				"0@0",
				"X0X",
				'X', camoPaste,
				'0', Block.cloth,
				'@', Block.stairCompactCobblestone
		});

		// CamoChests
		GameRegistry.addRecipe(new ItemStack(camoChest, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Item.rottenFlesh, '@', Block.chest });
		GameRegistry.addRecipe(new ItemStack(camoChest, 1), new Object[] { "X0X", "0@0", "X0X", 'X', camoPaste, '0', Block.cloth, '@', Block.chest });
	}
}
