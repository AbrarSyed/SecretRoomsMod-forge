package com.wynprice.secretroomsmod.integration.malisisdoors;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.SecretItemBlock;

import net.malisis.core.block.IRegisterable;
import net.malisis.core.registry.MalisisRegistry;
import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.DoorRegistry;
import net.malisis.doors.TrapDoorDescriptor;
import net.malisis.doors.movement.TrapDoorMovement;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SecretTrapDoorDescriptor extends TrapDoorDescriptor {
		
	public SecretTrapDoorDescriptor(String name, Material material) {
		this.setName(name);
		this.setTab(SecretRooms5.TAB);
		this.setTextureName(SecretRooms5.MODID, "items/" + name);
		this.setMaterial(material);
		this.setMovement(DoorRegistry.getMovement(TrapDoorMovement.class));
		Block trapDoor = new SecretMalisisTrapDoor(this);
		this.set(trapDoor, null);
	}
}
