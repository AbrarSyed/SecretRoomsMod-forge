package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.base.BaseTERender;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public abstract class BaseTextureFakeModel extends FakeBlockModel 
{
	public BaseTextureFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	public abstract IBlockState getNormalStateWith(IBlockState s);
	
	protected abstract Class<? extends ISecretBlock> getBaseBlockClass();
	
	protected EnumFacing[] fallbackOrder()
	{
		EnumFacing[] list = new EnumFacing[EnumFacing.VALUES.length + 1];
		for(int i = 0; i < EnumFacing.VALUES.length; i++)
			list[i] = EnumFacing.VALUES[i];
		list[EnumFacing.VALUES.length] = null;
		return list;
	}
	
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState)
	{
		return new RenderInfo(teMirrorState, getModel(teMirrorState));
	}
		
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(getBaseBlockClass() != null && !(getBaseBlockClass().isAssignableFrom(BaseTERender.currentRender.getBlock().getClass())))
			return super.getQuads(state, side, rand);
		ArrayList<BakedQuad> finalList = new ArrayList<BakedQuad>();
		RenderInfo renderInfo = getRenderInfo(side, ((ISecretTileEntity)BaseTERender.currentWorld.getTileEntity(BaseTERender.currentPos)).getMirrorState());
		IBlockState normalState = getNormalStateWith(BaseTERender.currentRender);
		if(renderInfo != null)
			for(BakedQuad quad : getModel(normalState).getQuads(normalState, side, rand))
			{
				List<BakedQuad> secList = new ArrayList<>(renderInfo.renderModel.getQuads(renderInfo.blockstate, side, rand));
				if(secList == null || secList.isEmpty())
					for(EnumFacing facing : fallbackOrder())
						if(!renderInfo.renderModel.getQuads(renderInfo.blockstate, facing, rand).isEmpty())
							secList = renderInfo.renderModel.getQuads(renderInfo.blockstate, facing, rand);
				for(BakedQuad mirrorQuad : secList)
				{
					int[] vList = new int[quad.getVertexData().length];
					System.arraycopy(quad.getVertexData(), 0, vList, 0, vList.length);
					int[] sList = mirrorQuad.getVertexData();
					int[] cList = {4, 5, 11, 12, 18, 19, 25, 26};
					if(sList != null)
						for(int i : cList)
							vList[i] = sList[i];
					finalList.add(new BakedQuad(vList, mirrorQuad.getTintIndex(), quad.getFace(), 
								Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state), mirrorQuad.shouldApplyDiffuseLighting(), mirrorQuad.getFormat()));
				}
			}
		return finalList;
	}
	
	public static class RenderInfo
	{
		public final IBlockState blockstate;
		public final IBakedModel renderModel;
		
		public RenderInfo(IBlockState blockstate, IBakedModel renderModel) 
		{
			this.blockstate = blockstate;
			this.renderModel = renderModel;
		}
	}
}
