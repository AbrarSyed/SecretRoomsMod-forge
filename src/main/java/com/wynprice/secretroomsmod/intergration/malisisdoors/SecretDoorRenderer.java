package com.wynprice.secretroomsmod.intergration.malisisdoors;

import com.wynprice.secretroomsmod.SecretBlocks;

import net.malisis.doors.block.Door;
import net.malisis.doors.block.FenceGate;
import net.malisis.doors.block.TrapDoor;
import net.malisis.doors.renderer.DoorRenderer;
import net.malisis.doors.tileentity.DoorTileEntity;

public class SecretDoorRenderer extends DoorRenderer
{
	public SecretDoorRenderer() 
	{
		super(true);
		registerFor(SecretMalisisTileEntityDoor.class);
		ensureBlock(SecretMalisisDoor.class);
	}
	
	@Override
	public void render() {
		System.out.println("sdasd");
		super.render();
	}
}
