package com.wynprice.secretroomsmod.integration.malisisdoors;

import com.google.common.base.Function;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.handler.HandlerModContainer;

import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.MalisisDoors;
import net.malisis.doors.DoorDescriptor.RedstoneBehavior;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ModContainerFactory;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SecretCompactMalisisDoors 
{	
	private static final DoorDescriptor wooden_desc = createDoorDescriptor(() -> {
		Loader.instance().setActiveModContainer(HandlerModContainer.getContainer(MalisisDoors.modid)); //Needed to stop log spam. Because DoorRegistry has a static initilizer, it will be loaded here.
		DoorDescriptor door = new DoorDescriptor();
		Loader.instance().setActiveModContainer(HandlerModContainer.getContainer(SecretRooms5.MODID));
		door.setName("secret_wooden_door");
		door.setTab(SecretRooms5.TAB);
		door.setTextureName(SecretRooms5.MODID, "secret_wooden_door");
		return door;
	});
	
	private static final DoorDescriptor iron_desc = createDoorDescriptor(() -> {
		DoorDescriptor door = new DoorDescriptor();
		door.setName("secret_iron_door");
		door.setTab(SecretRooms5.TAB);
		door.setTextureName(SecretRooms5.MODID, "secret_iron_door");
		door.setRedstoneBehavior(RedstoneBehavior.REDSTONE_ONLY);
		return door;
	});
	
	public static final Block WOODEN_DOOR = new SecretMalisisDoor(wooden_desc);
	public static Item WOODEN_DOOR_ITEM = new SecretMalisiItemDoor(wooden_desc);
	
	public static final Block IRON_DOOR = new SecretMalisisDoor(iron_desc);
	public static Item IRON_DOOR_ITEM = new SecretMalisiItemDoor(iron_desc);
	
	
	private static DoorDescriptor createDoorDescriptor(DoorDescriptorFactory factory) {
		return factory.getDescriptor();
	}
	
	public static void registerTileEntity() {
		GameRegistry.registerTileEntity(SecretMalisisTileEntityDoor.class, SecretRooms5.MODID + SecretMalisisTileEntityDoor.class.getSimpleName());
	}
}
