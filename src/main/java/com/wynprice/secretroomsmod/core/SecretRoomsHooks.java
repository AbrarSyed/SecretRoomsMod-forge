package com.wynprice.secretroomsmod.core;

import java.util.List;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretCompatibility;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.render.FakeBlockAccess;
import com.wynprice.secretroomsmod.render.fakemodels.SecretBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class SecretRoomsHooks {
	
	/**
	 * @see SecretRoomsTransformer#RenderChunk
	 */
	public static IBlockState getBlockState(IBlockAccess world, BlockPos pos) {
		if(world.getTileEntity(pos) instanceof ISecretTileEntity) {
			IBlockState state = world.getTileEntity(pos).getWorld().getBlockState(pos);
			IBlockState renderState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();
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
	 * @see SecretRoomsTransformer#StateImplementation
	 * @param block Is not needed, however having this as the first param makes asm easier
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
	 * @see SecretRoomsTransformer#StateImplementation
	 */
	public static boolean shouldSideBeRendered(Block block, IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing face) {
		if(!isMalisisDoor(state) && access.getTileEntity(pos) instanceof ISecretTileEntity) {
			World world = access.getTileEntity(pos).getWorld(); //Need to use world as the IBlockAccess wont return the right states
			IBlockState blockState = world.getTileEntity(pos).getWorld().getBlockState(pos);
			return blockState.getBlock().shouldSideBeRendered(blockState, world, pos, face);
		}
		return block.shouldSideBeRendered(state, access, pos, face);
	}
	
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
	
	public static void addCollisionBoxToList(Block block, IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
		if(EnergizedPasteHandler.hasReplacedState(worldIn, pos)) {
			IBlockState energizedState = EnergizedPasteHandler.getReplacedState(worldIn, pos);
			try {
				energizedState = energizedState.getActualState(worldIn, pos);
			} catch (Exception e) {
				;
			}
			energizedState.getBlock().addCollisionBoxToList(energizedState, worldIn, pos, entityBox, collidingBoxes, entityIn, true);
		} else {
			block.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
		}
    }
	
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
		return SecretCompatibility.MALISISDOORS && (state.getBlock() == SecretBlocks.SECRET_WOODEN_DOOR || state.getBlock() == SecretBlocks.SECRET_IRON_DOOR);
	}
}
