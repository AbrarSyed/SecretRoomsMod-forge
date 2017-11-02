package com.wynprice.secretroomsmod.base;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseTERender<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
	public static IBlockState currentRender;
	public static World currentWorld;
	public static BlockPos currentPos;
	
	public static final HashMap<Block, HashMap<Integer, IBakedModel>> TRUEMAP = new HashMap<>();
	
	@Override
	public void render(T tileEntity, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) 
	{
		if(!(tileEntity instanceof ISecretTileEntity))
			return;
		ISecretTileEntity te = (ISecretTileEntity)tileEntity;
		GlStateManager.pushMatrix();
        {
        	GlStateManager.enableBlend();
        	boolean isHelmet = false;
        	for(ItemStack stack : Minecraft.getMinecraft().player.getArmorInventoryList())
        		if(stack.getItem() instanceof TrueSightHelmet)
        			isHelmet = true;
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        	EntityPlayer entityplayer = Minecraft.getMinecraft().player;
            double d0 = (entityplayer.lastTickPosX + (entityplayer.posX - entityplayer.lastTickPosX) * (double)partialTicks);
            double d1 = (entityplayer.lastTickPosY + (entityplayer.posY - entityplayer.lastTickPosY) * (double)partialTicks);
            double d2 = (entityplayer.lastTickPosZ + (entityplayer.posZ - entityplayer.lastTickPosZ) * (double)partialTicks);
            Tessellator.getInstance().getBuffer().setTranslation(-d0, -d1, -d2);
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
	        World world = getWorld();
	        Tessellator.getInstance().getBuffer().noColor();
	        Tessellator tessellator = Tessellator.getInstance();
	        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
	        Block block = tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock();
	        ArrayList<Integer> tintList = new ArrayList<>();
	        currentRender = tileEntity.getWorld().getBlockState(tileEntity.getPos());
	        currentPos = tileEntity.getPos();
	        currentWorld = tileEntity.getWorld();
	        if(block instanceof ISecretBlock && te.getMirrorState() != null)
	        {
	        	IBlockState renderState = te.getMirrorState().getBlock().getActualState(te.getMirrorState(), tileEntity.getWorld(), tileEntity.getPos());
	        	if(renderState.isOpaqueCube())
		              GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? 7425 : 7424);

        		currentRender = ((ISecretBlock)block).overrideThisState(world, currentPos, currentRender);
	        	if(!isHelmet)
	        	{
		        	try
		        	{
		        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, ((ISecretBlock)block).phaseModel(new FakeBlockModel(renderState)),
			        			world.getBlockState(tileEntity.getPos()), tileEntity.getPos(), Tessellator.getInstance().getBuffer(), false);
		        	}
		        	catch (Throwable e) 
		        	{
		        		try
		        		{
			        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, ((ISecretBlock)block).phaseModel(new FakeBlockModel(renderState)),
			        				renderState, tileEntity.getPos(), Tessellator.getInstance().getBuffer(), false);
		        		}
		        		catch (Throwable t) {
		        			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, ((ISecretBlock)block).phaseModel(new FakeBlockModel(Blocks.STONE.getDefaultState())),
			        				Blocks.STONE.getDefaultState(), tileEntity.getPos(), Tessellator.getInstance().getBuffer(), false);
						}
					}
		        	
		        	for(BakedQuad quad : ((ISecretBlock)block).phaseModel(new FakeBlockModel(te.getMirrorState())).getQuads(te.getMirrorState(), null, 0L))
	        			tintList.add(quad.hasTintIndex() ? quad.getTintIndex() : -1);
		        	for(EnumFacing face : EnumFacing.values())
		        		for(BakedQuad quad : ((ISecretBlock)block).phaseModel(new FakeBlockModel(te.getMirrorState())).getQuads(te.getMirrorState(), face, 0L))
		        			tintList.add(quad.hasTintIndex() ? quad.getTintIndex() : -1);
	        	}
	        	else
	        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
	        		.renderModel(world, ((ISecretBlock)block).phaseTrueModel(new TrueSightModel(new FakeBlockModel(renderState))),
	        				tileEntity.getWorld().getBlockState(tileEntity.getPos()), tileEntity.getPos(), Tessellator.getInstance().getBuffer(), false);
	        }
	        Collections.reverse(tintList);
	        boolean isColorBlock = false;
	        if(te.getMirrorState() != null)
	        	for(String s : SecretConfig.ALLOWED_BLOCKCOLORS) 
	        		if(s.equals(te.getMirrorState().getBlock().getRegistryName().toString()) && ((ISecretBlock)block).allowForcedBlockColors()) 
	        			isColorBlock = true;
	        if(!isHelmet)
	        	for(int i = 0; i < tessellator.getBuffer().getVertexCount(); i++)
	    	    {
	            	int sec = Math.floorDiv(i - 1, 4);
	            	if(!isColorBlock && (sec < 0 || tintList.size() <= sec || tintList.get(sec) < 0))
	            		continue;
	            	Color color = new Color(Minecraft.getMinecraft().getBlockColors()
	            			.colorMultiplier(te.getMirrorState(), tileEntity.getWorld(), tileEntity.getPos(), isColorBlock ? 1 : 1));
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
