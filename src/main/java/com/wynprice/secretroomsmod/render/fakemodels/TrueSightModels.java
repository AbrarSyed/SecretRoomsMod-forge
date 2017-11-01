package com.wynprice.secretroomsmod.render.fakemodels;

import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.SecretUtils;
import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.TileEntityInfomationHolderRenderer;

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
		return new RenderInfo(TileEntityInfomationHolderRenderer.currentWorld.getBlockState(TileEntityInfomationHolderRenderer.currentPos), 
				SecretUtils.getModel(new ResourceLocation(SecretRooms2.MODID, "block/" + 
		TileEntityInfomationHolderRenderer.currentWorld.getBlockState(TileEntityInfomationHolderRenderer.currentPos).getBlock().getRegistryName().getResourcePath())));
	}
	@Override
	protected IBlockState getNormalStateWith(IBlockState s) 
	{
		IBlockState baseState = ((TileEntityInfomationHolder) TileEntityInfomationHolderRenderer.currentWorld.getTileEntity(TileEntityInfomationHolderRenderer.currentPos)).getMirrorState();
		return baseState.getBlock().getActualState(baseState, TileEntityInfomationHolderRenderer.currentWorld, TileEntityInfomationHolderRenderer.currentPos);
	}

	@Override
	protected Class<? extends ISecretBlock> getBaseBlockClass() {
		return null;
	}

}
