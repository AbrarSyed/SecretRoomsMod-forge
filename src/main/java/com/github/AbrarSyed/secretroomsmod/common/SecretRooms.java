package com.github.AbrarSyed.secretroomsmod.common;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.github.AbrarSyed.secretroomsmod.blocks.*;
import com.github.AbrarSyed.secretroomsmod.items.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author AbrarSyed
 */
@Mod(modid = SecretRooms.MODID, version = "@VERSION@", useMetadata = true,
        acceptableRemoteVersions = "@CHANGE_VERSION@", acceptedMinecraftVersions = "@MC_VERSION@", acceptableSaveVersions = "@CHANGE_VERSION@")
public class SecretRooms
{

    @SidedProxy(clientSide = "com.github.AbrarSyed.secretroomsmod.client.ProxyClient", serverSide = "com.github.AbrarSyed.secretroomsmod.common.ProxyCommon")
    public static ProxyCommon  proxy;

    public static final String MODID                      = "secretroomsmod";

    @Instance(value = MODID)
    public static SecretRooms  instance;

    // textures
    public static final String TEXTURE_ITEM_PASTE         = MODID + ":CamoPaste";
    public static final String TEXTURE_ITEM_DOOR_WOOD     = MODID + ":CamoDoorWood";
    public static final String TEXTURE_ITEM_DOOR_STEEL    = MODID + ":CamoDoorSteel";

    public static final String TEXTURE_BLOCK_BASE         = MODID + ":CamoBase";
    public static final String TEXTURE_BLOCK_STAIR        = MODID + ":CamoStair";
    public static final String TEXTURE_BLOCK_CHEST        = MODID + ":CamoChest";
    public static final String TEXTURE_BLOCK_DETECTOR     = MODID + ":CamoDetector";
    public static final String TEXTURE_BLOCK_GATE         = MODID + ":CamoGate";
    public static final String TEXTURE_BLOCK_LEVER        = MODID + ":CamoLever";
    public static final String TEXTURE_BLOCK_REDSTONE     = MODID + ":CamoRedstone";
    public static final String TEXTURE_BLOCK_BUTTON       = MODID + ":CamoButton";

    public static final String TEXTURE_BLOCK_PLATE_PLAYER = MODID + ":CamoPlatePlayer";
    public static final String TEXTURE_BLOCK_PLATE_WOOD   = MODID + ":CamoPlateWood";
    public static final String TEXTURE_BLOCK_PLATE_IRON   = MODID + ":CamoPlateIron";
    public static final String TEXTURE_BLOCK_PLATE_GOLD   = MODID + ":CamoPlateGold";

    public static final String TEXTURE_BLOCK_TORCH        = MODID + ":TorchLever";

    public static final String TEXTURE_BLOCK_SOLID_AIR    = MODID + ":SolidAir";
    public static final String TEXTURE_BLOCK_CLEAR        = MODID + ":clear";

    // render IDs
    public static boolean      displayCamo                = true;
    public static int          render3DId;
    public static int          renderFlatId;

    // misc
    public static Block        torchLever, oneWay;

    // doors and Trap-Doors
    public static Block        camoTrapDoor;
    public static Block        camoDoorWood, camoDoorIron;
    public static Item         camoDoorWoodItem, camoDoorIronItem;

    // Camo Paste
    public static Item         camoPaste;

    // FullCamo Stuff
    public static Block        camoGhost;
    public static Block        camoLever;
    public static Block        camoCurrent;
    public static Block        camoButton;
    public static Block        camoGate, camoGateExt;
    public static Block        camoPlateAll, camoPlatePlayer;
    public static Block        camoPlateLight, camoPlateHeavy;
    public static Block        camoStairs;
    public static Block        camoChest;
    public static Block        camoTrappedChest;
    public static Block        camoLightDetector;

    public static Block        solidAir;

    public static final String CAMO_PASTE                 = "camoPaste";

    // creative tab
    public static CreativeTabs tab;

