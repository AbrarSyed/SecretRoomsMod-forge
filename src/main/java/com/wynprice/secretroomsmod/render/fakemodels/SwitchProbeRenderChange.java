package com.wynprice.secretroomsmod.render.fakemodels;

import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Mouse;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.items.ItemStackHandler;

public class SwitchProbeRenderChange implements IBakedModel 
{
    private IBakedModel base;

    public SwitchProbeRenderChange(IBakedModel base) {
        this.base = base;
    }
    
    private final static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> DEFAULT_ITEM_TRANSFORM = 
    		 ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, getTransform(0, 3, 1, 0, 0, 0, 0.55f))
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, getTransform(0, 3, 1, 0, 0, 0, 0.55f))
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, getTransform(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f))
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, getTransform(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f))
            .put(ItemCameraTransforms.TransformType.GROUND, getTransform(0, 2, 0, 0, 0, 0, 0.5f))
            .put(ItemCameraTransforms.TransformType.HEAD, getTransform(0, 13, 7, 0, 180, 0, 1))
            .build();

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return Pair.of(this, DEFAULT_ITEM_TRANSFORM.get(cameraTransformType) == null ? getTransform(0, 0, 0, 0, 0, 0, 1.0f).getMatrix()
        		: DEFAULT_ITEM_TRANSFORM.get(cameraTransformType).getMatrix());
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return base.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return base.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return base.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return base.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return base.getParticleTexture();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return base.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() 
    {
        return new ItemOverrideList(base.getOverrides().getOverrides()) 
        {
            @Override
            public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) 
            {
            	if(!stack.hasTagCompound() || !GuiScreen.isAltKeyDown())
            		return super.handleItemState(originalModel, stack, world, entity);
            	ItemStackHandler handler = new ItemStackHandler(1);
            	handler.deserializeNBT(stack.getTagCompound().getCompoundTag("hit_itemstack"));
            	Block block = Block.getBlockFromName(stack.getTagCompound().getString("hit_block"));
            	if(handler.getStackInSlot(0).isEmpty())
            	{
            		if(Minecraft.getMinecraft().currentScreen instanceof GuiContainer)
            		{
            			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            			GuiContainer container = ((GuiContainer)Minecraft.getMinecraft().currentScreen);
            			for(Slot slot : container.inventorySlots.inventorySlots)
            				if(testForTextureRender(stack, slot.getStack(), block, slot.xPos, slot.yPos))
            					return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.FIRE.getDefaultState());
            			if(testForTextureRender(stack, Minecraft.getMinecraft().player.inventory.getItemStack(), block, 
            					(Mouse.getX() * new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() / Minecraft.getMinecraft().displayWidth) - container.getGuiLeft() - 8, 
            					(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - Mouse.getY() * new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() /
            							Minecraft.getMinecraft().displayHeight - 1) - container.getGuiTop() - 8))
        					return Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.FIRE.getDefaultState());
            		}
            		return super.handleItemState(originalModel, stack, world, entity);
            	}
            	else
            		return Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(handler.getStackInSlot(0), world, entity);
            }
        };
    }
    
    private boolean testForTextureRender(ItemStack overStack, ItemStack stack, Block block, int xPos, int yPos)
    {
    	if(overStack == stack && block != Blocks.AIR && block != null)
		{
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(xPos, yPos, 
					Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getStateFromMeta(stack.getTagCompound().getInteger("hit_meta"))), 16, 16);
			return true;
		}
    	return false;
    }
    
    private static TRSRTransformation getTransform(float tx, float ty, float tz, float ax, float ay, float az, float s) {
        return new TRSRTransformation(
            new Vector3f(tx / 16, ty / 16, tz / 16),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
            new Vector3f(s, s, s),
            null
        );
    }
}
