package com.wynprice.secretroomsmod;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class SecretRecipes
{
	public static void init()
	{
	    ArrayList<IRecipe> recipes = new ArrayList<>();
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:camouflage_paste"), 9), "XXX", "X#X", "XXX", 'X', "dye", '#', Item.getByNameOrId("minecraft:clay_ball")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:camouflage_paste"), 9), "XXX", "X#X", "XXX", 'X', "dye", '#', "dirt"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:camouflage_paste"), 9), "XXX", "X#X", "XXX", 'X', "dye", '#', "sand"));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:diamond_exposing_helmet"), 1), Item.getByNameOrId("minecraft:diamond_helmet"), Item.getByNameOrId("secretroomsmod:switch_probe")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:iron_exposing_helmet"), 1), Item.getByNameOrId("minecraft:iron_helmet"), Item.getByNameOrId("secretroomsmod:switch_probe")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:ghost_block"), 4), "X0X", "0 0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:ghost_block"), 4), "X0X", "0 0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:one_way_glass"), 9), "XXX", "000", "000", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', "blockGlassColorless"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:one_way_glass"), 9), "X00", "X00", "X00", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', "blockGlassColorless"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:one_way_glass"), 9), "00X", "00X", "00X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', "blockGlassColorless"));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:one_way_glass"), 1), Item.getByNameOrId("secretroomsmod:camouflage_paste"), "blockGlassColorless"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:one_way_glass"), 9), "000", "000", "XXX", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '0', "blockGlassColorless"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:programmable_switch_probe"), 1), "EXD", "XSX", "GXI", 'S', Item.getByNameOrId("secretroomsmod:switch_probe"), 'E', "gemEmerald", 'D', "gemDiamond", 'G', "ingotGold", 'I', "ingotIron", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_chest"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "chestWood", '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_chest"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "chestWood", '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_dispenser"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:dispenser"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_dispenser"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:dispenser"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_gate"), 1), "X0X", "0A0", "X@X", 'A', "enderpearl", '0', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "dustRedstone", 'X', "plankWood"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_heavy_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:heavy_weighted_pressure_plate"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_heavy_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:heavy_weighted_pressure_plate"), '0', "blockWool"));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_iron_door"), 1), Item.getByNameOrId("secretroomsmod:camouflage_paste"), Item.getByNameOrId("minecraft:iron_door")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_iron_trapdoor"), 1), Item.getByNameOrId("secretroomsmod:camouflage_paste"), Item.getByNameOrId("minecraft:iron_trapdoor")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_lever"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:lever"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_lever"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:lever"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_light_detector"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:daylight_detector"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_light_detector"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:daylight_detector"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_light_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:light_weighted_pressure_plate"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_light_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:light_weighted_pressure_plate"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_player_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:stone_pressure_plate"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_player_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:stone_pressure_plate"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:wooden_pressure_plate"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_pressure_plate"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:wooden_pressure_plate"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_redstone"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "dustRedstone", '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_redstone"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "dustRedstone", '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_stairs"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "blockStair", '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_stairs"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "blockStair", '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_stone_button"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:stone_button"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_stone_button"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:stone_button"), '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_trapped_chest"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "chestTrapped", '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_trapped_chest"), 1), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', "chestTrapped", '0', "blockWool"));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_wooden_button"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:wooden_button"), '0', Item.getByNameOrId("minecraft:rotten_flesh")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_wooden_button"), 4), "X0X", "0@0", "X0X", 'X', Item.getByNameOrId("secretroomsmod:camouflage_paste"), '@', Item.getByNameOrId("minecraft:wooden_button"), '0', "blockWool"));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_wooden_door"), 1), Item.getByNameOrId("secretroomsmod:camouflage_paste"), "doorWooden"));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:secret_wooden_trapdoor"), 1), Item.getByNameOrId("secretroomsmod:camouflage_paste"), Item.getByNameOrId("minecraft:trapdoor")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:solid_air"), 1), Item.getByNameOrId("minecraft:water_bucket"), Item.getByNameOrId("minecraft:blaze_powder")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:solid_air"), 1), Item.getByNameOrId("minecraft:water_bucket"), Item.getByNameOrId("minecraft:blaze_rod")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:solid_air"), 1), Item.getByNameOrId("minecraft:water_bucket"), Item.getByNameOrId("minecraft:magma_cream")));
	    recipes.add(new ShapelessOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:switch_probe"), 1), Item.getByNameOrId("minecraft:redstone_torch"), Item.getByNameOrId("secretroomsmod:camouflage_paste")));
	    recipes.add(new ShapedOreRecipe(new ItemStack(Item.getByNameOrId("secretroomsmod:torch_lever"), 1), "T", "L", 'T', "torch", 'L', Item.getByNameOrId("minecraft:lever")));
        for(IRecipe recipe : recipes)
        	GameRegistry.addRecipe(recipe);
	}
}
