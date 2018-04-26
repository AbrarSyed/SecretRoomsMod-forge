package com.wynprice.secretroomsmod.core;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class SecretRoomsHooksDummyMod extends DummyModContainer {
	public static final String MODID = "srm-hooks";
	public static final String MODNAME = "SecretRooms Hooks";
	public static final String VERSION = "1.12.2-1.0.0";
	
	public SecretRoomsHooksDummyMod() {
		super(new ModMetadata());
		ModMetadata data = getMetadata();
		data.modId = MODID;
		data.name = MODNAME;
		data.description = "Hook Class used for SecretRoomsMod. Without this, SecretRoomsMod will not function fully";
		data.version = VERSION;
		data.screenshots = new String[0];	
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
}
