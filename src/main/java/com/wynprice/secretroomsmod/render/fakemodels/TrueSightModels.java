package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.SecretUtils;
import com.wynprice.secretroomsmod.base.BaseTERender;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class TrueSightModels extends BaseTextureFakeModel
{

	public TrueSightModels(FakeBlockModel model) {
		super(model);
	}
	
	@Override
	protected RenderInfo getRenderInfo(IBlockState teMirrorState) 
	{
		return new RenderInfo(BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos), 
				SecretUtils.getModel(new ResourceLocation(SecretRooms2.MODID, "block/" + 
		BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock().getRegistryName().getResourcePath())));
	}
	@Override
	protected IBlockState getNormalStateWith(IBlockState s) 
	{
		if(((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this) instanceof BaseTextureFakeModel &&
				!(((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this) instanceof TrueSightModels))
			return ((BaseTextureFakeModel)((ISecretBlock)BaseTERender.currentWorld.getBlockState(BaseTERender.currentPos).getBlock()).phaseModel(this)).getNormalStateWith(s);
		IBlockState baseState = ((ISecretTileEntity) BaseTERender.currentWorld.getTileEntity(BaseTERender.currentPos)).getMirrorState();
		return baseState.getBlock().getActualState(baseState, BaseTERender.currentWorld, BaseTERender.currentPos);
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return null;
	}

}
