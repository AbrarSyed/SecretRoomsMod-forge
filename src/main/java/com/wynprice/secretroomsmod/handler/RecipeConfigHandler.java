package com.wynprice.secretroomsmod.handler;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.wynprice.secretroomsmod.SecretConfig;

import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeConfigHandler implements IConditionFactory
{
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		return () -> SecretConfig.GENERAL.survivalModeHelmet;
	}
}
