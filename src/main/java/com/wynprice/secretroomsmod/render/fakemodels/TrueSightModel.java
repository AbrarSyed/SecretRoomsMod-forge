package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.BaseTextureSwitchFakeModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TrueSightModel extends BaseTextureSwitchFakeModel
{

	public TrueSightModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState) 
	{
		return new RenderInfo(currentRender, getModel(new ResourceLocation(SecretRooms5.MODID, "block/" + currentRender.getBlock().getRegistryName().getResourcePath())));
	}
}
