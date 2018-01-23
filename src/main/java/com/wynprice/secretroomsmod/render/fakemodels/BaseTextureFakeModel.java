package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.base.BaseTERender;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.core.UVTransformer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
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
				int t = 0;
				for(BakedQuad mirrorQuad : secList)
				{
					int[] vList = new int[mirrorQuad.getVertexData().length];
					System.arraycopy(mirrorQuad.getVertexData(), 0, vList, 0, vList.length);
					int[] sList = quad.getVertexData();

					if(sList != null)
				      for (int i = 0; i < 4; i++)
				      {
				        int pos = i * quad.getVertexData().length / 4;
				        vList[pos ] = sList[pos];
				        vList[pos + 1] = sList[pos + 1];
				        vList[pos + 2] = sList[pos + 2];
				        try 
				        {
							BlockFaceUV faceUV = UVTransformer.getUV(sList, pos);
							if(faceUV != null)
							{
								vList[pos + 4] = Float.floatToRawIntBits(mirrorQuad.getSprite().getInterpolatedU((double)faceUV.getVertexU(i) * .999 + faceUV.getVertexU((i + 2) % 4) * .001));
						        vList[pos + 4 + 1] = Float.floatToRawIntBits(mirrorQuad.getSprite().getInterpolatedV((double)faceUV.getVertexV(i) * .999 + faceUV.getVertexV((i + 2) % 4) * .001));
							}
				        } catch (Throwable e) {
							e.printStackTrace();
						}
				      }
					
					finalList.add(new BakedQuad(vList, mirrorQuad.getTintIndex(), mirrorQuad.getFace(), 
							mirrorQuad.getSprite(), mirrorQuad.shouldApplyDiffuseLighting(), mirrorQuad.getFormat()));
					++t;
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
