/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.abrarsyed.secretroomsmod.malisisdoors;

import net.malisis.doors.MalisisDoors;
import net.malisis.doors.door.DoorDescriptor;
import net.malisis.doors.door.item.DoorItem;
import net.minecraft.block.material.Material;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author Ordinastie
 *
 */
public class MalisisDoorsCompat
{

	public static boolean isCompatible()
	{
		if (!Loader.isModLoaded("malisisdoors"))
			return false;

		if (!MalisisDoors.version.startsWith("1.7.10-1.3."))
			return false;

		return true;
	}

	public static void preInit()
	{

		//wood
		DoorDescriptor desc = new DoorDescriptor();
		desc.setName("SecretWoodenDoorBlock");
		desc.setTextureName(SecretRooms.TEXTURE_ITEM_DOOR_WOOD);
		desc.setTab(SecretRooms.tab);

		SecretRooms.camoDoorWood = new CamoDoor(desc);
		SecretRooms.camoDoorWoodItem = new DoorItem(desc);

		desc.set(SecretRooms.camoDoorWood, SecretRooms.camoDoorWoodItem);

		//iron
		desc = new DoorDescriptor();
		desc.setMaterial(Material.iron);
		desc.setName("SecretIronDoorBlock");
		desc.setTextureName(SecretRooms.TEXTURE_ITEM_DOOR_STEEL);
		desc.setTab(SecretRooms.tab);
		desc.setRequireRedstone(true);

		SecretRooms.camoDoorIron = new CamoDoor(desc);
		SecretRooms.camoDoorIronItem = new DoorItem(desc);

		desc.set(SecretRooms.camoDoorIron, SecretRooms.camoDoorIronItem);

		CamoDoorRenderer cdr = new CamoDoorRenderer();
		cdr.registerFor(CamoDoorTileEntity.class);

		GameRegistry.registerTileEntity(CamoDoorTileEntity.class, "TE_CamoDoor");

		//trap door
		SecretRooms.camoTrapDoor = new CamoTrapDoor().setBlockName("SecretTrapDoor").setCreativeTab(SecretRooms.tab);
		CamoTrapDoorRenderer ctdr = new CamoTrapDoorRenderer();
		ctdr.registerFor(CamoTrapDoor.class);
	}

}