    @EventHandler
    public void preLoad(FMLPreInitializationEvent e)
    {
        MinecraftForge.EVENT_BUS.register(proxy);

        // make creative tab.
        tab = new CreativeTabCamo();

        torchLever = new BlockTorchLever(80).setBlockName("TorchLever");

        // Camo oneWay
        oneWay = new BlockOneWay().setBlockName("OneWayGlass");

        // gates
        camoGate = new BlockCamoGate().setBlockName("CamoGate");
        camoGateExt = new BlockCamoDummy().setBlockName("CamoDummy");

        // TrapDoor
        camoTrapDoor = new BlockCamoTrapDoor().setBlockName("SecretTrapDoor");

        // doors, Iron AND Wood
        camoDoorWoodItem = new ItemCamoDoor(Material.wood).setUnlocalizedName("SecretWoodenDoorItem");
        camoDoorWood = new BlockCamoDoor(Material.wood).setBlockName("SecretWoodenDoorBlock");
        camoDoorIronItem = new ItemCamoDoor(Material.iron).setUnlocalizedName("SecretIronDoorItem");
        camoDoorIron = new BlockCamoDoor(Material.iron).setBlockName("SecretIronDoorBlock");

        // Camo Paste
        camoPaste = new Item().setUnlocalizedName("CamoflaugePaste").setCreativeTab(SecretRooms.tab).setTextureName(TEXTURE_ITEM_PASTE);

        // FullCamoBlocks
        camoGhost = new BlockCamoGhost().setBlockName("GhostBlock");
        camoLever = new BlockCamoLever().setBlockName("SecretLever");
        camoCurrent = new BlockCamoWire().setBlockName("SecretRedstone");
        camoButton = new BlockCamoButton().setBlockName("SecretButton");

        camoPlateAll = new BlockCamoPlate(false).setBlockName("SecretPressurePlate");
        camoPlatePlayer = new BlockCamoPlate(true).setBlockName("SecretPlayerPlate");
        camoPlateLight = new BlockCamoPlateWeighted(64).setBlockName("SecretLightPlate");
        camoPlateHeavy = new BlockCamoPlateWeighted(640).setBlockName("SecretHeavyPlate");

        camoStairs = new BlockCamoStair().setBlockName("SecretStair");

        camoChest = new BlockCamoChest(false).setBlockName("SecretChest");
        camoTrappedChest = new BlockCamoChest(true).setBlockName("SecretTrappedChest");

        camoLightDetector = new BlockCamoLightDetector().setBlockName("SecretLightDetector");

        solidAir = new BlockSolidAir(new MaterialFakeAir()).setBlockName("SolidAir");

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
        OreDictionary.registerOre(CAMO_PASTE, camoPaste);

        GameRegistry.registerBlock(camoGhost, "GhostBlock");
        GameRegistry.registerBlock(camoLever, "SecretCamoLever");
        GameRegistry.registerBlock(camoCurrent, "SecretCamoRedstone");

        GameRegistry.registerBlock(camoButton, ItemBlockCamoButton.class, "SecretCamoButton");

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
        GameRegistry.registerTileEntity(TileEntityCamo.class, "TE_CamoFull");
        GameRegistry.registerTileEntity(TileEntityCamoChest.class, "TE_CamoChest");
        GameRegistry.registerTileEntity(TileEntityCamoDetector.class, "TE_CamoDetector");

        //      LanguageRegistry.instance().addNameForObject(torchLever, "en_US", "Torch Lever");
        //      LanguageRegistry.instance().addNameForObject(oneWay, "en_US", "One-Way Glass");
        //      LanguageRegistry.instance().addNameForObject(camoGate, "en_US", "Camo Gate");
        //      LanguageRegistry.instance().addNameForObject(camoGateExt, "en_US", "Dummy Camo block (does nothing)");
        //      LanguageRegistry.instance().addNameForObject(camoTrapDoor, "en_US", "Secret TrapDoor");
        //
        //      LanguageRegistry.instance().addNameForObject(camoDoorWoodItem, "en_US", "Secret Wooden Door");
        //      LanguageRegistry.instance().addNameForObject(camoDoorIronItem, "en_US", "Secret Iron Door");
        //
        //      LanguageRegistry.instance().addNameForObject(camoPaste, "en_US", "Camoflage Paste");
        //
        //      LanguageRegistry.instance().addNameForObject(camoGhost, "en_US", "Ghost Block");
        //      LanguageRegistry.instance().addNameForObject(camoLever, "en_US", "Secret Lever");
        //      LanguageRegistry.instance().addNameForObject(camoCurrent, "en_US", "Secret Redstone");
        //      LanguageRegistry.instance().addNameForObject(camoButton, "en_US", "Secret Button");
        //
        //      LanguageRegistry.instance().addStringLocalization("SecretButton.stone", "Secret Stone Button");
        //      LanguageRegistry.instance().addStringLocalization("SecretButton.wood", "Secret Wood Button");
        //      LanguageRegistry.instance().addStringLocalization("SecretButton.stone", "en_us", "Secret Stone Button");
        //      LanguageRegistry.instance().addStringLocalization("SecretButton.wood", "en_us", "Secret Wood Button");
        //
        //      LanguageRegistry.instance().addNameForObject(camoPlateAll, "en_US", "Secret PressurePlate");
        //      LanguageRegistry.instance().addNameForObject(camoPlatePlayer, "en_US", "Secret PlayerPlate");
        //      LanguageRegistry.instance().addNameForObject(camoPlateLight, "en_US", "Secret Weighted Plate (light)");
        //      LanguageRegistry.instance().addNameForObject(camoPlateHeavy, "en_US", "Secret Weighted Plate (heavy)");
        //
        //      LanguageRegistry.instance().addNameForObject(camoStairs, "en_US", "Secret Stairs");
        //
        //      LanguageRegistry.instance().addNameForObject(camoChest, "en_US", "Secret Chest");
        //      LanguageRegistry.instance().addNameForObject(camoTrappedChest, "en_US", "Secret Trapped Chest");
        //      LanguageRegistry.instance().addStringLocalization("container.CamoChest", "en_US", "Hidden Chest");
        //
        //      LanguageRegistry.instance().addNameForObject(camoLightDetector, "en_US", "Secret Light Detector");
        //
        //      LanguageRegistry.instance().addNameForObject(solidAir, "en_US", "Solid Air");
    }

