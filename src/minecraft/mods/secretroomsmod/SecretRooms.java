package mods.secretroomsmod;

import java.util.ArrayList;
import java.util.logging.Logger;

import mods.secretroomsmod.blocks.BlockCamoButton;
import mods.secretroomsmod.blocks.BlockCamoChest;
import mods.secretroomsmod.blocks.BlockCamoDoor;
import mods.secretroomsmod.blocks.BlockCamoGate;
import mods.secretroomsmod.blocks.BlockCamoGateExt;
import mods.secretroomsmod.blocks.BlockCamoGhost;
import mods.secretroomsmod.blocks.BlockCamoLever;
import mods.secretroomsmod.blocks.BlockCamoLightDetector;
import mods.secretroomsmod.blocks.BlockCamoPlate;
import mods.secretroomsmod.blocks.BlockCamoPlateWeighted;
import mods.secretroomsmod.blocks.BlockCamoStair;
import mods.secretroomsmod.blocks.BlockCamoTrapDoor;
import mods.secretroomsmod.blocks.BlockCamoWire;
import mods.secretroomsmod.blocks.BlockOneWay;
import mods.secretroomsmod.blocks.BlockTorchLever;
import mods.secretroomsmod.blocks.TileEntityCamo;
import mods.secretroomsmod.blocks.TileEntityCamoChest;
import mods.secretroomsmod.blocks.TileEntityCamoDetector;
import mods.secretroomsmod.client.CreativeTabCamo;
import mods.secretroomsmod.common.CommandShow;
import mods.secretroomsmod.common.ProxyCommon;
import mods.secretroomsmod.items.ItemCamoDoor;
import mods.secretroomsmod.items.ItemCamoPaste;
import mods.secretroomsmod.network.HandlerClient;
import mods.secretroomsmod.network.HandlerServer;
import mods.secretroomsmod.network.PacketSRMBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
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

	@SidedProxy(clientSide = "mods.secretroomsmod.client.ProxyClient", serverSide = "mods.secretroomsmod.common.ProxyCommon")
	public static ProxyCommon	proxy;

	public static final String	MODID					= "secretroomsmod";

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
	public static Block			camoPlateLight;
	public static Block			camoPlateHeavy;
	public static Block			camoStairs;
	public static Block			camoChest;
	public static Block			camoTrappedChest;
	public static Block			camoLightDetector;

	public static final String	CAMO_PASTE				= "camoPaste";

	// creative tab
	public static CreativeTabs	tab;

	// ids
	private int[]				ids;

	@PreInit
	public void preLoad(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();

		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		ids = new int[] {
				config.getBlock("CamoBlocks", "torchLever", 200).getInt(),
				config.getBlock("CamoBlocks", "oneWay", 201).getInt(),
				config.getBlock("CamoBlocks", "camoGate", 202).getInt(),
				config.getBlock("CamoBlocks", "camoGateExt", 203).getInt(),
				config.getBlock("CamoBlocks", "camoTrapDoor", 204).getInt(),
				config.getItem("CamoItems", "camoWoodDoor", 4106).getInt(),
				config.getBlock("CamoBlocks", "camoWoodDoor", 205).getInt(),
				config.getItem("CamoItems", "camoIronDoor", 4107).getInt(),
				config.getBlock("CamoBlocks", "camoIronDoor", 206).getInt(),
				config.getItem("CamoItems", "camoPasteID", 4108).getInt(),
				config.getBlock("CamoBlocks", "ghostBlock", 207).getInt(),
				config.getBlock("CamoBlocks", "camoLeverBlock", 208).getInt(),
				config.getBlock("CamoBlocks", "camoRedstoneBlock", 209).getInt(),
				config.getBlock("CamoBlocks", "camoButtonBlock", 210).getInt(),
				config.getBlock("CamoBlocks", "camoPlateAllBlock", 211).getInt(),
				config.getBlock("CamoBlocks", "camoPlatePlayerBlock", 212).getInt(),
				config.getBlock("CamoBlocks", "camoPlateWeightedBlock_light", 213).getInt(),
				config.getBlock("CamoBlocks", "camoPlateWeightedBlock_heavy", 214).getInt(),
				config.getBlock("CamoBlocks", "camoStairBlock", 215).getInt(),
				config.getBlock("CamoBlocks", "camoChestBlock", 216).getInt(),
				config.getBlock("CamoBlocks", "camoChestTrappedBlock", 217).getInt(),
				config.getBlock("CamoBlocks", "camoLightDetectorBlock", 218).getInt()
		};
		config.save();

		MinecraftForge.EVENT_BUS.register(proxy);

		// camo paste to the ore dictionary...
		OreDictionary.registerOre("camoPaste", new ItemStack(ids[9] - 256, 1, 0));
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
		camoLever = new BlockCamoLever(ids[11]).setUnlocalizedName("mod_SRM.SecretLever");
		camoCurrent = new BlockCamoWire(ids[12]).setUnlocalizedName("mod_SRM.SecretRedstone");
		camoButton = new BlockCamoButton(ids[13]).setUnlocalizedName("mod_SRM.SecretButton");

		camoPlateAll = new BlockCamoPlate(ids[14], false).setUnlocalizedName("mod_SRM.SecretPressurePlate");
		camoPlatePlayer = new BlockCamoPlate(ids[15], true).setUnlocalizedName("mod_SRM.SecretPlayerPlate");
		camoPlateLight = new BlockCamoPlateWeighted(ids[16], 64).setUnlocalizedName("mod_SRM.SecretLightPlate");
		camoPlateHeavy = new BlockCamoPlateWeighted(ids[17], 640).setUnlocalizedName("mod_SRM.SecretHeavyPlate");

		camoStairs = new BlockCamoStair(ids[18]).setUnlocalizedName("mod_SRM.SecretStair");

		camoChest = new BlockCamoChest(ids[19], false).setUnlocalizedName("mod_SRM.SecretChest");
		camoTrappedChest = new BlockCamoChest(ids[20], true).setUnlocalizedName("mod_SRM.SecretTrappedChest");
		
		camoLightDetector = new BlockCamoLightDetector(ids[21]).setUnlocalizedName("mod_SRM.SecretLightDetector");

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
		GameRegistry.registerBlock(camoPlateLight, "mod_SRM.SecretLightPlate");
		GameRegistry.registerBlock(camoPlateHeavy, "mod_SRM.SecretHeavyPlate");

		GameRegistry.registerBlock(camoStairs, "mod_SRM.SecretStair");

		GameRegistry.registerBlock(camoChest, "mod_SRM.SecretChest");
		GameRegistry.registerBlock(camoTrappedChest, "mod_SRM.SecretTrappedChest");
		
		GameRegistry.registerBlock(camoLightDetector, "mod_SRM.SecretLightDetector");

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCamo.class, "mod_SRM.TE_CamoFull");
		GameRegistry.registerTileEntity(TileEntityCamoChest.class, "mod_SRM.TE_CamoChest");
		GameRegistry.registerTileEntity(TileEntityCamoDetector.class, "mod_SRM.TE_CamoDetector");

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
		LanguageRegistry.instance().addNameForObject(camoLever, "en_US", "Secret Lever");
		LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US", "Secret Redstone");
		LanguageRegistry.instance().addNameForObject(camoButton, "en_US", "Secret Button");

		LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US", "Secret PressurePlate");
		LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US", "Secret PlayerPlate");
		LanguageRegistry.instance().addNameForObject(camoPlateLight, "en_US", "Secret Weighted Plate (light)");
		LanguageRegistry.instance().addNameForObject(camoPlateHeavy, "en_US", "Secret Weighted Plate (heavy)");

		LanguageRegistry.instance().addNameForObject(camoStairs, "en_US", "Secret Stairs");

		// Names -- Added by Alexbegt (Camo Chest)
		LanguageRegistry.instance().addNameForObject(camoChest, "en_US", "Secret Chest");
		LanguageRegistry.instance().addNameForObject(camoTrappedChest, "en_US", "Secret Trapped Chest");
		LanguageRegistry.instance().addStringLocalization("container.CamoChest", "en_US", "Hidden Chest");
		
		LanguageRegistry.instance().addNameForObject(camoLightDetector, "en_US", "Secret Light Detector");

		// Renders
		proxy.loadRenderStuff();

		// Recipes
		/*
		 * TESTING RECIPES recipes.add(new ShapedOreRecipe(new ItemStack(fullCamoGhost),
		 * new Object[] { "#", '#', Block.dirt })); GameRegistry.addRecipe(new
		 * ItemStack(oneWay), new Object[] { "##", '#', Block.dirt }));
		 * recipes.add(new ShapedOreRecipe(new ItemStack(torchLever), new Object[] { "#",
		 * "#", '#', Block.dirt })); GameRegistry.addRecipe(new
		 * ItemStack(camoCurrent), new Object[] { "##", "##", '#', Block.dirt
		 * }));
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
		IRecipe recipe;
		ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();

		// Camo gate
		recipes.add(new ShapedOreRecipe(new ItemStack(camoGate, 1), new Object[] {
				"#0#",
				"0A0",
				"#@#",
				'#', Block.planks,
				'0', CAMO_PASTE,
				'@', Item.redstone,
				'A', Item.enderPearl
		}));

		// TorchLever
		recipes.add(new ShapedOreRecipe(new ItemStack(torchLever, 1), new Object[] {
				"#",
				"X",
				'#', Block.torchWood,
				'X', Item.redstone
		}));

		// CamoDoors
		recipes.add(new ShapelessOreRecipe(new ItemStack(camoDoorWoodItem, 1),
				new Object[] {
						CAMO_PASTE,
						Item.doorWood
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(camoDoorIronItem, 1),
				new Object[] {
						CAMO_PASTE,
						Item.doorIron
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(camoTrapDoor, 1),
				new Object[] {
						CAMO_PASTE,
						Block.trapdoor
				}));

		// CAMO_PASTE
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"XXX",
				"X0X",
				"XXX",
				'X', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE),
				'0', Block.dirt
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"XXX",
				"X0X",
				"XXX",
				'X', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE),
				'0', Block.sand
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"XXX",
				"X0X",
				"XXX",
				'X', new ItemStack(Item.dyePowder, 1, OreDictionary.WILDCARD_VALUE),
				'0', Item.clay
		}));

		// Camo OneWay
		recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] {
				"X00",
				"X00",
				"X00",
				'X', CAMO_PASTE,
				'0', Block.glass
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] {
				"00X",
				"00X",
				"00X",
				'X', CAMO_PASTE,
				'0', Block.glass
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] { "XXX",
				"000",
				"000",
				'X', CAMO_PASTE,
				'0', Block.glass
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] { "000",
				"000",
				"XXX",
				'X', CAMO_PASTE,
				'0', Block.glass
		}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(oneWay, 1), new Object[] {
				CAMO_PASTE, Block.glass }));

		// CamoGhost
		recipes.add(new ShapedOreRecipe(new ItemStack(camoGhost, 4), new Object[] {
				"X0X",
				"0 0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoGhost, 4), new Object[] {
				"X0X",
				"0 0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth
		}));

		// Camo-Redstone
		recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Item.redstone
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Item.redstone
		}));

		// Camo-Lever
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.lever
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.lever
		}));

		// Camo-Button stuff
		recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.stoneButton
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.stoneButton }));

		// pressure plates
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X@X",
				"0 0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlatePlanks
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X@X",
				"0 0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.pressurePlatePlanks }));

		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlatePlanks,
				'#', Item.ingotIron
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.pressurePlatePlanks,
				'#', Item.ingotIron
		}));

		// weighted pressure plates
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateLight, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlateGold,
				'#', Item.ingotIron
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateLight, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.pressurePlateGold,
				'#', Item.ingotIron
		}));

		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateHeavy, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlateIron,
				'#', Item.ingotIron
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateHeavy, 1), new Object[] {
				"X@X",
				"0#0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.pressurePlateIron,
				'#', Item.ingotIron
		}));

		// CamoStairs
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', "stairWood"
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipe = new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', "stairWood"
		});
		GameRegistry.addRecipe(recipe); // add the recipe..
		recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.stairsCobblestone
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.stairsCobblestone
		}));

		// CamoChests
		recipes.add(new ShapedOreRecipe(new ItemStack(camoChest, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.chest
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoChest, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.chest
		}));

		// Trapped Chests
		recipes.add(new ShapedOreRecipe(new ItemStack(camoTrappedChest, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.chestTrapped
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoTrappedChest, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.chestTrapped
		}));
		
		// Trapped Chests
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLightDetector, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.daylightSensor
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLightDetector, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Block.cloth,
				'@', Block.daylightSensor
		}));

		for (IRecipe r : recipes)
		{
			GameRegistry.addRecipe(r);
		}
	}
}
