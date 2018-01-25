package com.wynprice.secretroomsmod.intergration.jei.energizedpaste;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class EnergizedPasteRecipe 
{
	private final ArrayList<ItemStack> inputs;
	
	private final ItemStack output;
	
	private final TextureAtlasSprite backgroundSprite;
	
	public EnergizedPasteRecipe(ItemStack input1, ItemStack input2, ItemStack input3, ItemStack input4, ItemStack input5, ItemStack output, TextureAtlasSprite backgroundSprite) 
	{
		inputs = Lists.newArrayList(input1, input2, input3, input4, input5);
		this.output = output;
		this.backgroundSprite = backgroundSprite;
	}
	
	public ItemStack getOutput() {
		return output;
	}
	
	public List<List<ItemStack>> getInputs()
	{
		List<List<ItemStack>> list = new ArrayList<>();
		for(ItemStack stack : this.inputs)
			list.add(Lists.newArrayList(stack));
		return list;
	}
	
	public TextureAtlasSprite getBackgroundSprite() {
		return backgroundSprite;
	}
}
