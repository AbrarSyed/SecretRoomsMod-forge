package com.wynprice.secretroomsmod.handler;

import java.util.Map;
import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.wynprice.secretroomsmod.SecretConfig;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RecipeConfigHandler implements IConditionFactory
{
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> SecretConfig.singlePlayerHelmet;
	}
}
