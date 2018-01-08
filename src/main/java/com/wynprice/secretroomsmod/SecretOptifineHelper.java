package com.wynprice.secretroomsmod;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.apache.logging.log4j.core.util.Loader;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.proxy.ClientProxy;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;

public class SecretOptifineHelper 
{
	public static final boolean IS_OPTIFINE = getIsOptifine();
	
	private static boolean getIsOptifine()
	{
		if(!Loader.isClassAvailable("ChunkCacheOF"))
			return false;
		Field field;
		try {
			field = Class.forName("ChunkCacheOF").getDeclaredField("cacheBlockStates");
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			return false;
		}
		field.setAccessible(true);
		if(!Modifier.isFinal(field.getModifiers()))
			return true;
		return false;
	}
	
	public static boolean resetCached_C6()
	{
		try
		{
			Field field = Class.forName("ChunkCacheOF").getDeclaredField("cacheBlockStates");
			field.setAccessible(true);
			if(field.get(null).getClass() != ClientProxy.secretOptifine.getClass())
				try {
					field.set(null, ClientProxy.secretOptifine.getClass().newInstance());
				}
				catch (IllegalAccessException e) {
					return false;
				}

			field.setAccessible(false);
			return true;
		}
		catch (Throwable e) 
		{
			return false;
		}
	}
}
