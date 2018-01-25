package com.wynprice.secretroomsmod.intergration.jei.energizedpaste;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class EnergizedPasteWrapper implements IRecipeWrapper
{
	
	private final EnergizedPasteRecipe recipe;
	
	
	public EnergizedPasteWrapper(EnergizedPasteRecipe recipe) 
	{
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputLists(ItemStack.class, recipe.getInputs());
		ingredients.setOutput(ItemStack.class, recipe.getOutput());
	}
	
	public TextureAtlasSprite getBackgroundSprite() {
		return recipe.getBackgroundSprite();
	}

}
