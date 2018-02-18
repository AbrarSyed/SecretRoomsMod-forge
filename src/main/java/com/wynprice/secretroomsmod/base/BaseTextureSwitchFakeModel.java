package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.fakemodels.BaseTextureFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

public abstract class BaseTextureSwitchFakeModel extends BaseTextureFakeModel 
{

	public BaseTextureSwitchFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	public IBlockState getNormalStateWith(IBlockState s, IBlockState mirrorState) {
		if(((ISecretBlock)currentRender.getBlock()).phaseModel(this) instanceof BaseTextureFakeModel &&
				!(((ISecretBlock)currentRender.getBlock()).phaseModel(this) instanceof BaseTextureSwitchFakeModel))
			return ((BaseTextureFakeModel)((ISecretBlock)currentRender.getBlock()).phaseModel(this)).getNormalStateWith(s, mirrorState);
		
		return mirrorState.getBlock().getActualState(mirrorState, Minecraft.getMinecraft().world, currentPos);
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return null;
	}

}
