package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.base.BaseTextureSwitchFakeModel;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
		if(face != null && Minecraft.getMinecraft().world.getBlockState(currentPos.offset(face)).getBlock() == SecretBlocks.ONE_WAY_GLASS)
			return null;
		if(face == null && currentRender.getValue(BlockDirectional.FACING) == EnumFacing.UP)
			return super.getRenderInfo(face, teMirrorState);			
		return currentRender != null && currentRender.getBlock() == SecretBlocks.ONE_WAY_GLASS && 
				face != currentRender.getValue(BlockDirectional.FACING) ? super.getRenderInfo(face, Blocks.GLASS.getDefaultState()) : super.getRenderInfo(face, teMirrorState);
	}

}
