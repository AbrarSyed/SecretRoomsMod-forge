package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.Arrays;
import java.util.List;

import com.wynprice.secretroomsmod.base.BaseFakeBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class OneSidedRender extends FakeBlockModel
{
	
	private final EnumFacing face;

	public OneSidedRender(IBlockState overstate, EnumFacing face) {
		super(overstate);
		this.face = face;
	}
	
	public OneSidedRender(IBakedModel overstate, EnumFacing face) {
		super(overstate);
		this.face = face;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		return side == face ? super.getQuads(state, side, rand) : Arrays.asList();
	}

}
