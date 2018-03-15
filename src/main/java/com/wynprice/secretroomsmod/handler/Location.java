package com.wynprice.secretroomsmod.handler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Simple position holder
 * @author Wyn Price
 *
 */
public class Location {
	private final IBlockAccess world;
	private final BlockPos position;
	
	public Location(IBlockAccess world, BlockPos position) {
		this.world = world;
		this.position = position;
	}
	
	public IBlockAccess getWorld() {
		return world;
	}
	
	public BlockPos getPosition() {
		return position;
	}
	
}
