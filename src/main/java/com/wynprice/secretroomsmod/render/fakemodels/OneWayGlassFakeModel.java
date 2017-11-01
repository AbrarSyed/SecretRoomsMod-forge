package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.render.TileEntityInfomationHolderRenderer;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
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
		if(side != null && TileEntityInfomationHolderRenderer.currentWorld.getBlockState(TileEntityInfomationHolderRenderer.currentPos.offset(side)).getBlock() == SecretBlocks.ONE_WAY_GLASS)
			return new ArrayList<BakedQuad>();
		return TileEntityInfomationHolderRenderer.currentRender != null && TileEntityInfomationHolderRenderer.currentRender.getBlock() == SecretBlocks.ONE_WAY_GLASS && 
				side != TileEntityInfomationHolderRenderer.currentRender.getValue(BlockDirectional.FACING)? getModel(Blocks.GLASS.getDefaultState()).getQuads(state, side, rand) : super.getQuads(state, side, rand);
	}
}
