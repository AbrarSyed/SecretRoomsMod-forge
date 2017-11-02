package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.base.BaseTERender;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class OneWayGlassFakeModel extends FakeBlockModel
{
	
	public OneWayGlassFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		if(side != null && BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos.offset(side)).getBlock() == SecretBlocks.ONE_WAY_GLASS)
			return new ArrayList<BakedQuad>();
		return BaseTERender.currentRender != null && BaseTERender.currentRender.getBlock() == SecretBlocks.ONE_WAY_GLASS && 
				side != BaseTERender.currentRender.getValue(BlockDirectional.FACING)? getModel(Blocks.GLASS.getDefaultState()).getQuads(state, side, rand) : super.getQuads(state, side, rand);
	}
}
