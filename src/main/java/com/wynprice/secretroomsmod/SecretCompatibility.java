package com.wynprice.secretroomsmod;

import net.minecraftforge.fml.common.Loader;

public class SecretCompatibility
{
	public static final boolean MALISISDOORS = isLoaded("malisisdoors");

	private static boolean isLoaded(String modid) {
		boolean loaded = Loader.isModLoaded(modid);
		if(loaded) {
			SecretRooms5.LOGGER.info("Enabling support for: " + modid);
		}
		return loaded;
	}
}
