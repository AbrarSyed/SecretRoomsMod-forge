package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.base.BaseTERender;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class TrueSightFaceDiffrentModel extends TrueSightModel
{
	
	private final IBakedModel sides;
	
	public TrueSightFaceDiffrentModel(FakeBlockModel model, IBakedModel sides) 
	{
		super(model);
		this.sides = sides;
	}
	
	@Override
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState) 
	{
		return face == BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getValue(BlockDirectional.FACING) ? super.getRenderInfo(face, teMirrorState) : 
			new RenderInfo(BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos), sides);
	}

}
