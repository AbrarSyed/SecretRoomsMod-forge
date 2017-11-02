package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.blocks.SecretTrapDoor;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class TrapDoorFakeModel extends BaseTextureFakeModel
{

	public TrapDoorFakeModel(FakeBlockModel model) {
		super(model);

	}
	
	
	@Override
	protected EnumFacing[] fallbackOrder() {
		EnumFacing[] list = {
				EnumFacing.DOWN,
				EnumFacing.NORTH,
				EnumFacing.EAST,
				EnumFacing.SOUTH,
				EnumFacing.WEST,
				null,
				EnumFacing.UP
		};
		return list;
	}

	@Override
	public IBlockState getNormalStateWith(IBlockState s) {
		return Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.FACING, s.getValue(BlockTrapDoor.FACING)).withProperty(BlockTrapDoor.HALF, s.getValue(BlockTrapDoor.HALF))
				.withProperty(BlockTrapDoor.OPEN, s.getValue(BlockTrapDoor.OPEN));
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return SecretTrapDoor.class;
	}

}
