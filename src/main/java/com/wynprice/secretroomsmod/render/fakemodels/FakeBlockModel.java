package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class FakeBlockModel implements IBakedModel
{
	protected final IBakedModel model;
	
	public FakeBlockModel(IBlockState overstate) 
	{
		this(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(overstate));
	}
	
	public FakeBlockModel(IBakedModel model) 
	{
		this.model = model;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		return model.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return model.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return model.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return model.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return model.getOverrides();
	}
	
	public static IBakedModel getModel(IBlockState state)
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
	}
		
	public static IBakedModel getModel(ResourceLocation resourceLocation) 
	{
		IBakedModel bakedModel;
		IModel model;
		try {
	        model = ModelLoaderRegistry.getModel(resourceLocation);
		} catch (Exception e) {
          throw new RuntimeException(e);
		}
		bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
				location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
	    return bakedModel;
	}
	
}
