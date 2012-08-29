package com.github.AbrarSyed.SecretRooms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.Arrays;

import net.minecraft.src.Block;
import net.minecraft.src.CommandHandler;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.server.FMLServerHandler;

@NetworkMod( 
		clientSideRequired = true, 
		serverSideRequired = true, 
		versionBounds = "[4.2,)",
		clientPacketHandlerSpec = @SidedPacketHandler(
				channels = {"SRM-TE-CamoFull", "SRM-TE-Camo", "SRM-KeyEvents", "SRM-Display"},
				packetHandler = PacketHandlerClient.class
				),
		serverPacketHandlerSpec = @SidedPacketHandler(
				channels = {"SRM-TE-CamoFull", "SRM-TE-Camo", "SRM-KeyEvents", "SRM-Display"},
				packetHandler = PacketHandlerServer.class
				)
		)
@Mod(modid = "SecretRoomsMod", name = "SecretRoomsMod", version = "4.2.2")
public class SecretRooms
{
	
	@SidedProxy( clientSide = "com.github.AbrarSyed.SecretRooms.SecretRooms_client", serverSide = "com.github.AbrarSyed.SecretRooms.Proxy")
	public static Proxy proxy;
	
	@Instance
	public static SecretRooms instance;
	
	@PreInit
    public void load(FMLPreInitializationEvent e)
    {
		Configuration config = proxy.getConfig();
		
		config.load();
		int[] ids = {
				Integer.parseInt(config.getOrCreateBlockIdProperty("torchLever", 200).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("oneWay", 201).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoGate", 202).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoGateExt", 203).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoTrapDoor", 204).value),
				
				Integer.parseInt(config.getOrCreateIntProperty("camoWoodDoor", config.CATEGORY_ITEM, 3850+256).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoWoodDoor", 205).value),
				Integer.parseInt(config.getOrCreateIntProperty("camoIronDoor", config.CATEGORY_ITEM, 3851+256).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoIronDoor", 206).value),
				
				Integer.parseInt(config.getOrCreateIntProperty("camoPasteID", config.CATEGORY_ITEM, 3852+256).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("ghostBlock", 207).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoLeverBlock", 208).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoRedstoneBlock", 209).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoButtonBlock", 210).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoPlateAllBlock", 211).value),
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoPlatePlayerBlock", 212).value),
				
				Integer.parseInt(config.getOrCreateBlockIdProperty("camoStairBlock", 213).value),
		};
		config.save();
        
        torchLever = (new BlockTorchLever(ids[0], 80)).setBlockName("Torch Lever");
        
        // Camo oneWay
        oneWay = (new BlockOneWay(ids[1], 49)).setBlockName("One-Way Glass");
        
        // gates
        camoGate = (new BlockCamoGate(ids[2])).setBlockName("Camo Gate");
        camoGateExt = (new BlockCamoGateExt(ids[3])).setBlockName("Camo Gate Extension");
        
        // TrapDoor
        camoTrapDoor = (new BlockCamoTrapDoor(ids[4])).setBlockName("Secret TrapDoor");
        
        // doors, Iron AND Wood
        camoDoorWoodItem = (new ItemCamoDoor(ids[5], Material.wood)).setItemName("Secret Wooden Door");
        camoDoorWood = (new BlockCamoDoor(ids[6], Material.wood)).setBlockName("Secret Wooden Door");
        camoDoorIronItem = (new ItemCamoDoor(ids[7], Material.iron)).setItemName("Secret Iron Door");
        camoDoorIron = (new BlockCamoDoor(ids[8], Material.iron)).setBlockName("Secret Iron Door");
        
        // Camo Paste
        camoPaste = (new ItemCamo(ids[9])).setItemName("Camoflauge Paste");
        
        // FullCamoBlocks
        fullCamoGhost = (new BlockCamoGhost(ids[10])).setBlockName("Ghost Block");
        camoLever = (new BlockCamoLever(ids[11])).setBlockName("Secret Camo Lever");
        camoCurrent = (new BlockCamoWire(ids[12])).setBlockName("Secret Camo Redstone");
        camoButton = (new BlockCamoButton(ids[13])).setBlockName("Secret Camo Button");
        
        camoPlateAll = (new BlockCamoPlate(ids[14], EnumMobType.everything)).setBlockName("Secret PressurePlate");
        camoPlatePlayer = (new BlockCamoPlate(ids[15], EnumMobType.players)).setBlockName("Secret PlayerPlate");
        
        camoStairs = (new BlockCamoStair(ids[16], 0)).setBlockName("Secret Camo Stair");
        
        // key Events
        proxy.doKeyStuff();
        
        //registers
        GameRegistry.registerBlock(torchLever);
        GameRegistry.registerBlock(oneWay);
        GameRegistry.registerBlock(camoGate);
        GameRegistry.registerBlock(camoGateExt);
        GameRegistry.registerBlock(camoTrapDoor);
        GameRegistry.registerBlock(camoDoorWood);
        GameRegistry.registerBlock(camoDoorIron);
        
        GameRegistry.registerBlock(fullCamoGhost);
        
        GameRegistry.registerBlock(camoLever);
        GameRegistry.registerBlock(camoCurrent);
        GameRegistry.registerBlock(camoButton);
        
        GameRegistry.registerBlock(camoPlateAll);
        GameRegistry.registerBlock(camoPlatePlayer);
        
        GameRegistry.registerBlock(camoStairs);
        
        // camo Tile Entities
        GameRegistry.registerTileEntity(TileEntityCamo.class, "Camo");
        GameRegistry.registerTileEntity(TileEntityCamoFull.class, "CamoFull");
        
        //Names
        LanguageRegistry.instance().addNameForObject(torchLever, "en_US",  "Torch Lever");
        LanguageRegistry.instance().addNameForObject(oneWay, "en_US",  "One-Way Glass");
        LanguageRegistry.instance().addNameForObject(camoGate, "en_US",  "Camo Gate");
        LanguageRegistry.instance().addNameForObject(camoGateExt, "en_US",  "Camo Gate Extension");
        LanguageRegistry.instance().addNameForObject(camoTrapDoor, "en_US",  "Secret TrapDoor");
        
        LanguageRegistry.instance().addNameForObject(camoDoorWoodItem, "en_US",  "Secret Wooden Door");
        LanguageRegistry.instance().addNameForObject(camoDoorIronItem, "en_US",  "Secret Iron Door");
        
        LanguageRegistry.instance().addNameForObject(camoPaste, "en_US",  "Camoflauge Paste");
        
        LanguageRegistry.instance().addNameForObject(fullCamoGhost, "en_US",  "Ghost Block");
        LanguageRegistry.instance().addNameForObject(camoLever, "en_US",  "Secret Camo lever");
        LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US",  "Secret Camo Redstone");
        LanguageRegistry.instance().addNameForObject(camoButton, "en_US",  "Secret Camo button");
        
        LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US",  "Secret PressurePlate");
        LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US",  "Secret PlayerPlate");
        
        //Renders
        proxy.doRenderStuff();
        
        
        //Recipes
        /*
         * TESTING RECIPES
         *
        GameRegistry.addRecipe(new ItemStack(fullCamoGhost), new Object[] {
            "#", '#', Block.dirt
        });
        GameRegistry.addRecipe(new ItemStack(oneWay), new Object[] {
            "##", '#', Block.dirt
        });
        GameRegistry.addRecipe(new ItemStack(torchLever), new Object[] {
            "#", "#", '#', Block.dirt
        });
        GameRegistry.addRecipe(new ItemStack(camoCurrent), new Object[] {
            "##", "##", '#', Block.dirt
        });
        **/
        
        addrecipes();
    }
	
