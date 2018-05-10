package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

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
				EnumFacing face = quad.getFace();
				List<BakedQuad> secList = renderInfo.renderModel.getQuads(renderInfo.blockstate, side, 0);
				if(secList.isEmpty()) {
					secList = renderInfo.renderModel.getQuads(renderInfo.blockstate, quad.getFace(), 0);
				}
				if(secList == null || secList.isEmpty()) {
					for(EnumFacing facing : fallbackOrder()) {
						List<BakedQuad> secList2 = renderInfo.renderModel.getQuads(renderInfo.blockstate, facing, 0);
						if(!secList2.isEmpty()) {
							secList = secList2;
						}
					}
				}
				int[] aint1 = quad.getVertexData();
				TextureAtlasSprite tas = quad.getSprite();
				
				float baseMaxU = Math.max(tas.getUnInterpolatedU(Float.intBitsToFloat(aint1[0 * (aint1.length / 4) + 4])), tas.getUnInterpolatedU(Float.intBitsToFloat(aint1[2 * (aint1.length / 4) + 4])));
				float baseMaxV = Math.max(tas.getUnInterpolatedV(Float.intBitsToFloat(aint1[0 * (aint1.length / 4) + 5])), tas.getUnInterpolatedV(Float.intBitsToFloat(aint1[2 * (aint1.length / 4) + 5])));
				
				int[] aint = Arrays.copyOf(aint1, aint1.length); //TODO: turn into a method and seperate class 
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
				
				if(face.getAxis() == Axis.X) {
					baseMaxU = rangeZ * 16f;
					baseMaxV = rangeY * 16f;
				} else if(face.getAxis() == Axis.Y) {
					baseMaxU = rangeX * 16f;
					baseMaxV = rangeZ * 16f;
				} else {
					baseMaxU = rangeX * 16f;
					baseMaxV = rangeY * 16f;
				}
				
				for(BakedQuad mirrorQuad : secList) {
					int[] newAint = Arrays.copyOf(mirrorQuad.getVertexData(), mirrorQuad.getVertexData().length);

					int[] aint_m = Arrays.copyOf(newAint, newAint.length);
					float minX_m = Float.MAX_VALUE;
					float minY_m = Float.MAX_VALUE;
					float minZ_m = Float.MAX_VALUE;
					float maxX_m = Float.MIN_VALUE;
					float maxY_m = Float.MIN_VALUE;
					float maxZ_m = Float.MIN_VALUE;
					for(int i = 0; i < 4; i++) {
						int pos = (aint_m.length / 4) * i;
						float x = Float.intBitsToFloat(aint_m[pos + 0]);
						float y = Float.intBitsToFloat(aint_m[pos + 1]);
						float z = Float.intBitsToFloat(aint_m[pos + 2]);
						minX_m = Math.min(minX_m, x);
						minY_m = Math.min(minY_m, y);
						minZ_m = Math.min(minZ_m, z);						
						maxX_m = Math.max(maxX_m, x);
						maxY_m = Math.max(maxY_m, y);
						maxZ_m = Math.max(maxZ_m, z);
					}
					float rangeX_m = maxX_m - minX_m;
					float rangeY_m = maxY_m - minY_m;
					float rangeZ_m = maxZ_m - minZ_m;	
					
					int uvIndex = mirrorQuad.getFormat().getUvOffsetById(0) / 4;
					TextureAtlasSprite sprite = mirrorQuad.getSprite();
	
					float texMinU = sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex]));
					float texMinV = sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1]));
					float texRangeU = Math.abs(sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex])) - texMinU);
					float texRangeV = Math.abs(sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1])) - texMinV);
					
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
						
						float rangeBaseU = 16;
						float rangeBaseV = 16;
						
						float offsetBaseU;
						float offsetBaseV;

						if(face.getAxis() == Axis.X) {
							rangeBaseU /= getStretchValue(rangeZ, rangeZ_m);
							rangeBaseV /= getStretchValue(rangeY, rangeY_m);
							
							offsetBaseU = 1 - maxZ - rangeZ_m;
							offsetBaseV = 1 - maxY - rangeY_m;
						} else if(face.getAxis() == Axis.Y) {
							rangeBaseU /= getStretchValue(rangeX, rangeX_m);
							rangeBaseV /= getStretchValue(rangeZ, rangeZ_m);
							
							offsetBaseU = 1 - maxX - rangeX_m;
							offsetBaseV = 1 - maxZ - rangeZ_m;
							
						} else {
							rangeBaseU /= getStretchValue(rangeX, rangeX_m);
							rangeBaseV /= getStretchValue(rangeY, rangeY_m);
														
							offsetBaseU = 1 - rangeX - rangeX_m;
							offsetBaseV = 1 - rangeY - rangeY_m;

						}
						float newTexMinU = texMinU + Math.max(offsetBaseU * 8f, 0);
						float newTexMinV = texMinV + Math.max(offsetBaseV * 8f, 0);
												
						float newMaxU = (rangeBaseU / 16f) * texRangeU + newTexMinU;
						float newMaxV = (rangeBaseV / 16f) * texRangeV + newTexMinV;
						
						BlockFaceUV faceUV = new BlockFaceUV(new float[]{ newTexMinU, newTexMinV, newMaxU, newMaxV }, 0);
						
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
						if(stretchSide(side, Axis.Z, rangeZ)){
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
	
	private float getStretchValue(float range, float range_m) {
		return range_m <= range ? 1F : (1f / (range / range_m));
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
