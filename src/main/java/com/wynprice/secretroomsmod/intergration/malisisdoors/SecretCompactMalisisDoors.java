package com.wynprice.secretroomsmod.intergration.malisisdoors;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.DoorDescriptor.RedstoneBehavior;
import net.malisis.doors.item.DoorItem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;

public class SecretCompactMalisisDoors 
{
	
	public static final boolean ENABLED = Loader.isModLoaded("malisisdoors");
	
	private static final DoorDescriptor wooden_desc = new DoorDescriptor();
	private static final DoorDescriptor iron_desc = new DoorDescriptor();

	static {
		wooden_desc.setName("secret_wooden_door");
		wooden_desc.setTab(SecretRooms5.TAB);
		wooden_desc.setTextureName(SecretRooms5.MODID, "secret_wooden_door");
		
		iron_desc.setName("secret_iron_door");
		iron_desc.setTab(SecretRooms5.TAB);
		iron_desc.setTextureName(SecretRooms5.MODID, "secret_iron_door");
		iron_desc.setRedstoneBehavior(RedstoneBehavior.REDSTONE_ONLY);
	}
	
	public static final Block WOODEN_DOOR = new SecretMalisisDoor(wooden_desc);
	public static final Item WOODEN_DOOR_ITEM = new SecretMalisiItemDoor(wooden_desc);
	
	public static final Block IRON_DOOR = new SecretMalisisDoor(iron_desc);
	public static final Item IRON_DOOR_ITEM = new SecretMalisiItemDoor(iron_desc);
	
	static {
		wooden_desc.set(WOODEN_DOOR, WOODEN_DOOR_ITEM);
		iron_desc.set(IRON_DOOR, IRON_DOOR_ITEM);
	}
}
