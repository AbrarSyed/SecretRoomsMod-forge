package com.wynprice.secretroomsmod.integration.malisisdoors;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.handler.HandlerModContainer;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.blocks.SecretMalisisDoorBlock;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.blocks.SecretMalisisTrapDoorBlock;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.items.SecretMalisiItemDoor;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.tileentities.SecretMalisisTileEntityDoor;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.tileentities.SecretMalisisTileEntityTrapDoor;

import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.DoorDescriptor.RedstoneBehavior;
import net.malisis.doors.DoorRegistry;
import net.malisis.doors.MalisisDoors;
import net.malisis.doors.TrapDoorDescriptor;
import net.malisis.doors.movement.TrapDoorMovement;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SecretCompactMalisisDoors 
{	
	private static final DoorDescriptor wooden_desc = createDoorDescriptor(DoorDescriptor.class, () -> {
		Loader.instance().setActiveModContainer(HandlerModContainer.getContainer(MalisisDoors.modid)); //Needed to stop log spam. Because DoorRegistry has a static initilizer, it will be loaded here.
		DoorDescriptor door = new DoorDescriptor();
		Loader.instance().setActiveModContainer(HandlerModContainer.getContainer(SecretRooms5.MODID));
		door.setName("secret_wooden_door");
		door.setTab(SecretRooms5.TAB);
		door.setTextureName(SecretRooms5.MODID, "secret_wooden_door");
		return door;
	});
	
	private static final DoorDescriptor iron_desc = createDoorDescriptor(DoorDescriptor.class, () -> {
		DoorDescriptor door = new DoorDescriptor();
		door.setName("secret_iron_door");
		door.setTab(SecretRooms5.TAB);
		door.setTextureName(SecretRooms5.MODID, "secret_iron_door");
		door.setRedstoneBehavior(RedstoneBehavior.REDSTONE_ONLY);
		return door;
	});
	
	private static final TrapDoorDescriptor wooden_trap_desc = createDoorDescriptor(TrapDoorDescriptor.class, () -> {
		TrapDoorDescriptor desc = new TrapDoorDescriptor();
		desc.setName("secret_wooden_trapdoor");
		desc.setTab(SecretRooms5.TAB);
		desc.setTextureName(SecretRooms5.MODID, "items/secret_wooden_trapdoor"); //Not needed but removes errors in log
		desc.setMaterial(Material.WOOD);
		desc.setMovement(DoorRegistry.getMovement(TrapDoorMovement.class));
		return desc;
	});
	
	private static final TrapDoorDescriptor iron_trap_desc = createDoorDescriptor(TrapDoorDescriptor.class, () -> {
		TrapDoorDescriptor desc = new TrapDoorDescriptor();
		desc.setName("secret_iron_trapdoor");
		desc.setTab(SecretRooms5.TAB);
		desc.setTextureName(SecretRooms5.MODID, "items/secret_iron_trapdoor");
		desc.setMaterial(Material.IRON);
		desc.setRedstoneBehavior(RedstoneBehavior.REDSTONE_ONLY);
		desc.setMovement(DoorRegistry.getMovement(TrapDoorMovement.class));
		return desc;
	});
		
	public static final Block WOODEN_DOOR = new SecretMalisisDoorBlock(wooden_desc);
	public static final Item WOODEN_DOOR_ITEM = new SecretMalisiItemDoor(wooden_desc);
	
	public static final Block IRON_DOOR = new SecretMalisisDoorBlock(iron_desc);
	public static final Item IRON_DOOR_ITEM = new SecretMalisiItemDoor(iron_desc);
	
	public static final Block WOODEN_TRAPDOOR = new SecretMalisisTrapDoorBlock(wooden_trap_desc);
	public static final Block IRON_TRAPDOOR = new SecretMalisisTrapDoorBlock(iron_trap_desc);

	
	private static <T extends DoorDescriptor> T createDoorDescriptor(Class<T> clas, DescriptorFactory<T>  factory) {
		return factory.getDescriptor();
	}
	
	public static void registerTileEntity() {
		GameRegistry.registerTileEntity(SecretMalisisTileEntityDoor.class, SecretRooms5.MODID + SecretMalisisTileEntityDoor.class.getSimpleName());
		GameRegistry.registerTileEntity(SecretMalisisTileEntityTrapDoor.class, SecretRooms5.MODID + SecretMalisisTileEntityTrapDoor.class.getSimpleName());

	}
}
