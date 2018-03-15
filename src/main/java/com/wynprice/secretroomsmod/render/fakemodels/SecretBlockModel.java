package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretCompatibility;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.handler.Location;
import com.wynprice.secretroomsmod.intergration.ctm.SecretCompatCTM;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * The Model used to replace blocks with 
 * @author Wyn Price
 *
 */
public class SecretBlockModel implements IBakedModel
{
	
	private static SecretBlockModel instance;
	
	/**
	 * The ThreadLocal used to control what {@link #isAmbientOcclusion()} should return
	 */
	public final ThreadLocal<Boolean> AO = ThreadLocal.withInitial(() -> false);
	
	/**
	 * The ThreadLocal used to control what SecretRoomsBlock is being rendered at the moment. 
	 */
	public final ThreadLocal<IBlockState> SRMBLOCK = ThreadLocal.withInitial(() -> null);
	
	public final ThreadLocal<Location> LOCATION = ThreadLocal.withInitial(() -> null);

	
	private final IBakedModel model;

	public SecretBlockModel(IBakedModel model) {
		this.model = model;
		instance = this;
	}
	
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) 
	{
		if(SRMBLOCK.get() != null) {
			IBlockState secretBlockState = SRMBLOCK.get();
			if(SecretCompatibility.MALISISDOORS && (secretBlockState.getBlock() == SecretBlocks.SECRET_WOODEN_DOOR || secretBlockState.getBlock() == SecretBlocks.SECRET_IRON_DOOR)) {
				return Lists.newArrayList(); //If malisisdoors is enabled, dont render anything
			}
			IBlockState renderActualState = ((IExtendedBlockState)secretBlockState).getValue(ISecretBlock.RENDER_PROPERTY);
			if(renderActualState != null)
			{
				FakeBlockModel renderModel = ((ISecretBlock)secretBlockState.getBlock()).phaseModel(new FakeBlockModel(renderActualState));
				if(TrueSightHelmet.isHelmet()) {
        			renderModel = ((ISecretBlock)secretBlockState.getBlock()).phaseTrueModel(new TrueSightModel(new FakeBlockModel(renderActualState)));
        		}
				return SecretCompatCTM.getQuads(renderModel.setCurrentRender(secretBlockState).setCurrentActualState(renderActualState).setCurrentLocation(LOCATION.get()), state, side, rand);
			}
		}
		return this.model.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return AO.get();
	}

	@Override
	public boolean isGui3d() {
		return model.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture() 
	{
		return model.getParticleTexture();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return model.getOverrides();
	}
	
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return model.getItemCameraTransforms();
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return model.handlePerspective(cameraTransformType);
	}
	
	public static SecretBlockModel instance() {
		return instance;
	}
	
}
