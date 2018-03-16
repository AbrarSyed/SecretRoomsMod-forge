package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.BaseTextureSwitchFakeModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * The basic true sight model. All True Sight models extend this class.
 * @author Wyn Price
 *
 */
public class TrueSightModel extends BaseTextureSwitchFakeModel
{

	public TrueSightModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState, IBlockState teMirrorStateExtended) 
	{
		return new RenderInfo(currentRender, getModel(new ResourceLocation(SecretRooms5.MODID, "block/" + currentRender.getBlock().getRegistryName().getResourcePath())));
	}
}
