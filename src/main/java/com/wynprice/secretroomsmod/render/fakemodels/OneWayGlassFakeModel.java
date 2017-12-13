package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.base.BaseTERender;
import com.wynprice.secretroomsmod.base.BaseTextureSwitchFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.BaseTextureFakeModel.RenderInfo;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

public class OneWayGlassFakeModel extends BaseTextureSwitchFakeModel
{
	public OneWayGlassFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState) 
	{
		if(face != null && BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos.offset(face)).getBlock() == SecretBlocks.ONE_WAY_GLASS)
			return null;
		if(face == null && BaseTERender.currentRender.getValue(BlockDirectional.FACING) == EnumFacing.UP)
			return super.getRenderInfo(face, teMirrorState);
		return BaseTERender.currentRender != null && BaseTERender.currentRender.getBlock() == SecretBlocks.ONE_WAY_GLASS && 
				face != BaseTERender.currentRender.getValue(BlockDirectional.FACING) ? super.getRenderInfo(face, Blocks.GLASS.getDefaultState()) : super.getRenderInfo(face, teMirrorState);
	}

}
