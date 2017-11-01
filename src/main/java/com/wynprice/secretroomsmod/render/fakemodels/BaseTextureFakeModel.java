package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.TileEntityInfomationHolderRenderer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

public abstract class BaseTextureFakeModel extends FakeBlockModel 
{
	public BaseTextureFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	protected abstract IBlockState getNormalStateWith(IBlockState s);
	
	protected abstract Class<? extends ISecretBlock> getBaseBlockClass();
	
	protected EnumFacing[] fallbackOrder()
	{
		EnumFacing[] list = new EnumFacing[EnumFacing.VALUES.length + 1];
		for(int i = 0; i < EnumFacing.VALUES.length; i++)
			list[i] = EnumFacing.VALUES[i];
		list[EnumFacing.VALUES.length] = null;
		return list;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(!(getBaseBlockClass().isAssignableFrom(TileEntityInfomationHolderRenderer.currentRender.getBlock().getClass())))
			return super.getQuads(state, side, rand);
		ArrayList<BakedQuad> finalList = new ArrayList<BakedQuad>();
		for(BakedQuad quad : getModel(getNormalStateWith(TileEntityInfomationHolderRenderer.currentRender)).getQuads(state, side, rand))
		{
			IBlockState iblockstate = ((TileEntityInfomationHolder)TileEntityInfomationHolderRenderer.currentWorld.getTileEntity(TileEntityInfomationHolderRenderer.currentPos)).getMirrorState();
			List<BakedQuad> secList = new ArrayList<>(getModel(iblockstate).getQuads(iblockstate, side, rand));
			if(secList == null || secList.isEmpty())
				for(EnumFacing facing : fallbackOrder())
					if(!getModel(iblockstate).getQuads(iblockstate, facing, rand).isEmpty())
						secList = getModel(iblockstate).getQuads(iblockstate, facing, rand);
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
}
