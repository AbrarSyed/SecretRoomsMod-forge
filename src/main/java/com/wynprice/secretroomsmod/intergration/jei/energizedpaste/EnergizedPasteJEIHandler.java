package com.wynprice.secretroomsmod.intergration.jei.energizedpaste;

import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.item.ItemStack;

public class EnergizedPasteJEIHandler implements IRecipeWrapperFactory<EnergizedPasteRecipe>
{

	@Override
	public IRecipeWrapper getRecipeWrapper(EnergizedPasteRecipe recipe) 
	{
		return new EnergizedPasteWrapper(recipe);
	}

}
