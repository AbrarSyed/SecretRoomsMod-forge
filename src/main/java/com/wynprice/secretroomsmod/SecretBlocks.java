package com.wynprice.secretroomsmod;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.secretroomsmod.base.BaseBlockDoor;
import com.wynprice.secretroomsmod.base.SecretItemBlock;
import com.wynprice.secretroomsmod.blocks.GhostBlock;
import com.wynprice.secretroomsmod.blocks.OneWayGlass;
import com.wynprice.secretroomsmod.blocks.SecretButton;
import com.wynprice.secretroomsmod.blocks.SecretChest;
import com.wynprice.secretroomsmod.blocks.SecretDispenser;
import com.wynprice.secretroomsmod.blocks.SecretGate;
import com.wynprice.secretroomsmod.blocks.SecretGateEmptyBlock;
import com.wynprice.secretroomsmod.blocks.SecretLever;
import com.wynprice.secretroomsmod.blocks.SecretLightDetector;
import com.wynprice.secretroomsmod.blocks.SecretPlayerPressurePlate;
import com.wynprice.secretroomsmod.blocks.SecretPressurePlate;
import com.wynprice.secretroomsmod.blocks.SecretRedstone;
import com.wynprice.secretroomsmod.blocks.SecretStairs;
import com.wynprice.secretroomsmod.blocks.SecretTrapDoor;
import com.wynprice.secretroomsmod.blocks.SecretTrappedChest;
import com.wynprice.secretroomsmod.blocks.SecretWeightedPressurePlate;
import com.wynprice.secretroomsmod.blocks.SolidAir;
import com.wynprice.secretroomsmod.blocks.TorchLever;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SecretBlocks 
{
	
	public static final Block GHOST_BLOCK = new GhostBlock();
	public static final Block FAKE_STAIRS = new SecretStairs();
	public static final Block FAKE_LEVER = new SecretLever();
	public static final Block FAKE_REDSTONE = new SecretRedstone();
	public static final Block ONE_WAY_GLASS = new OneWayGlass();
	public static final Block SECRET_WOODEN_BUTTON = new SecretButton(true);
	public static final Block SECRET_STONE_BUTTON = new SecretButton(false);
	public static final Block TORCH_LEVER = new TorchLever();
	public static final Block SECRET_PRESSURE_PLATE = new SecretPressurePlate();
	public static final Block SECRET_LIGHT_PRESSURE_PLATE = new SecretWeightedPressurePlate("secret_light_pressure_plate", 15);
	public static final Block SECRET_HEAVY_PRESSURE_PLATE = new SecretWeightedPressurePlate("secret_heavy_pressure_plate", 150);
	public static final Block SECRET_PLAYER_PRESSURE_PLATE = new SecretPlayerPressurePlate();
	public static final Block SOLID_AIR = new SolidAir();
	public static final Block SECRET_CHEST = new SecretChest("secret_chest");
	public static final BaseBlockDoor SECRET_WOODEN_DOOR = new BaseBlockDoor("secret_wooden_door", Material.WOOD);
	public static final BaseBlockDoor SECRET_IRON_DOOR = new BaseBlockDoor("secret_iron_door", Material.IRON);
	public static final Block SECRET_WOODEN_TRAPDOOR = new SecretTrapDoor("secret_wooden_trapdoor", Material.WOOD);
	public static final Block SECRET_IRON_TRAPDOOR = new SecretTrapDoor("secret_iron_trapdoor", Material.IRON);
	public static final Block SECRET_DISPENSER = new SecretDispenser();
	public static final Block SECRET_TRAPPED_CHEST = new SecretTrappedChest();
	public static final Block SECRET_GATE_BLOCK = new SecretGateEmptyBlock();
	public static final Block SECRET_GATE = new SecretGate();
	public static final SecretLightDetector SECRET_LIGHT_DETECTOR = new SecretLightDetector(false);
	public static final SecretLightDetector SECRET_LIGHT_DETECTOR_INVERTED = new SecretLightDetector(true);

	
	public static void preInit()
	{
		regBlock(GHOST_BLOCK);
		regBlockIgnoreAll(FAKE_STAIRS);
		regBlockIgnoreAll(FAKE_LEVER);
		regBlockIgnoreAll(FAKE_REDSTONE);
		regBlockIgnoreAll(ONE_WAY_GLASS);
		regBlockIgnoreAll(SECRET_WOODEN_BUTTON);
		regBlockIgnoreAll(SECRET_STONE_BUTTON);
		regBlock(TORCH_LEVER, 64, TorchLever.POWERED);
		regBlockIgnoreAll(SECRET_PRESSURE_PLATE);
		regBlockIgnoreAll(SECRET_LIGHT_PRESSURE_PLATE);
		regBlockIgnoreAll(SECRET_HEAVY_PRESSURE_PLATE);
		regBlockIgnoreAll(SECRET_PLAYER_PRESSURE_PLATE);
		regBlock(SOLID_AIR);
		regBlock(SECRET_CHEST);
		regSingleBlockIgnoreAll(SECRET_WOODEN_DOOR);
		regSingleBlockIgnoreAll(SECRET_IRON_DOOR);
		regBlockIgnoreAll(SECRET_WOODEN_TRAPDOOR);
		regBlockIgnoreAll(SECRET_IRON_TRAPDOOR);
		regBlockIgnoreAll(SECRET_DISPENSER);
		regBlock(SECRET_TRAPPED_CHEST);
		regSingleBlock(SECRET_GATE_BLOCK);
		regBlockIgnoreAll(SECRET_GATE);
		regBlockIgnoreAll(SECRET_LIGHT_DETECTOR);
		regSingleBlockIgnoreAll(SECRET_LIGHT_DETECTOR_INVERTED);
	}
	
	private final static ArrayList<Block> BLOCKS_WITH_ITEMS = new ArrayList<Block>();
	private final static HashMap<Block, Integer> BLOCK_STACK_SIZES = new HashMap<>();
	private final static ArrayList<Block> BLOCKS_WITH_CUSTOM_STATE_MAP = new ArrayList<Block>();
	private final static ArrayList<IProperty<?>[]> PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP = new ArrayList<IProperty<?>[]>();
	
	public static void regRenders() {
		for(int i = 0; i < BLOCKS_WITH_CUSTOM_STATE_MAP.size(); i++)
			createStateMappers(BLOCKS_WITH_CUSTOM_STATE_MAP.get(i), PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.get(i));
		for(Block b : BLOCKS_WITH_ITEMS)
			regRender(b);
	}

	private static void regBlock(Block block) 
	{
		regBlock(block, 64);
	}

	private static void regBlock(Block block, int stackSize) 
	{
		BLOCKS_WITH_ITEMS.add(block);
		BLOCK_STACK_SIZES.put(block, stackSize);	
		block.setCreativeTab(SecretRooms5.TAB);
		register(block);
	}
	
	private static void regBlockIgnoreAll(Block block)
	{
		regBlockIgnoreAll(block, 64);
	}
	
	private static void regBlockIgnoreAll(Block block, int stackSize)
	{
		regBlock(block, stackSize, block.getDefaultState().getProperties().asMultimap().asMap().keySet().toArray(new IProperty[block.getDefaultState().getProperties().asMultimap().asMap().keySet().size()]));
	}
	
	private static void regSingleBlockIgnoreAll(Block block)
	{
		regSingleBlock(block, block.getDefaultState().getProperties().asMultimap().asMap().keySet().toArray(new IProperty[block.getDefaultState().getProperties().asMultimap().asMap().keySet().size()]));
	}
	
	private static void regBlock(Block block, int stackSize, IProperty<?>... toIgnore)
	{
		BLOCKS_WITH_CUSTOM_STATE_MAP.add(block);
		PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.add(toIgnore);
		regBlock(block, stackSize);
	}
	
	@SideOnly(Side.CLIENT)
	private static void createStateMappers(Block block, IProperty<?>[] toIgnore)
	{
		ModelLoader.setCustomStateMapper(block, (new StateMap.Builder().ignore(toIgnore)).build());
	}
	
	private static void regSingleBlock(Block block)
	{
		register(block);
	}
	
	private static void regSingleBlock(Block block,  IProperty<?>... toIgnore)
	{
		BLOCKS_WITH_CUSTOM_STATE_MAP.add(block);
		PROPERTIES_TO_IGNORE_CUSTOM_STATE_MAP.add(toIgnore);
		register(block);
	}

	private static void regRender(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	private static void register(Block block)
	{
		ForgeRegistries.BLOCKS.register(block);
		if(BLOCKS_WITH_ITEMS.contains(block))
		{
			ItemBlock item = new SecretItemBlock(block);
			item.setRegistryName(block.getRegistryName());
			item.setMaxStackSize(BLOCK_STACK_SIZES.get(block));
			ForgeRegistries.ITEMS.register(item);
		}
	}
}
