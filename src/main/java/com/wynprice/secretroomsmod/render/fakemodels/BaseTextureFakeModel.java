package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.integration.ctm.SecretCompatCTM;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;

/**
 * The class used to take textures from one model, and put it on another model
 * @author Wyn Price
 *
 */
public abstract class BaseTextureFakeModel extends FakeBlockModel 
{
	public BaseTextureFakeModel(FakeBlockModel model) {
		super(model);
	}
	
	/**
	 * Used to convert the {@link IBlockState} {@code actualWorldState} to have the same properties as {@code mirrorState}
	 * <br>Used to get the block that will be used to get the model
	 * @param actualWorldState the SRM block thats actually in the world
	 * @param mirrorState the mirror state
	 * @return the mirror state, but with the same properties as the actualWorldState
	 */
	public abstract IBlockState getNormalStateWith(IBlockState actualWorldState, IBlockState mirrorState);
	
	/**
	 * Used to get the base state, to make sure that a state with a different blockstate properties wont be used.
	 * @return The base class that will be rendered
	 */
	protected abstract Class<? extends ISecretBlock> getBaseBlockClass();
	
	/**
	 * Used to get the order of EnumFacing values that will be resorted Tto if {@link #getQuads(IBlockState, EnumFacing, long)} returns a null or empty list on default.
	 * @return a list of EnumFacings, one of which may be null
	 * @deprecated Was used to fix a bug, which was later fixed anyway. Keeping here because I can't be bothered to remove it. It works how it is
	 */
	@Deprecated
	protected EnumFacing[] fallbackOrder()
	{
		EnumFacing[] list = new EnumFacing[EnumFacing.VALUES.length + 1];
		for(int i = 0; i < EnumFacing.VALUES.length; i++)
			list[i] = EnumFacing.VALUES[i];
		list[EnumFacing.VALUES.length] = null;
		return list;
	}
	
	/**
	 * Used to get the visuals that will be put onto a different model
	 * @param face the direction thats being rendered. Can be null
	 * @param teMirrorState the state being mirrored by the SRM block
	 * @param teMirrorStateExtended the extended state being rendered by the SRM block
	 * @return The RenderInfo containing the {@link IBlockState} and the {@link IBakedModel} that going to be rendered
	 */
	protected RenderInfo getRenderInfo(EnumFacing face, IBlockState teMirrorState, IBlockState teMirrorStateExtended)
	{
		return new RenderInfo(teMirrorState, getModel(teMirrorStateExtended));
	}
	
