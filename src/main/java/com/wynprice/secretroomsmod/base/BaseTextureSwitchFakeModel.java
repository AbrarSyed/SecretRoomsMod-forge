package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.fakemodels.BaseTextureFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.state.IBlockState;

public abstract class BaseTextureSwitchFakeModel extends BaseTextureFakeModel 
{

	public BaseTextureSwitchFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	public IBlockState getNormalStateWith(IBlockState s) {
		if(((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this) instanceof BaseTextureFakeModel &&
				!(((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this) instanceof BaseTextureSwitchFakeModel))
			return ((BaseTextureFakeModel)((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this)).getNormalStateWith(s);
		IBlockState baseState = ((ISecretTileEntity) BaseTERender.currentWorld.getTileEntity(BaseTERender.currentPos)).getMirrorState();
		return baseState.getBlock().getActualState(baseState, BaseTERender.currentWorld, BaseTERender.currentPos);
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return null;
	}

}
