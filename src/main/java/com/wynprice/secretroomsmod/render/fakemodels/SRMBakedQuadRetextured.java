package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.Arrays;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

public class SRMBakedQuadRetextured extends BakedQuad {
	
	/**
	 * A more accurate version of {@link BakedQuadRetextured}.
	 * @param modelQuad The models quad
	 * @param textureQuad The quad to get the texture from and put onto the model
	 * @param side The side linked to this quad. <b>This is no the side of the quad. this is the side thats passed in with: </b> {@link IBakedModel#getQuads(net.minecraft.block.state.IBlockState, EnumFacing, long)}
	 */
	public SRMBakedQuadRetextured(BakedQuad modelQuad, BakedQuad textureQuad, @Nullable EnumFacing side) {
		super(Arrays.copyOf(textureQuad.getVertexData(), textureQuad.getVertexData().length), textureQuad.getTintIndex(), textureQuad.getFace(), textureQuad.getSprite(), textureQuad.shouldApplyDiffuseLighting(), textureQuad.getFormat());	
		
		EnumFacing face = FaceBakery.getFacingFromVertexData(this.getVertexData());
		
		int[] aint = Arrays.copyOf(modelQuad.getVertexData(), modelQuad.getVertexData().length);
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
		
		int[] newAint = Arrays.copyOf(textureQuad.getVertexData(), textureQuad.getVertexData().length);

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
		
		int uvIndex = textureQuad.getFormat().getUvOffsetById(0) / 4;
		TextureAtlasSprite sprite = textureQuad.getSprite();

		float texMinU = sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[0 * textureQuad.getFormat().getIntegerSize() + uvIndex]));
		float texMinV = sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[0 * textureQuad.getFormat().getIntegerSize() + uvIndex + 1]));
		float texRangeU = Math.abs(sprite.getUnInterpolatedU(Float.intBitsToFloat(newAint[2 * textureQuad.getFormat().getIntegerSize() + uvIndex])) - texMinU);
		float texRangeV = Math.abs(sprite.getUnInterpolatedV(Float.intBitsToFloat(newAint[2 * textureQuad.getFormat().getIntegerSize() + uvIndex + 1])) - texMinV);
		
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
				
				offsetBaseU = 1 - rangeZ - rangeZ_m;
				offsetBaseV = 1 - rangeY - rangeY_m;
			} else if(face.getAxis() == Axis.Y) {
				rangeBaseU /= getStretchValue(rangeX, rangeX_m);
				rangeBaseV /= getStretchValue(rangeZ, rangeZ_m);
				
				offsetBaseU = 1 - rangeX - rangeX_m;
				offsetBaseV = 1 - rangeZ - rangeZ_m;
				
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
		
		int[] vertexAint = this.getVertexData();
		
		if(!postQuad) {
			for(int i = 0; i < vertexAint.length; i++) {
				vertexAint[i] = 0; //Hacky way to disable the rendering of the quad
			}
		} else {
			for(int i = 0; i < vertexAint.length; i++) {
				vertexAint[i] = newAint[i];
			}
		}
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
}
