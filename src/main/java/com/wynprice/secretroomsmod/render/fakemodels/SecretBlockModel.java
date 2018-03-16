package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretCompatibility;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.intergration.ctm.SecretCompatCTM;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * The Model used to replace blocks with 
 * @author Wyn Price
 *
 */
public class SecretBlockModel extends FakeBlockModel
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
	
	
	public SecretBlockModel(IBakedModel stone) {
		super(stone);
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
				return SecretCompatCTM.getQuads(renderModel.setCurrentRender(secretBlockState).setCurrentActualState(renderActualState), state, side, rand);
			}
		}
		return this.model.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return AO.get();
	}
	
	public static SecretBlockModel instance() {
		return instance;
	}
	
	public static SecretBlockModel setInstance(IBakedModel stoneModel) {
		instance = new SecretBlockModel(stoneModel);
		return instance;
	}
}
