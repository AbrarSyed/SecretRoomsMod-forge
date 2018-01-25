package com.wynprice.secretroomsmod.intergration.jei;

import java.util.Arrays;

import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.intergration.jei.energizedpaste.EnergizedPasteCategory;
import com.wynprice.secretroomsmod.intergration.jei.energizedpaste.EnergizedPasteJEIHandler;
import com.wynprice.secretroomsmod.intergration.jei.energizedpaste.EnergizedPasteRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class SecretRoomsJEI implements IModPlugin
{
	public static final String ENERGIZED_PASTE_UUID = "secretroomsmod:energizedpaste";
	
	@Override
	public void register(IModRegistry registry) {
		registry.handleRecipes(EnergizedPasteRecipe.class, new EnergizedPasteJEIHandler(), ENERGIZED_PASTE_UUID);
		registry.addRecipeCatalyst(new ItemStack(Items.FLINT_AND_STEEL), ENERGIZED_PASTE_UUID);
		registry.addRecipes(Lists.newArrayList(new EnergizedPasteRecipe(new ItemStack(SecretItems.CAMOUFLAGE_PASTE), new ItemStack(SecretItems.CAMOUFLAGE_PASTE), new ItemStack(SecretItems.CAMOUFLAGE_PASTE), new ItemStack(SecretItems.CAMOUFLAGE_PASTE), new ItemStack(SecretItems.CAMOUFLAGE_PASTE), new ItemStack(SecretItems.CAMOUFLAGE_PASTE, 1, 1),  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.FIRE.getDefaultState()))), ENERGIZED_PASTE_UUID);

	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new EnergizedPasteCategory(registry)
		);
	}
	
}
