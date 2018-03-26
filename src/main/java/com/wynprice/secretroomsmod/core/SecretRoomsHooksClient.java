package com.wynprice.secretroomsmod.core;

import java.util.List;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretCompatibility;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.render.FakeBlockAccess;
import com.wynprice.secretroomsmod.render.FakeChunkCache;
import com.wynprice.secretroomsmod.render.fakemodels.SecretBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SecretRoomsHooksClient {
	
	/**
	 * Transforms {@link net.minecraft.client.renderer.chunk.RenderChunk}
	 * <br>Causes the iblockstate used to get the model to be recieved through here
	 * This means that I am able to set up one state in the {@link FakeChunkCache}, and leave it, as the model is set elsewhere.
	 * @param world The world 
	 * @param pos The position 
	 * @return The actual block state, if the tileEntity at that position is an {@link ISecretTileEntity}
	 */
	public static IBlockState getBlockState(IBlockAccess world, BlockPos pos) {
		if(world.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState state = world.getTileEntity(pos).getWorld().getBlockState(pos);
			IBlockState renderState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorStateSafely();
			try
			{
				renderState = renderState.getActualState(new FakeBlockAccess(world.getTileEntity(pos).getWorld()), pos);
				state = state.getActualState(world, pos);
			}
			catch (Exception e) 
			{
				;
			}
			state = ((IExtendedBlockState)state).withProperty(ISecretBlock.RENDER_PROPERTY, renderState);
			SecretBlockModel.instance().AO.set(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState).isAmbientOcclusion());
			SecretBlockModel.instance().SRMBLOCK.set(state);
			return state;
		}
		return world.getBlockState(pos);
	}
	
	/**
	 * Transforms {@link BlockStateContainer.StateImplementation}.
	 * This means that the state can be set as something, yet if that block in the actual world is a SecretRoomsMod block, then the correct boolean will be used
	 * @param block The Block used. Not needed however its already loaded, and unloading it would need more asm
	 * @param state The state that that this is being called from
	 * @param world The world 
	 * @param pos The position
	 * @param face The current face
	 * @return {@link Block#doesSideBlockRendering(IBlockState, IBlockAccess, BlockPos, EnumFacing)}, run on the normal block, or the SRM state if the tileEntity at the position is an {@link ISecretTileEntity}
	 */
	public static boolean doesSideBlockRendering(Block block, IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		if(world.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState otherState = world.getTileEntity(pos).getWorld().getBlockState(pos);
			return otherState.getBlock().doesSideBlockRendering(otherState, world, pos, face);
//			if(!isMalisisDoor(state) && ((ISecretBlock)world.getTileEntity(pos).getWorld().getBlockState(pos).getBlock()).getModelClass() != FakeBlockModel.class) {
//				return false;
//			} 
		}
		return block.doesSideBlockRendering(state, world, pos, face);
	}
	
	/**
	 * Causes {@link IBlockProperties#shouldSideBeRendered(IBlockAccess, BlockPos, EnumFacing)} to be run through here. Means that culling will be correct, as the {@code access} wont have the correct {@link IBlockState} at the given position
	 * @param block The Block used. Not needed however its already loaded, and unloading it would need more asm
	 * @param state The state that that this is being called from
	 * @param access the world
	 * @param pos the position
	 * @param face the blockface
	 * @return {@link Block#shouldSideBeRendered(IBlockState, IBlockAccess, BlockPos, EnumFacing)}, run on the normal block, or the SRM state if the tileEntity at the position is an instance of {@link ISecretTileEntity}
	 */
	public static boolean shouldSideBeRendered(Block block, IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing face) {
		if(!isMalisisDoor(state) && access.getTileEntity(pos) instanceof ISecretTileEntity) {
			World world = access.getTileEntity(pos).getWorld(); //Need to use world as the IBlockAccess wont return the right states
			IBlockState blockState = world.getTileEntity(pos).getWorld().getBlockState(pos);
			return blockState.getBlock().shouldSideBeRendered(blockState, world, pos, face);
		}
		return block.shouldSideBeRendered(state, access, pos, face);
	}
	
	/**
	 * Called from {@link BlockModelRenderer#renderModel(IBlockAccess, IBakedModel, IBlockState, BlockPos, net.minecraft.client.renderer.BufferBuilder, boolean, long)}, and sets the IBakedModel
	 * @param access The world
	 * @param pos The position
	 * @param model The current model
	 * @return {@code model}, or {@link SecretBlockModel#instance()} if the tileEntity at the position is an instance of {@link ISecretTileEntity}
	 */
	public static IBakedModel getActualModel(IBlockAccess access, BlockPos pos, IBakedModel model) {
		if(access.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState state = access.getTileEntity(pos).getWorld().getBlockState(pos);
			if(isMalisisDoor(state)) {
				return model;
			}
			return SecretBlockModel.instance();
		}
		return model;
	}
	
	/**
	 * Called from {@link BlockRendererDispatcher#renderBlock(IBlockState, BlockPos, IBlockAccess, net.minecraft.client.renderer.BufferBuilder)}. 
	 * Causes the blockState used in rendering to be the mirrored state, if the tileEntity at the position is an instance of {@link ISecretTileEntity}
	 * @param access The world
	 * @param pos The position
	 * @param state The blockstate used to be rendering. <i> This block will be an instance of ISecretBlock, if the block at that position in game is so</i>
	 * @return {@code state} or the mirrored state of the SRM block, if so needed. 
	 */
	public static IBlockState getActualState(IBlockAccess access, BlockPos pos, IBlockState state) {
		if(isMalisisDoor(state)) {
			return state;
		}
		
		if(state.getBlock() instanceof ISecretBlock) {
			IBlockState mirroredState = access.getBlockState(pos);
			try {
				mirroredState = mirroredState.getActualState(access, pos);
			} catch (Exception e) {
				;
			}
			return mirroredState;
		}
		return state;
	}
	
	private static boolean isMalisisDoor(IBlockState state) {
		return SecretCompatibility.MALISISDOORS && (state.getBlock() == SecretBlocks.SECRET_WOODEN_DOOR || state.getBlock() == SecretBlocks.SECRET_IRON_DOOR || state.getBlock() == SecretBlocks.SECRET_WOODEN_TRAPDOOR);
	}
}
