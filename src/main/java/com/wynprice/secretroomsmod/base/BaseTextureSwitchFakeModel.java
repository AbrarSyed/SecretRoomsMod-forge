package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.fakemodels.BaseTextureFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.state.IBlockState;

/**
 * The basic texture switch model. Implements more things that {@link BaseTextureFakeModel}
 * @author Wyn Price
 *
 */
public abstract class BaseTextureSwitchFakeModel extends BaseTextureFakeModel 
{

	public BaseTextureSwitchFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	public IBlockState getNormalStateWith(IBlockState s, IBlockState mirrorState) {
//		if(((ISecretBlock)currentRender.getBlock()).phaseModel(this) instanceof BaseTextureFakeModel &&
//				!(((ISecretBlock)currentRender.getBlock()).phaseModel(this) instanceof BaseTextureSwitchFakeModel))
//			return ((BaseTextureFakeModel)((ISecretBlock)currentRender.getBlock()).phaseModel(this)).getNormalStateWith(s, mirrorState);
		
		return mirrorState;
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return null;
	}

}
