package com.wynprice.secretroomsmod.intergration.malisisdoors;

import net.malisis.core.renderer.Parameter;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.icon.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import scala.annotation.meta.param;

public class SecretRenderParameters extends RenderParameters
{
	
	private final TextureAtlasSprite fallbackSprite;
	
	public SecretRenderParameters(RenderParameters rp, TextureAtlasSprite quadSprite) 
	{
		renderAllFaces.set(rp.renderAllFaces.get());
		useBlockBounds.set(rp.useBlockBounds.get());
		renderBounds.set(rp.renderBounds.get());
		useCustomTexture.set(rp.useCustomTexture.get());
		applyTexture.set(rp.applyTexture.get());
		iconProvider.set(rp.iconProvider.get());
		icon.set(rp.icon.get());
		useWorldSensitiveIcon.set(rp.useWorldSensitiveIcon.get());
		useTexture.set(rp.useTexture.get());
		interpolateUV.set(rp.interpolateUV.get());
		rotateIcon.set(rp.rotateIcon.get());
		calculateAOColor.set(rp.calculateAOColor.get());
		calculateBrightness.set(rp.calculateBrightness.get());
		usePerVertexColor.set(rp.usePerVertexColor.get());
		usePerVertexAlpha.set(rp.usePerVertexAlpha.get());
		usePerVertexBrightness.set(rp.usePerVertexBrightness.get());
		useEnvironmentBrightness.set(rp.useEnvironmentBrightness.get());
		useNormals.set(rp.useNormals.get());
		colorMultiplier.set(rp.colorMultiplier.get());
		colorFactor.set(rp.colorFactor.get());
		brightness.set(rp.brightness.get());
		alpha.set(rp.alpha.get());
		direction.set(rp.direction.get());
		textureSide.set(rp.textureSide.get());
		aoMatrix.set(rp.aoMatrix.get());
		flipU.set(rp.flipU.get());
		flipV.set(rp.flipV.get());
		this.quadSprite.set(quadSprite);
		this.fallbackSprite = quadSprite;
		deductParameters.set(rp.deductParameters.get());
	}
	
	/**
	 * The index of the quad currently being rendered
	 */
	public Parameter<TextureAtlasSprite> quadSprite = new Parameter<>(Icon.missing);

	@Override
	public RenderParameters clone() {
		RenderParameters params = super.clone();
		if(params != null) {
			return new SecretRenderParameters(params, quadSprite.get());
		}
		return null;
	}
	
	@Override
	public void merge(RenderParameters params)
	{
		super.merge(params);
	}
	
	private Parameter<?> getParameter(int index)
	{
		if (index < 0 || index >= listParams.size()) {
			return null;
		}
		return listParams.get(index);
	}
	

	@Override
	protected void buildList() {
		super.buildList();
 		if(quadSprite == null) { //Keeps becoming null. no idea why :/
			quadSprite = new Parameter<TextureAtlasSprite>(Icon.missing);
			quadSprite.set(fallbackSprite);
		}
		listParams.add(quadSprite);
	}
	
}
