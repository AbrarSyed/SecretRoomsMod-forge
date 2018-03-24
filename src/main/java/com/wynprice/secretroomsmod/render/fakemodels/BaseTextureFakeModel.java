package com.wynprice.secretroomsmod.render.fakemodels;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.handler.SpriteCreator;
import com.wynprice.secretroomsmod.integration.ctm.SecretCompatCTM;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.EnumFacing;

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
				int uvIndex = quad.getFormat().getUvOffsetById(0) / 4;

				List<BakedQuad> secList = SecretCompatCTM.getQuads(renderInfo.renderModel, renderInfo.blockstate, side, rand);
				if(secList.isEmpty()) {
					secList = SecretCompatCTM.getQuads(renderInfo.renderModel, renderInfo.blockstate, quad.getFace(), rand);
				}
				if(secList == null || secList.isEmpty()) {
					for(EnumFacing facing : fallbackOrder()) {
						List<BakedQuad> secList2 = SecretCompatCTM.getQuadsNull(renderInfo.renderModel, renderInfo.blockstate, facing, rand);
						if(!secList2.isEmpty()) {
							secList = secList2;
						}
					}
				}
				
				for(BakedQuad mirrorQuad : secList) {
					SpriteCreator sprite = SpriteCreator.createSprite(mirrorQuad.getSprite()).setuvs(
							Float.intBitsToFloat(mirrorQuad.getVertexData()[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex]), 
							Float.intBitsToFloat(mirrorQuad.getVertexData()[0 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1]), 
							Float.intBitsToFloat(mirrorQuad.getVertexData()[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex]), 
							Float.intBitsToFloat(mirrorQuad.getVertexData()[2 * mirrorQuad.getFormat().getIntegerSize() + uvIndex + 1])
							);
					finalList.add(new BakedQuad(new BakedQuadRetextured(quad, sprite).getVertexData(), mirrorQuad.getTintIndex(), mirrorQuad.getFace(), sprite, mirrorQuad.shouldApplyDiffuseLighting(), mirrorQuad.getFormat()));				}
			}
		}
		return finalList;
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
