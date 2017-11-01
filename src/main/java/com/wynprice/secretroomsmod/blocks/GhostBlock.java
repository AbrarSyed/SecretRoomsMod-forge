package com.wynprice.secretroomsmod.blocks;

import java.util.List;

import com.wynprice.secretroomsmod.base.BaseFakeBlock;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GhostBlock extends BaseFakeBlock
{

	public GhostBlock() {
		super("ghost_block", Material.ROCK);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) 
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, NULL_AABB);
	}
}