    @EventHandler
    public void load(FMLInitializationEvent e)
    {
        // ore dictionary

        OreDictionary.registerOre(CAMO_PASTE, camoPaste);

        // Renders
        proxy.loadRenderStuff();

        addrecipes();
    }

    @EventHandler
    public void registerCommand(FMLServerStartingEvent e)
    {
        e.registerServerCommand(new CommandShow());
    }

    @EventHandler
    public void registerCommand(FMLServerStoppingEvent e)
    {
        proxy.onServerStop(e);
    }

    public static void addrecipes()
    {
        ArrayList<IRecipe> recipes = new ArrayList<IRecipe>();

        // Camo gate
        recipes.add(new ShapedOreRecipe(camoGate, new Object[] {
                "#0#",
                "0A0",
                "#@#",
                '#', Blocks.planks,
                '0', CAMO_PASTE,
                '@', Items.redstone,
                'A', Items.ender_pearl
        }));

        // TorchLever
        recipes.add(new ShapedOreRecipe(torchLever, new Object[] {
                "#",
                "X",
                '#', Blocks.torch,
                'X', Items.redstone
        }));

        // CamoDoors
        recipes.add(new ShapelessOreRecipe(camoDoorWoodItem,
                new Object[] {
                        CAMO_PASTE,
                        Items.wooden_door
                }));
        recipes.add(new ShapelessOreRecipe(camoDoorIronItem,
                new Object[] {
                        CAMO_PASTE,
                        Items.iron_door
                }));
        recipes.add(new ShapelessOreRecipe(camoTrapDoor,
                new Object[] {
                        CAMO_PASTE,
                        Blocks.trapdoor
                }));

        // CAMO_PASTE
        recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
                "XXX",
                "X0X",
                "XXX",
                'X', new ItemStack(Items.dye, 1, OreDictionary.WILDCARD_VALUE),
                '0', Blocks.dirt
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
                "XXX",
                "X0X",
                "XXX",
                'X', new ItemStack(Items.dye, 1, OreDictionary.WILDCARD_VALUE),
                '0', Blocks.sand
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoPaste, 9), new Object[] {
                "XXX",
                "X0X",
                "XXX",
                'X', new ItemStack(Items.dye, 1, OreDictionary.WILDCARD_VALUE),
                '0', Items.clay_ball
        }));

        // Camo OneWay
        recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] {
                "X00",
                "X00",
                "X00",
                'X', CAMO_PASTE,
                '0', Blocks.glass
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] {
                "00X",
                "00X",
                "00X",
                'X', CAMO_PASTE,
                '0', Blocks.glass
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] { "XXX",
                "000",
                "000",
                'X', CAMO_PASTE,
                '0', Blocks.glass
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(oneWay, 9), new Object[] { "000",
                "000",
                "XXX",
                'X', CAMO_PASTE,
                '0', Blocks.glass
        }));
        recipes.add(new ShapelessOreRecipe(oneWay, new Object[] {
                CAMO_PASTE, Blocks.glass }));

        // CamoGhost
        recipes.add(new ShapedOreRecipe(new ItemStack(camoGhost, 4), new Object[] {
                "X0X",
                "0 0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoGhost, 4), new Object[] {
                "X0X",
                "0 0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)
        }));

        // Camo-Redstone
        recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Items.redstone
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoCurrent, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Items.redstone
        }));

        // Camo-Lever
        recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.lever
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoLever, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.lever
        }));

        // Camo-Button Stone stuff
        recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4, 0), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.stone_button
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4, 0), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.stone_button
        }));

        // Camo-Button Wood stuff
        recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4, 1), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.wooden_button
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoButton, 4, 1), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.wooden_button
        }));

        // pressure plates
        recipes.add(new ShapedOreRecipe(camoPlateAll, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.stone_pressure_plate
        }));
        recipes.add(new ShapedOreRecipe(camoPlateAll, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.stone_pressure_plate
        }));

        recipes.add(new ShapedOreRecipe(camoPlatePlayer, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.stone_pressure_plate,
        }));
        recipes.add(new ShapedOreRecipe(camoPlatePlayer, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.stone_pressure_plate,
        }));

        // weighted pressure plates
        recipes.add(new ShapedOreRecipe(camoPlateLight, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.light_weighted_pressure_plate,
        }));
        recipes.add(new ShapedOreRecipe(camoPlateLight, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                // new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE)
                '@', Blocks.light_weighted_pressure_plate,
        }));

        recipes.add(new ShapedOreRecipe(camoPlateHeavy, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.heavy_weighted_pressure_plate,
        }));
        recipes.add(new ShapedOreRecipe(camoPlateHeavy, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.heavy_weighted_pressure_plate,
        }));

        // CamoStairs
        recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', "stairWood"
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', "stairWood"
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.stone_stairs
        }));
        recipes.add(new ShapedOreRecipe(new ItemStack(camoStairs, 4), new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.stone_stairs
        }));

        // CamoChests
        recipes.add(new ShapedOreRecipe(camoChest, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.chest
        }));
        recipes.add(new ShapedOreRecipe(camoChest, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.chest
        }));

        // Trapped Chests
        recipes.add(new ShapedOreRecipe(camoTrappedChest, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.trapped_chest
        }));
        recipes.add(new ShapedOreRecipe(camoTrappedChest, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.trapped_chest
        }));

        // Trapped Chests
        recipes.add(new ShapedOreRecipe(camoLightDetector, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', Items.rotten_flesh,
                '@', Blocks.daylight_detector
        }));
        recipes.add(new ShapedOreRecipe(camoLightDetector, new Object[] {
                "X0X",
                "0@0",
                "X0X",
                'X', CAMO_PASTE,
                '0', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE),
                '@', Blocks.daylight_detector
        }));

        // Solid Air
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.blaze_powder,
                        Items.water_bucket
                }));
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.blaze_rod,
                        Items.water_bucket
                }));
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.magma_cream,
                        Items.water_bucket
                }));
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.blaze_powder,
                        Items.potionitem
                }));
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.blaze_rod,
                        Items.potionitem
                }));
        recipes.add(new ShapelessOreRecipe(solidAir,
                new Object[] {
                        CAMO_PASTE,
                        Items.magma_cream,
                        Items.potionitem
                }));

        // actually add the recipe
        for (IRecipe r : recipes)
        {
            GameRegistry.addRecipe(r);
        }
    }
}