	@ServerStarted
	public void registerCommand(FMLServerStartedEvent e)
	{
		((CommandHandler)FMLCommonHandler.instance().getSidedDelegate().getServer().getCommandManager()).registerCommand(new CommandShowSecretBlocks());
	}
	
    public static void addrecipes()
    {
        // Shelf stuff
        GameRegistry.addRecipe(new ItemStack(camoGate, 1), new Object[]
                {
                    "#0#",
                    "0A0",
                    "#@#",
                    '#', Block.planks,
                    '0', camoPaste,
                    '@', Item.redstone,
                    'A', Item.enderPearl
                });
        
        // TorchLever
        GameRegistry.addRecipe(new ItemStack(torchLever, 1), new Object[]
                {
                    "#", "X", '#',  Block.torchWood, 'X', Item.redstone
                });
        
        // CamoDoors
        GameRegistry.addShapelessRecipe(new ItemStack(camoDoorWoodItem, 1), new Object[]
                { camoPaste, Item.doorWood});
        GameRegistry.addShapelessRecipe(new ItemStack(camoDoorIronItem, 1), new Object[]
                { camoPaste,Item.doorSteel});
        GameRegistry.addShapelessRecipe(new ItemStack(camoTrapDoor, 1), new Object[]
                { camoPaste, Block.trapdoor});
        
        //CamoPaste
        GameRegistry.addRecipe(new ItemStack(camoPaste, 9), new Object[]
        {
            "X#X",
            "$0$",
            "@#@",
            'X', new ItemStack(Item.dyePowder.shiftedIndex, 1, 1),
            '$', new ItemStack(Item.dyePowder.shiftedIndex, 1, 2),
            '@', new ItemStack(Item.dyePowder.shiftedIndex, 1, 11),
            '#', new ItemStack(Item.dyePowder.shiftedIndex, 1, 0),
            '0', Block.dirt
        });
        GameRegistry.addRecipe(new ItemStack(camoPaste, 9), new Object[]
        {
        	"X#X",
            "$0$",
            "@#@",
            'X', new ItemStack(Item.dyePowder.shiftedIndex, 1, 1),
            '$', new ItemStack(Item.dyePowder.shiftedIndex, 1, 2),
            '@', new ItemStack(Item.dyePowder.shiftedIndex, 1, 11),
            '#', new ItemStack(Item.dyePowder.shiftedIndex, 1, 0),
            '0', Block.sand
        });
        GameRegistry.addRecipe(new ItemStack(camoPaste, 9), new Object[]
        {
        	"X#X",
            "$0$",
            "@#@",
            'X', new ItemStack(Item.dyePowder.shiftedIndex, 1, 1),
            '$', new ItemStack(Item.dyePowder.shiftedIndex, 1, 2),
            '@', new ItemStack(Item.dyePowder.shiftedIndex, 1, 11),
            '#', new ItemStack(Item.dyePowder.shiftedIndex, 1, 0),
            '0', Item.clay
        });
        
        // Camo OneWay
        GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[]
                {
                    "X00",
                    "X00",
                    "X00",
                    'X', camoPaste,
                    '0', Block.glass
                });
        GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[]
                {
                    "00X",
                    "00X",
                    "00X",
                    'X', camoPaste,
                    '0', Block.glass
                });
        GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[]
                {
                    "XXX",
                    "000",
                    "000",
                    'X', camoPaste,
                    '0', Block.glass
                });
        GameRegistry.addRecipe(new ItemStack(oneWay, 9), new Object[]
                {
                    "000",
                    "000",
                    "XXX",
                    'X', camoPaste,
                    '0', Block.glass
                });
        ModLoader.addShapelessRecipe(new ItemStack(oneWay, 1), new Object[]
                { camoPaste, Block.glass });
        
        // CamoGhost
        GameRegistry.addRecipe(new ItemStack(fullCamoGhost, 4), new Object[]
                {
                    "X0X",
                    "0 0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh
                });
        GameRegistry.addRecipe(new ItemStack(fullCamoGhost, 4), new Object[]
                {
                    "X0X",
                    "0 0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth
                });
        
        // Camo-Redstone
        GameRegistry.addRecipe(new ItemStack(camoCurrent, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh,
                    '@', Item.redstone
                });
        GameRegistry.addRecipe(new ItemStack(camoCurrent, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth,
                    '@', Item.redstone
                });
        
        // Camo-Lever
        GameRegistry.addRecipe(new ItemStack(camoLever, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh,
                    '@', Block.lever
                });
        GameRegistry.addRecipe(new ItemStack(camoLever, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth,
                    '@', Block.lever
                });
        
        // Camo-Button stuff
        GameRegistry.addRecipe(new ItemStack(camoButton, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh,
                    '@', Block.button
                });
        GameRegistry.addRecipe(new ItemStack(camoButton, 4), new Object[]
                {
                    "X0X",
                    "0@0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth,
                    '@', Block.button
                });
        
        // pressure plates
        GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[]
                {
                    "X@X",
                    "0 0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh,
                    '@', Block.pressurePlatePlanks
                });
        GameRegistry.addRecipe(new ItemStack(camoPlateAll, 1), new Object[]
                {
                    "X@X",
                    "0 0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth,
                    '@', Block.pressurePlatePlanks
                });
        
        
        GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[]
                {
                    "X@X",
                    "0#0",
                    "X0X",
                    'X', camoPaste,
                    '0', Item.rottenFlesh,
                    '@', Block.pressurePlatePlanks,
                    '#', Item.ingotIron
                });
        GameRegistry.addRecipe(new ItemStack(camoPlatePlayer, 1), new Object[]
                {
                    "X@X",
                    "0#0",
                    "X0X",
                    'X', camoPaste,
                    '0', Block.cloth,
                    '@', Block.pressurePlatePlanks,
                    '#', Item.ingotIron
                });
    }

    // shelfs
    public static Block torchLever;
    public static Block oneWay;
    public static Block camoGate;
    public static Block camoGateExt;
    
    // doors and Trap-Doors
    public static Block camoTrapDoor;
    public static Block camoDoorWood;
    public static Item camoDoorWoodItem;
    public static Block camoDoorIron;
    public static Item camoDoorIronItem;
    
    // Camo Paste
    public static Item camoPaste;
    
    // textures
    @SideOnly( value = Side.CLIENT)
    public static final String textureFile = "/com/github/AbrarSyed/SecretRooms/textures.png";
    
    // FullCamo Stuff
    public static Block fullCamoGhost;
    public static Block camoLever;
    public static Block camoCurrent;
    public static Block camoButton;
    
    // camo Plates
    public static Block camoPlateAll;
    public static Block camoPlatePlayer;
    
    // camo Stairs - alexbegt
    public static Block camoStairs;
    
    protected static int camoRenderId;
    protected static int torchRenderId;
    
    protected static boolean displayCamo = true;

    // Keybinding stuff.
    protected static boolean OneWayFaceTowards = true;
}
