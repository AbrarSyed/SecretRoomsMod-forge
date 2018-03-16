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

/**
 * The base Model for all SRM models. All this does is render the input model
 * @author Wyn Price
 *
 */
public class FakeBlockModel implements IBakedModel
{
	protected final IBakedModel model;
	
	/**
	 * The actual state of the SRM block in the world
	 */
	protected IBlockState currentRender;
	
	protected IBlockState currentActualState;
	
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
	
	/**
	 * Used to set the current state of the SRM block thats actually in the world
	 * @param currentRender
	 * @return this instance
	 */
	public FakeBlockModel setCurrentRender(IBlockState currentRender) {
		this.currentRender = currentRender;
		return this;
	}
	
	/**
	 * Used to set the current actual state of the block being rendered
	 * @param currentActualState
	 * @return this instance
	 */
	public FakeBlockModel setCurrentActualState(IBlockState currentActualState) {
		this.currentActualState = currentActualState;
		return this;
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
	
	/**
	 * Used to get the model from the IBlockState
	 * @param state the state to get the model from
	 * @return the model used to render {@code state}
	 */
	public static IBakedModel getModel(IBlockState state)
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
	}
		
	/**
	 * Gets the model from the {@link ResourceLocation}
	 * @param resourceLocation the location of the model
	 * @return the model, at the {@link ResourceLocation}
	 * @throws RuntimeException if the model can't be loaded
	 */
	public static IBakedModel getModel(ResourceLocation resourceLocation) throws RuntimeException
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
