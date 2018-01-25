package com.wynprice.secretroomsmod.intergration.jei.energizedpaste;

import java.awt.Point;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.intergration.jei.SecretRoomsJEI;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnergizedPasteCategory implements IRecipeCategory
{
	
	protected final IDrawable background;
	protected final IDrawable overlay;

	
	public EnergizedPasteCategory(IRecipeCategoryRegistration reg)
	{
		background = reg.getJeiHelpers().getGuiHelper().createBlankDrawable(150, 110);
		overlay = reg.getJeiHelpers().getGuiHelper().createDrawable(new ResourceLocation(SecretRooms5.MODID, "textures/gui/jei/energized_paste_cover.png"), 0, 0, 150, 110, 150, 110);

	} 
	
	@Override
	public String getUid() {
		return  SecretRoomsJEI.ENERGIZED_PASTE_UUID;
	}

	@Override
	public String getTitle() {
		return I18n.format("jei.energizedpaste.name");
	}

	@Override
	public String getModName() {
		return SecretRooms5.MODID;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	private TextureAtlasSprite textureSprite;

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapperIn, IIngredients ingredients) 
	{
		if(!(recipeWrapperIn instanceof EnergizedPasteWrapper))
			return;
		EnergizedPasteWrapper recipeWrapper = ((EnergizedPasteWrapper)recipeWrapperIn);
		textureSprite = recipeWrapper.getBackgroundSprite();
		int[] points = 
			{
				8, 13,
				26, 13,
				44, 13,
				62, 13,
				80, 13
			};
		for(int i = 0; i < 5; i++)
		{
			recipeLayout.getItemStacks().init(i, true, points[i*2], points[(i*2)+1]);
			recipeLayout.getItemStacks().set(i, ingredients.getInputs(ItemStack.class).get(i));
		}
		
		recipeLayout.getItemStacks().init(5, false, 127, 45);
		recipeLayout.getItemStacks().set(5, ingredients.getOutputs(ItemStack.class).get(0));
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		overlay.draw(minecraft);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(25, 30, textureSprite, 75, 75);
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}

}
