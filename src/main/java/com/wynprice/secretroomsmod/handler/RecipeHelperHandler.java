package com.wynprice.secretroomsmod.handler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHelperHandler 
{
	public static void preInit()
	{
		OreDictionary.registerOre("doorWooden", Items.OAK_DOOR);
		OreDictionary.registerOre("doorWooden", Items.BIRCH_DOOR);
		OreDictionary.registerOre("doorWooden", Items.ACACIA_DOOR);
		OreDictionary.registerOre("doorWooden", Items.DARK_OAK_DOOR);
		OreDictionary.registerOre("doorWooden", Items.JUNGLE_DOOR);
		
		OreDictionary.registerOre("blockWool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
		
		OreDictionary.registerOre("blockStair",   Blocks.OAK_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.SPRUCE_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.BIRCH_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.JUNGLE_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.ACACIA_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.DARK_OAK_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.STONE_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.STONE_BRICK_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.PURPUR_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.BRICK_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.NETHER_BRICK_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.QUARTZ_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.RED_SANDSTONE_STAIRS);
		OreDictionary.registerOre("blockStair",   Blocks.SANDSTONE_STAIRS);

	}
}
