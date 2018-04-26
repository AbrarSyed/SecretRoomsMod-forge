package com.wynprice.secretroomsmod.integration.malisisdoors.malisisrenders;

import java.util.List;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.integration.malisisdoors.SecretBlockIconProvider;
import com.wynprice.secretroomsmod.integration.malisisdoors.SecretRenderParameters;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.blocks.SecretMalisisTrapDoorBlock;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.tileentities.SecretMalisisTileEntityTrapDoor;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.element.Face;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.malisis.core.renderer.icon.provider.IItemIconProvider;
import net.malisis.core.util.EnumFacingUtils;
import net.malisis.doors.block.Door;
import net.malisis.doors.block.TrapDoor;
import net.malisis.doors.renderer.TrapDoorRenderer;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class SecretTrapDoorRenderer extends TrapDoorRenderer {
	
	public SecretTrapDoorRenderer() 
	{
		registerFor(SecretMalisisTileEntityTrapDoor.class);
		ensureBlock(SecretMalisisTrapDoorBlock.class, TrapDoor.class);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		((RenderParameters)ReflectionHelper.getPrivateValue(TrapDoorRenderer.class, this, "rp")).calculateAOColor.set(true);
	}
	
	@Override
	protected Icon getIcon(Face face, RenderParameters params)
	{
		if (params != null && params.icon.get() != null)
			return params.icon.get();
		
		IIconProvider iconProvider = getIconProvider(params);
		if (iconProvider instanceof IItemIconProvider && itemStack != null)
			return ((IItemIconProvider) iconProvider).getIcon(itemStack);
		//SRM START
		if(iconProvider instanceof SecretBlockIconProvider) {
			((SecretBlockIconProvider)iconProvider).current_params.set(params);
		}
		//SRM STOP
		if (iconProvider instanceof IBlockIconProvider && block != null)
		{
			EnumFacing side = params != null ? params.textureSide.get() : EnumFacing.SOUTH;
			if (params != null && shouldRotateIcon(params))
				side = EnumFacingUtils.getRealSide(blockState, side);

			IBlockIconProvider iblockp = (IBlockIconProvider) iconProvider;
			if (renderType == RenderType.BLOCK || renderType == RenderType.TILE_ENTITY)
				return iblockp.getIcon(world, pos, blockState, side);
			else if (renderType == RenderType.ITEM)
				return iblockp.getIcon(itemStack, side);
		}

		return iconProvider != null ? iconProvider.getIcon() : Icon.missing;	
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
				DoorTileEntity te = Door.getDoor(world, pos);
				
				EnumFacing dir = world.getBlockState(pos).getValue(BlockTrapDoor.FACING);
								
				if(te.isMoving() || world.getBlockState(pos).getValue(BlockTrapDoor.OPEN)) {
					if(facing.getAxis() == Axis.Y || facing.getAxis() == dir.getAxis()) {
						facing = facing.rotateAround(dir.rotateY().getAxis());
						if(facing.getAxis() == Axis.Y) {
							facing = facing.getOpposite();
						}
					}
				}
				
				IBlockState mirrorState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorStateSafely();
				try {
					mirrorState = mirrorState.getActualState(world, pos);
				} catch (Exception e) {
					;
				}
				IBlockState extendedMirrorState = mirrorState.getBlock().getExtendedState(mirrorState, world, pos);
				
				BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
				ForgeHooksClient.setRenderLayer(mirrorState.getBlock().getBlockLayer());
				List<BakedQuad> quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirrorState).getQuads(extendedMirrorState, facing, MathHelper.getPositionRandom(pos));
				if(quadList.isEmpty())
					quadList = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(mirrorState).getQuads(extendedMirrorState, null, MathHelper.getPositionRandom(pos));
				ForgeHooksClient.setRenderLayer(layer);
				Face controllFace = new Face(f, sParams);
				for(BakedQuad quad : quadList) {
					Icon icon = new Icon(quad.getSprite());
					int uvIndex = quad.getFormat().getUvOffsetById(0) / 4;
					
					icon.setUVs(
							Float.intBitsToFloat(quad.getVertexData()[0 * quad.getFormat().getIntegerSize() + uvIndex]), 
							Float.intBitsToFloat(quad.getVertexData()[0 * quad.getFormat().getIntegerSize() + uvIndex + 1]), 
							Float.intBitsToFloat(quad.getVertexData()[2 * quad.getFormat().getIntegerSize() + uvIndex]), 
							Float.intBitsToFloat(quad.getVertexData()[2 * quad.getFormat().getIntegerSize() + uvIndex + 1])
					);
					
					sParams.quadSprite.set(icon);
					if(TrueSightHelmet.isHelmet()) {
						sParams.quadSprite.set(FakeBlockModel.getModel(new ResourceLocation(SecretRooms5.MODID, "block/secret_" + (block == SecretBlocks.SECRET_WOODEN_TRAPDOOR ? "wooden" : "iron") + "_trapdoor")).getParticleTexture());
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
}
