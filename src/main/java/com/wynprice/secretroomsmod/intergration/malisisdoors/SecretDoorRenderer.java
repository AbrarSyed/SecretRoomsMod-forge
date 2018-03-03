package com.wynprice.secretroomsmod.intergration.malisisdoors;

import com.wynprice.secretroomsmod.SecretBlocks;

import net.malisis.core.renderer.RenderType;
import net.malisis.doors.block.Door;
import net.malisis.doors.block.FenceGate;
import net.malisis.doors.block.TrapDoor;
import net.malisis.doors.renderer.DoorRenderer;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SecretDoorRenderer extends DoorRenderer
{
	public SecretDoorRenderer() 
	{
		super(true);
		registerFor(SecretMalisisTileEntityDoor.class);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		ensureBlock(SecretMalisisDoor.class);
		rp.calculateAOColor.set(true);
	}
}
