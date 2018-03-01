package com.wynprice.secretroomsmod.render.fakemodels;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

/**
 * A True Sight model, whereas a certain face of the model will be different to the rest. Used for directional True Sight Models
 * @author Wyn Price
 *
 */
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
		return face == currentRender.getValue(BlockDirectional.FACING) ? super.getRenderInfo(face, teMirrorState) : 
			new RenderInfo(currentRender, sides);
	}

}
