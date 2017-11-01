package com.wynprice.secretroomsmod.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.lwjgl.opengl.GL11;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.SecretUtils;
import com.wynprice.secretroomsmod.base.BaseExposingHelmet;
import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityInfomationHolderRenderer extends TileEntitySpecialRenderer<TileEntityInfomationHolder>
{
	
	public static IBlockState currentRender;
	public static World currentWorld;
	public static BlockPos currentPos;
	
	@Override
	public void render(TileEntityInfomationHolder te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		GlStateManager.pushMatrix();
        {
        	GlStateManager.enableBlend();
        	boolean isHelmet = false;
        	for(ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList())
        		if(stack.getItem() instanceof BaseExposingHelmet)
        			isHelmet = true;
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        	EntityPlayer entityplayer = Minecraft.getMinecraft().player;
            double d0 = (entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks);
            double d1 = (entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks);
            double d2 = (entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks);
            Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);
	        RenderHelper.disableStandardItemLighting();
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
	        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	        World world = getWorld();
	        Tessellator tessellator = Tessellator.getInstance();
	        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
	        Block block = te.getWorld().getBlockState(te.getPos()).getBlock();
	        ArrayList<Integer> tintList = new ArrayList<>();
	        currentRender = te.getWorld().getBlockState(te.getPos());
	        currentPos = te.getPos();
	        currentWorld = te.getWorld();
	        if(block instanceof ISecretBlock && te.getMirrorState() != null && SecretUtils.getModel((ISecretBlock)block, te.getMirrorState()) != null)
	        {
	        	if(!isHelmet)
	        	{
	        		currentRender = ((ISecretBlock)block).overrideThisState(world, currentPos, currentRender);
		        	IBlockState renderState = te.getMirrorState().getBlock().getActualState(te.getMirrorState(), te.getWorld(), te.getPos());
		        	try
		        	{
		        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, SecretUtils.getModel((ISecretBlock)block, renderState),
			        			world.getBlockState(te.getPos()), te.getPos(), Tessellator.getInstance().getBuffer(), false);
		        	}
		        	catch (Throwable e) 
		        	{
		        		try
		        		{
			        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, SecretUtils.getModel((ISecretBlock)block, renderState),
			        				renderState, te.getPos(), Tessellator.getInstance().getBuffer(), false);
		        		}
		        		catch (Throwable t) {
		        			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, SecretUtils.getModel((ISecretBlock)block, Blocks.STONE.getDefaultState()),
			        				Blocks.STONE.getDefaultState(), te.getPos(), Tessellator.getInstance().getBuffer(), false);
						}
					}
		        	
		        	for(BakedQuad quad : SecretUtils.getModel((ISecretBlock)block, te.getMirrorState()).getQuads(te.getMirrorState(), null, 0L))
	        			tintList.add(quad.hasTintIndex() ? quad.getTintIndex() : -1);
		        	for(EnumFacing face : EnumFacing.values())
		        		for(BakedQuad quad : SecretUtils.getModel((ISecretBlock)block, te.getMirrorState()).getQuads(te.getMirrorState(), face, 0L))
		        			tintList.add(quad.hasTintIndex() ? quad.getTintIndex() : -1);
	        	}
	        	else
	        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
	        		.renderModel(world, SecretUtils.getModel(new ResourceLocation(SecretRooms2.MODID, "block/" + block.getRegistryName().getResourcePath())),
	        				te.getWorld().getBlockState(te.getPos()), te.getPos(), Tessellator.getInstance().getBuffer(), false);
	        }
	        Collections.reverse(tintList);
	        boolean isColorBlock = false;
	        if(te.getMirrorState() != null)
	        	for(String s : SecretConfig.ALLOWED_BLOCKCOLORS) 
	        		if(s.equals(te.getMirrorState().getBlock().getRegistryName().toString())) 
	        			isColorBlock = true;
	        if(!isHelmet)
	        for(int i = 0; i < tessellator.getBuffer().getVertexCount(); i++)
 	        {
 	        	if(!isColorBlock && (tintList.size() <= Math.floorDiv(i, 4) || tintList.get(Math.floorDiv(i, 4)) < 0))
 	        		continue;
 	        	Color color = new Color(Minecraft.getMinecraft().getBlockColors()
 	        			.colorMultiplier(te.getMirrorState(), te.getWorld(), te.getPos(), isColorBlock ? 0 : tintList.get(Math.floorDiv(i, 4))));
 	        	tessellator.getBuffer().putColorMultiplier(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, i);
 	        }
	        
	        tessellator.draw();
	        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
        	GlStateManager.disableBlend();
	        RenderHelper.enableStandardItemLighting();
        }
    	GlStateManager.popMatrix();
	}
}
