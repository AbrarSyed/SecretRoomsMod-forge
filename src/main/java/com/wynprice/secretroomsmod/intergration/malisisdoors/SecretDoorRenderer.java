package com.wynprice.secretroomsmod.intergration.malisisdoors;

import java.util.List;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;

import net.malisis.core.block.IComponent;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.malisis.core.renderer.icon.provider.IItemIconProvider;
import net.malisis.core.util.EnumFacingUtils;
import net.malisis.doors.iconprovider.DoorIconProvider;
import net.malisis.doors.renderer.DoorRenderer;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;

public class SecretDoorRenderer extends DoorRenderer
{
	public SecretDoorRenderer() 
	{
		super(true);
		registerFor(SecretMalisisTileEntityDoor.class);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		ensureBlock(SecretMalisisDoor.class);
		rp.calculateAOColor.set(true);
		rp.deductParameters.set(true);
	}
	
	@Override
	public void drawShape(Shape s, RenderParameters params) {
		if (s == null)
			return;

		s.applyMatrix();

		for (Face f : s.getFaces()) {
			
			if(world.getBlockState(pos).getBlock() instanceof ISecretBlock) {
				SecretRenderParameters sParams = new SecretRenderParameters(params, Icon.missing);
				RenderParameters sParamsFace = new RenderParameters(params); //Used to get the EnumFacing from the face
				Face getFacing = new Face(f, sParamsFace);
				getFacing.deductParameters();
				EnumFacing facing = sParamsFace.textureSide.get();
				facing = facing == null ? EnumFacing.SOUTH : facing; //was in build somewhere, just leave it here
				facing = EnumFacingUtils.getRealSide(blockState, facing);
				IBlockState mirrorState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();
				
				boolean facingX = blockState.getValue(BlockDoor.FACING).getAxis() == Axis.X !=  blockState.getValue(BlockDoor.OPEN);
				
				
				DoorTileEntity te = ((DoorTileEntity)world.getTileEntity(blockState.getValue(BlockDoor.HALF) == EnumDoorHalf.LOWER? pos : pos.down()));
				if(te.getTimer().elapsedTick() > te.getOpeningTime() && te.isMoving()) {
					facingX = !facingX;
				}
				
				if(!facingX) {
					facing = facing.rotateAround(Axis.Y);
				}
				
				List<BakedQuad> quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirrorState).getQuads(mirrorState, facing, MathHelper.getPositionRandom(pos));
				if(quadList.isEmpty())
					quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirrorState).getQuads(mirrorState, null, MathHelper.getPositionRandom(pos));
				Face controllFace = new Face(f, sParams);
				for(BakedQuad quad : quadList) {
					sParams.quadSprite.set(quad.getSprite());
					if(TrueSightHelmet.isHelmet()) {
						sParams.quadSprite.set(FakeBlockModel.getModel(new ResourceLocation(SecretRooms5.MODID, "block/secret_" + (block == SecretBlocks.SECRET_WOODEN_DOOR ? "wooden" : "iron") + "_door")).getParticleTexture());
					}
					if(!quad.hasTintIndex() || Minecraft.getMinecraft().player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet) {
						sParams.colorMultiplier.set(-1);
					} else {
						sParams.colorMultiplier.set(Minecraft.getMinecraft().getBlockColors().colorMultiplier(mirrorState, world, pos, quad.getTintIndex()));
					}
					drawFace(new Face(f, sParams), sParams);
				}
			} else {
				drawFace(f, params);
			}
			
		}
	}
	
	@Override
	protected boolean shouldRenderFace(Face face, RenderParameters params) {
		boolean ret = super.shouldRenderFace(face, params);
		if(ret) {
			if (!topBlock && face.name().equals("Top")) {
				return false;
			}

			if (topBlock && face.name().equals("Bottom")) {
				return false;
			}
		}
		return ret;
	}
	
	@Override
	protected Icon getIcon(Face face, RenderParameters params)
	{
		
		if (params.icon.get() != null)
			return params.icon.get();

		DoorIconProvider iconProvider0 = IComponent.getComponent(DoorIconProvider.class, block);
		if (iconProvider0 == null)  {
			if (params != null && params.icon.get() != null)
				return params.icon.get();

			IIconProvider iconProvider = getIconProvider(params);
			if (iconProvider instanceof IItemIconProvider && itemStack != null)
				return ((IItemIconProvider) iconProvider).getIcon(itemStack);

			if (iconProvider instanceof IBlockIconProvider && block != null)
			{
				EnumFacing side = params != null ? params.textureSide.get() : EnumFacing.SOUTH;
				if (params != null && shouldRotateIcon(params))
					side = EnumFacingUtils.getRealSide(blockState, side);

				IBlockIconProvider iblockp = (IBlockIconProvider) iconProvider;
				
				//SRM START
				if(iblockp instanceof SecretBlockIconProvider) {
					((SecretBlockIconProvider)iblockp).current_params.set(params);
				}
				//SRM STOP
				if (renderType == RenderType.BLOCK || renderType == RenderType.TILE_ENTITY)
					return iblockp.getIcon(world, pos, blockState, side);
				else if (renderType == RenderType.ITEM)
					return iblockp.getIcon(itemStack, side);
			}

			return iconProvider != null ? iconProvider.getIcon() : Icon.missing;
		}

		return iconProvider0.getIcon(topBlock, hingeLeft, params.textureSide.get());
	}
	
}
