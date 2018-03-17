package com.wynprice.secretroomsmod.integration.malisisdoors;

import java.util.List;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;

public class SecretBlockIconProvider implements IBlockIconProvider
{

	@Override
	public Icon getIcon(IBlockState state, EnumFacing side) {
		if(state instanceof IExtendedBlockState && state.getBlock() instanceof ISecretBlock && ((IExtendedBlockState)state).getValue(ISecretBlock.RENDER_PROPERTY) != null) {
			return Icon.from(((IExtendedBlockState)state).getValue(ISecretBlock.RENDER_PROPERTY));
		}
		return Icon.from(SecretBlocks.GHOST_BLOCK);
		
	}

	public ThreadLocal<RenderParameters> current_params = ThreadLocal.withInitial(() -> null);
	
	private boolean previousFacingX = false;
	
	@Override
	public Icon getIcon(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) 
	{
		if(world.getBlockState(pos).getBlock() instanceof ISecretBlock) {
			IBlockState mirroredState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();

			List<BakedQuad> quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirroredState).getQuads(mirroredState, side, MathHelper.getPositionRandom(pos));
			
			Icon icon = quadList.isEmpty() || side == null ? Icon.from(mirroredState) : new Icon(quadList.get(0).getSprite());						
			if(current_params.get() instanceof SecretRenderParameters) {
				icon = new Icon(((SecretRenderParameters)current_params.get()).quadSprite.get());
			}
			
			//For some reason, the textureAtlasSprite registed to the quads for the door is missingno. Manually put the UV coords in:
			float u;
			float v;
			
			if(side == null) {
				u = icon.getInterpolatedU(16);
				v = icon.getInterpolatedV(16);
			} else {
				boolean facingX = state.getValue(BlockDoor.FACING).getAxis() == Axis.X != state.getValue(BlockDoor.OPEN);
				
				
				DoorTileEntity te = ((DoorTileEntity)world.getTileEntity(state.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER? pos : pos.down()));
				if(te.getTimer().elapsedTick() > te.getOpeningTime() && te.isMoving()) {
					facingX = !facingX;
				}
				
				if(facingX) {
					side = side.rotateAround(Axis.Y);
				}
				
				if(side.getAxis() == Axis.X) {
					u = icon.getInterpolatedU(3);
					v = icon.getInterpolatedV(16);
				} else if(side.getAxis() == Axis.Y) {
					u = icon.getInterpolatedU(16);
					v = icon.getInterpolatedV(3);
					
				} else {
					u = icon.getInterpolatedU(16);
					v = icon.getInterpolatedV(16);
				}
			}
			
			return new Icon("secretroomsmod door", icon.getMinU(), icon.getMinV(), u, v);
		}
		return IBlockIconProvider.super.getIcon(world, pos, state, side);
	}

}
