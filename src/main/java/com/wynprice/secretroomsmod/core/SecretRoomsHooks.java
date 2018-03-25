package com.wynprice.secretroomsmod.core;

import java.util.List;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecretRoomsHooks {
	/**
	 * Causes {@link IBlockProperties#addCollisionBoxToList(World, BlockPos, net.minecraft.util.math.AxisAlignedBB, java.util.List, net.minecraft.entity.Entity, boolean)} to be run through here.
	 * This means that if a block is being overridden with Energized Paste, it will have the correct collision box	 
	 * @param block The Block used. Not needed however its already loaded, and unloading it would need more asm
	 * @param state The state that that this is being called from
	 * @param worldIn The world
	 * @param pos The position
	 * @param entityBox The Entities Bounding Box
	 * @param collidingBoxes The list of colliding boxes to add to
	 * @param entityIn The Entity thats colliding
	 * @param isActualState If its the actual state or not
	 */
	public static void addCollisionBoxToList(Block block, IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
		if(EnergizedPasteHandler.hasReplacedState(worldIn, pos)) {
			IBlockState energizedState = EnergizedPasteHandler.getReplacedState(worldIn, pos);
			try {
				energizedState = energizedState.getActualState(worldIn, pos);
			} catch (Exception e) {
				;
			}
			energizedState.getBlock().addCollisionBoxToList(energizedState, worldIn, pos, entityBox, collidingBoxes, entityIn, true);
		} else {
			block.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
		}
    }
}
