package com.wynprice.secretroomsmod.handler;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SpriteCreator extends TextureAtlasSprite {

	protected SpriteCreator(TextureAtlasSprite sprite) {
		super(sprite.getIconName());
		copyFrom(sprite);
		this.minU = sprite.getMinU();
		this.minV = sprite.getMinV();
		this.maxU = sprite.getMaxU();
		this.maxV = sprite.getMaxV();
	}
	
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
	
	public SpriteCreator setuvs(float u, float v, float U, float V) {

		this.minU = u;
		this.minV = v;
		this.maxU = U;
		this.maxV = V;
		return this;
	}
	
	@Override
	public float getMinU() {
		return minU;
	}
	
	@Override
	public float getMinV() {
		return minV;
	}
	
	@Override
	public float getMaxU() {
		return maxU;
	}
	
	@Override
	public float getMaxV() {
		return maxV;
	}
	
	@Override
	public float getInterpolatedU(double u)
    {
        float f = this.maxU - this.minU;
        return this.minU + f * (float)u / 16.0F;
    }

	@Override
    public float getUnInterpolatedU(float u)
    {
        float f = this.maxU - this.minU;
        return (u - this.minU) / f * 16.0F;
    }
    
	@Override
    public float getInterpolatedV(double v)
    {
        float f = this.maxV - this.minV;
        return this.minV + f * (float)v / 16.0F;
    }
	
	@Override
    public float getUnInterpolatedV(float v)
    {
        float f = this.maxV - this.minV;
        return (v - this.minV) / f * 16.0F;
    }
	
	public static SpriteCreator createSprite(TextureAtlasSprite base) {
		if(base instanceof SpriteCreator) {
			return (SpriteCreator) base;
		}
		return new SpriteCreator(base);
	}

}
