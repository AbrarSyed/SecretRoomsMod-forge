package com.github.AbrarSyed.secretroomsmod.common;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoButton;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoChest;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoDoor;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoDummy;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoGate;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoGhost;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoLever;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoLightDetector;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoPlate;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoPlateWeighted;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoStair;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoTrapDoor;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoWire;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockOneWay;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockSolidAir;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockTorchLever;
import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamo;
import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamoChest;
import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamoDetector;
import com.github.AbrarSyed.secretroomsmod.client.CreativeTabCamo;
import com.github.AbrarSyed.secretroomsmod.items.ItemBlockCamoButton;
import com.github.AbrarSyed.secretroomsmod.items.ItemCamoDoor;
import com.github.AbrarSyed.secretroomsmod.items.ItemCamoPaste;
import com.github.AbrarSyed.secretroomsmod.network.HandlerClient;
import com.github.AbrarSyed.secretroomsmod.network.HandlerServer;
import com.github.AbrarSyed.secretroomsmod.network.PacketSRMBase;

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

	@SidedProxy(clientSide = "com.github.AbrarSyed.secretroomsmod.client.ProxyClient", serverSide = "com.github.AbrarSyed.secretroomsmod.common.ProxyCommon")
	public static ProxyCommon	proxy;

	public static final String	MODID						= "secretroomsmod";

	@Instance(value = MODID)
	public static SecretRooms	instance;

	public static Logger		logger;

	// textures
	public static final String	TEXTURE_ITEM_PASTE			= MODID + ":CamoPaste";
	public static final String	TEXTURE_ITEM_DOOR_WOOD		= MODID + ":CamoDoorWood";
	public static final String	TEXTURE_ITEM_DOOR_STEEL		= MODID + ":CamoDoorSteel";

	public static final String	TEXTURE_BLOCK_BASE			= MODID + ":CamoBase";
	public static final String	TEXTURE_BLOCK_STAIR			= MODID + ":CamoStair";
	public static final String	TEXTURE_BLOCK_CHEST			= MODID + ":CamoChest";
	public static final String	TEXTURE_BLOCK_DETECTOR		= MODID + ":CamoDetector";
	public static final String	TEXTURE_BLOCK_GATE			= MODID + ":CamoGate";
	public static final String	TEXTURE_BLOCK_LEVER			= MODID + ":CamoLever";
	public static final String	TEXTURE_BLOCK_REDSTONE		= MODID + ":CamoRedstone";
	public static final String	TEXTURE_BLOCK_BUTTON		= MODID + ":CamoButton";

	public static final String	TEXTURE_BLOCK_PLATE_PLAYER	= MODID + ":CamoPlatePlayer";
	public static final String	TEXTURE_BLOCK_PLATE_WOOD	= MODID + ":CamoPlateWood";
	public static final String	TEXTURE_BLOCK_PLATE_IRON	= MODID + ":CamoPlateIron";
	public static final String	TEXTURE_BLOCK_PLATE_GOLD	= MODID + ":CamoPlateGold";

	public static final String	TEXTURE_BLOCK_TORCH			= MODID + ":TorchLever";

	public static final String	TEXTURE_BLOCK_SOLID_AIR		= MODID + ":SolidAir";
	public static final String	TEXTURE_BLOCK_CLEAR			= MODID + ":clear";

	// render IDs
	public static boolean		displayCamo					= true;
	public static int			render3DId;
	public static int			renderFlatId;

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

	public static Block			solidAir;

	public static final String	CAMO_PASTE					= "camoPaste";

	// creative tab
	public static CreativeTabs	tab;
	
	// material
	public static final MaterialFakeAir AIR_MAT = new MaterialFakeAir(); 

	// ids
	private int[]				ids;

	@PreInit
	public void preLoad(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();

		Configuration config = new Configuration(e.getSuggestedConfigurationFile());
		ids = new int[] {
				config.getBlock("CamoBlocks", "torchLever", 2020).getInt(),
				config.getBlock("CamoBlocks", "oneWay", 2021).getInt(),
				config.getBlock("CamoBlocks", "camoGate", 2022).getInt(),
				config.getBlock("CamoBlocks", "camoDummy", 2023).getInt(),
				config.getBlock("CamoBlocks", "camoTrapDoor", 2024).getInt(),
				config.getItem("CamoItems", "camoWoodDoor", 4106).getInt(),
				config.getBlock("CamoBlocks", "camoWoodDoor", 2025).getInt(),
				config.getItem("CamoItems", "camoIronDoor", 4107).getInt(),
				config.getBlock("CamoBlocks", "camoIronDoor", 2026).getInt(),
				config.getItem("CamoItems", "camoPasteID", 4108).getInt(),
				config.getBlock("CamoBlocks", "ghostBlock", 2027).getInt(),
				config.getBlock("CamoBlocks", "camoLeverBlock", 2028).getInt(),
				config.getBlock("CamoBlocks", "camoRedstoneBlock", 2029).getInt(),
				config.getBlock("CamoBlocks", "camoButtonBlock", 2030).getInt(),
				config.getBlock("CamoBlocks", "camoPlateAllBlock", 2031).getInt(),
				config.getBlock("CamoBlocks", "camoPlatePlayerBlock", 2032).getInt(),
				config.getBlock("CamoBlocks", "camoPlateWeightedBlock_light", 2033).getInt(),
				config.getBlock("CamoBlocks", "camoPlateWeightedBlock_heavy", 2034).getInt(),
				config.getBlock("CamoBlocks", "camoStairBlock", 2035).getInt(),
				config.getBlock("CamoBlocks", "camoChestBlock", 2036).getInt(),
				config.getBlock("CamoBlocks", "camoChestTrappedBlock", 2037).getInt(),
				config.getBlock("CamoBlocks", "camoLightDetectorBlock", 2038).getInt(),
				config.getBlock("CamoBlocks", "solidAir", 2039).getInt()
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
		camoGateExt = new BlockCamoDummy(ids[3]).setUnlocalizedName("mod_SRM.CamoDummy");

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

		solidAir = new BlockSolidAir(ids[22]).setUnlocalizedName("mod_SRM.SolidAir");

		// key Events
		proxy.loadKeyStuff();

		// registers
		GameRegistry.registerBlock(torchLever, "TorchLever");
		GameRegistry.registerBlock(oneWay, "OneWayGlass");
		GameRegistry.registerBlock(camoGate, "CamoGate");
		GameRegistry.registerBlock(camoGateExt, "CamoDummy");

		GameRegistry.registerBlock(camoTrapDoor, "SecretTrapDoor");

		GameRegistry.registerBlock(camoDoorWood, "SecretWoodenDoorBlock");
		GameRegistry.registerItem(camoDoorWoodItem, "SecretWoodenDoorItem");
		GameRegistry.registerBlock(camoDoorIron, "SecretIronDoorBlock");
		GameRegistry.registerItem(camoDoorIronItem, "SecretWoodenIronItem");

		GameRegistry.registerItem(camoPaste, "CamoflaugePaste");
		OreDictionary.registerOre("CAMO_PASTE", camoPaste);

		GameRegistry.registerBlock(camoGhost, "GhostBlock");
		GameRegistry.registerBlock(camoLever, "SecretCamoLever");
		GameRegistry.registerBlock(camoCurrent, "SecretCamoRedstone");
		
		GameRegistry.registerBlock(camoButton, ItemBlockCamoButton.class,  "SecretCamoButton");

		GameRegistry.registerBlock(camoPlateAll, "SecretPressurePlate");
		GameRegistry.registerBlock(camoPlatePlayer, "SecretPlayerPlate");
		GameRegistry.registerBlock(camoPlateLight, "SecretLightPlate");
		GameRegistry.registerBlock(camoPlateHeavy, "SecretHeavyPlate");

		GameRegistry.registerBlock(camoStairs, "SecretStair");

		GameRegistry.registerBlock(camoChest, "SecretChest");
		GameRegistry.registerBlock(camoTrappedChest, "SecretTrappedChest");

		GameRegistry.registerBlock(camoLightDetector, "SecretLightDetector");

		GameRegistry.registerBlock(solidAir, "SolidAir");

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCamo.class, "mod_SRM.TE_CamoFull");
		GameRegistry.registerTileEntity(TileEntityCamoChest.class, "mod_SRM.TE_CamoChest");
		GameRegistry.registerTileEntity(TileEntityCamoDetector.class, "mod_SRM.TE_CamoDetector");

		// Names
		LanguageRegistry.instance().addNameForObject(torchLever, "en_US", "Torch Lever");
		LanguageRegistry.instance().addNameForObject(oneWay, "en_US", "One-Way Glass");
		LanguageRegistry.instance().addNameForObject(camoGate, "en_US", "Camo Gate");
		LanguageRegistry.instance().addNameForObject(camoGateExt, "en_US", "Dummy Camo block (does nothing)");
		LanguageRegistry.instance().addNameForObject(camoTrapDoor, "en_US", "Secret TrapDoor");

		LanguageRegistry.instance().addNameForObject(camoDoorWoodItem, "en_US", "Secret Wooden Door");
		LanguageRegistry.instance().addNameForObject(camoDoorIronItem, "en_US", "Secret Iron Door");

		LanguageRegistry.instance().addNameForObject(camoPaste, "en_US", "Camoflage Paste");

		LanguageRegistry.instance().addNameForObject(camoGhost, "en_US", "Ghost Block");
		LanguageRegistry.instance().addNameForObject(camoLever, "en_US", "Secret Lever");
		LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US", "Secret Redstone");
		LanguageRegistry.instance().addNameForObject(camoButton, "en_US", "Secret Button");
		
		LanguageRegistry.instance().addStringLocalization("mod_SRM.SecretButton.stone", "Secret Stone Button");
		LanguageRegistry.instance().addStringLocalization("mod_SRM.SecretButton.wood", "Secret Wood Button");
		LanguageRegistry.instance().addStringLocalization("mod_SRM.SecretButton.stone", "en_us", "Secret Stone Button");
		LanguageRegistry.instance().addStringLocalization("mod_SRM.SecretButton.wood", "en_us", "Secret Wood Button");

		LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US", "Secret PressurePlate");
		LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US", "Secret PlayerPlate");
		LanguageRegistry.instance().addNameForObject(camoPlateLight, "en_US", "Secret Weighted Plate (light)");
		LanguageRegistry.instance().addNameForObject(camoPlateHeavy, "en_US", "Secret Weighted Plate (heavy)");

		LanguageRegistry.instance().addNameForObject(camoStairs, "en_US", "Secret Stairs");

		LanguageRegistry.instance().addNameForObject(camoChest, "en_US", "Secret Chest");
		LanguageRegistry.instance().addNameForObject(camoTrappedChest, "en_US", "Secret Trapped Chest");
		LanguageRegistry.instance().addStringLocalization("container.CamoChest", "en_US", "Hidden Chest");

		LanguageRegistry.instance().addNameForObject(camoLightDetector, "en_US", "Secret Light Detector");

		LanguageRegistry.instance().addNameForObject(solidAir, "en_US", "Solid Air");

		// ore dictionary

		OreDictionary.registerOre(CAMO_PASTE, camoPaste);

		// Renders
		proxy.loadRenderStuff();

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
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE)
		}));

		// Camo-Redstone
		recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Item.redstone
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Item.redstone
		}));

		// Camo-Lever
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.lever
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.lever
		}));

		// Camo-Button stuff
		recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.stoneButton
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.stoneButton }));

		// pressure plates
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlatePlanks
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateAll, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.pressurePlatePlanks }));

		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlateStone,
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlatePlayer, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.pressurePlateStone,
		}));

		// weighted pressure plates
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateLight, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlateGold,
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateLight, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				// new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE)
				'@', Block.pressurePlateGold,
		}));

		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateHeavy, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', Block.pressurePlateIron,
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoPlateHeavy, 1), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.pressurePlateIron,
		}));

		// CamoStairs
		recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', Item.rottenFlesh,
				'@', "stairWood"
		}));
		recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
				"X0X",
				"0@0",
				"X0X",
				'X', CAMO_PASTE,
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', "stairWood"
		}));
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
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
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
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
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
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
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
				'0', new ItemStack(Block.cloth.blockID, 1, OreDictionary.WILDCARD_VALUE),
				'@', Block.daylightSensor
		}));

		// Solid Air
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.blazePowder,
						Item.bucketWater
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.blazeRod,
						Item.bucketWater
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.magmaCream,
						Item.bucketWater
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.blazePowder,
						Item.potion
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.blazeRod,
						Item.potion
				}));
		recipes.add(new ShapelessOreRecipe(new ItemStack(solidAir, 1),
				new Object[] {
						CAMO_PASTE,
						Item.magmaCream,
						Item.potion
				}));

		// actually add the recipe
		for (IRecipe r : recipes)
		{
			GameRegistry.addRecipe(r);
		}
	}
}