	/**
	 * Try not to use. Will get the model from the state.
	 * @param face the direction thats being rendered. Can be null
	 * @param state the state being mirrored by the SRM block
	 * @return The RenderInfo containing the {@link IBlockState} and the {@link IBakedModel} that going to be rendered
	 */
	public final RenderInfo getRenderInfo(EnumFacing face, IBlockState state) {
		return new RenderInfo(state, getModel(state));
	}
			
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
		if(getBaseBlockClass() != null && !(getBaseBlockClass().isAssignableFrom(currentRender.getBlock().getClass()))) {
			return super.getQuads(state, side, rand);
		}
		ArrayList<BakedQuad> finalList = new ArrayList<BakedQuad>();
		RenderInfo renderInfo = getRenderInfo(side, state, currentActualState);
		IBlockState normalState = getNormalStateWith(currentRender, currentActualState);
		if(renderInfo != null) {
			for(BakedQuad quad : getModel(normalState).getQuads(currentActualState, side, rand))
			{
				if(this instanceof OneWayGlassFakeModel) {
					finalList.add(quad);
					continue;
				}
				List<BakedQuad> secList = SecretCompatCTM.getQuads(renderInfo.renderModel, renderInfo.blockstate, side, 0);
				if(secList.isEmpty()) {
					secList = SecretCompatCTM.getQuads(renderInfo.renderModel, renderInfo.blockstate, quad.getFace(), 0);
				}
				if(secList == null || secList.isEmpty()) {
					for(EnumFacing facing : fallbackOrder()) {
						List<BakedQuad> secList2 = SecretCompatCTM.getQuadsNull(renderInfo.renderModel, renderInfo.blockstate, facing, 0);
						if(!secList2.isEmpty()) {
							secList = secList2;
						}
					}
				}
				int[] aint1 = quad.getVertexData();
				TextureAtlasSprite tas = quad.getSprite();
				
				float baseMaxU = Math.max(tas.getUnInterpolatedU(Float.intBitsToFloat(aint1[0 * (aint1.length / 4) + 4])), tas.getUnInterpolatedU(Float.intBitsToFloat(aint1[2 * (aint1.length / 4) + 4])));
				float baseMaxV = Math.max(tas.getUnInterpolatedV(Float.intBitsToFloat(aint1[0 * (aint1.length / 4) + 5])), tas.getUnInterpolatedV(Float.intBitsToFloat(aint1[2 * (aint1.length / 4) + 5])));
						
				for(BakedQuad mirrorQuad : secList) {
					int[] aint = Arrays.copyOf(aint1, aint1.length);
					int[] newAint = Arrays.copyOf(mirrorQuad.getVertexData(), mirrorQuad.getVertexData().length);
					float minX = Float.MAX_VALUE;
					float minY = Float.MAX_VALUE;
					float minZ = Float.MAX_VALUE;
					float maxX = Float.MIN_VALUE;
					float maxY = Float.MIN_VALUE;
					float maxZ = Float.MIN_VALUE;
					for(int i = 0; i < 4; i++) {
						int pos = (aint.length / 4) * i;
						float x = Float.intBitsToFloat(aint[pos + 0]);
						float y = Float.intBitsToFloat(aint[pos + 1]);
						float z = Float.intBitsToFloat(aint[pos + 2]);
						minX = Math.min(minX, x);
						minY = Math.min(minY, y);
						minZ = Math.min(minZ, z);						
						maxX = Math.max(maxX, x);
						maxY = Math.max(maxY, y);
						maxZ = Math.max(maxZ, z);
					}
					float rangeX = maxX - minX;
					float rangeY = maxY - minY;
					float rangeZ = maxZ - minZ;	
					
					int uvIndex = mirrorQuad.getFormat().getUvOffsetById(0) / 4;
					TextureAtlasSprite sprite = mirrorQuad.getSprite();
	
					float texMinU = sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex]));
					float texMinV = sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1]));
					float texRangeU = Math.abs(sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex])) - texMinU);
					float texRangeV = Math.abs(sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1])) - texMinV);
													
					float newMaxU = (baseMaxU / 16f) * texRangeU + texMinU;
					float newMaxV = (baseMaxV / 16f) * texRangeV + texMinV;
					
					BlockFaceUV faceUV = new BlockFaceUV(new float[]{ texMinU, texMinV, newMaxU, newMaxV }, 0);
					
					boolean postQuad = false;
					for(int i = 0; i < 4; i++) {
						int pos = (aint.length / 4) * i;	
						float textureX = Float.intBitsToFloat(newAint[pos + 0]);
						float textureY = Float.intBitsToFloat(newAint[pos + 1]);
						float textureZ = Float.intBitsToFloat(newAint[pos + 2]);
						
						boolean inRangeX = isWithinRange(textureX, minX, maxX);
						boolean inRangeY = isWithinRange(textureY, minY, maxY);
						boolean inRangeZ = isWithinRange(textureZ, minZ, maxZ);
						if((inRangeX && inRangeY && inRangeZ) || side == null) {
							postQuad = true;
						}
						newAint[pos + 0] = Float.floatToRawIntBits(Float.intBitsToFloat(newAint[pos + 0]) * rangeX + minX);
						newAint[pos + 1] = Float.floatToRawIntBits(Float.intBitsToFloat(newAint[pos + 1]) * rangeY + minY);
						newAint[pos + 2] = Float.floatToRawIntBits(Float.intBitsToFloat(newAint[pos + 2]) * rangeZ + minZ);
	
						newAint[pos + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(faceUV.getVertexU(i)));
						newAint[pos + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(faceUV.getVertexV(i)));

						if(stretchSide(side, Axis.X, rangeX)) {
							newAint[pos + 0] = aint[pos + 0];
						}
						if(stretchSide(side, Axis.Y, rangeY)) {
							newAint[pos + 1] = aint[pos + 1];
						}
						if(stretchSide(side, Axis.Z, rangeZ)) {
							newAint[pos + 2] = aint[pos + 2];
						}
					}
					if(!postQuad) {
						continue;
					}
					BakedQuad newQuad = new BakedQuad(newAint, mirrorQuad.getTintIndex(), mirrorQuad.getFace(), mirrorQuad.getSprite(), mirrorQuad.shouldApplyDiffuseLighting(), mirrorQuad.getFormat());
					finalList.add(newQuad);
				}
				break;
			}
		}
		return finalList;
	}
	
	public boolean stretchSide(EnumFacing facing, Axis axis, float axisRange) {
		return axisRange != 1f;
	}
	
	private boolean isWithinRange(float num, float min, float max) {
		return MathHelper.clamp(Math.round(num * 10f) / 10f, Math.round(min * 10f) / 10f, Math.round(max * 10f) / 10f) == Math.round(num * 10f) / 10f;
	}
	
	/**
	 * Used as a container for rendering info
	 * @author Wyn Price
	 *
	 */
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
