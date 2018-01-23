package com.wynprice.secretroomsmod.base;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.core.util.Loader;
import org.lwjgl.opengl.GL11;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifineHelper;
import com.wynprice.secretroomsmod.render.FakeBlockAccess;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.OneSidedRender;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
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
import net.minecraft.world.IBlockAccess;
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
	        Block block = tileEntity.getWorld().getBlockState(tileEntity.getPos()).getBlock();
	        ArrayList<Integer> tintList = new ArrayList<>();
	        currentRender = tileEntity.getWorld().getBlockState(tileEntity.getPos()).getActualState(tileEntity.getWorld(), tileEntity.getPos());
	        currentPos = tileEntity.getPos();
	        currentWorld = tileEntity.getWorld();
	        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
	        if(block instanceof ISecretBlock && te.getMirrorState() != null)
	        {
	        	IBlockState renderState = te.getMirrorState().getBlock().getActualState(te.getMirrorState(), tileEntity.getWorld(), tileEntity.getPos());
	            GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() || SecretConfig.forceAO ? 7425 : 7424);

        		currentRender = ((ISecretBlock)block).overrideThisState(world, currentPos, currentRender);
        		
        		IBlockAccess access = SecretOptifineHelper.IS_OPTIFINE ? new FakeBlockAccess(world) : world;
        		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        	if(!isHelmet)
	        	{
	        		try
	        		{
	            		if(((ISecretBlock)block).phaseModel(new FakeBlockModel(renderState)).getClass() == FakeBlockModel.class)
	            				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(renderState, tileEntity.getPos(), new FakeBlockAccess(world), buffer);
	            		else
			        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(access, ((ISecretBlock)block).phaseModel(new FakeBlockModel(renderState)),
			        				renderState, tileEntity.getPos(), buffer, true);
	        		}
	        		catch (Throwable t) {
	        			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(access, ((ISecretBlock)block).phaseModel(new FakeBlockModel(Blocks.STONE.getDefaultState())),
		        				Blocks.STONE.getDefaultState(), tileEntity.getPos(), buffer, true);
					}
	        	}
	        	else
	        		try
		        	{
		        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer()
		        		.renderModel(access, ((ISecretBlock)block).phaseTrueModel(new TrueSightModel(new FakeBlockModel(renderState))),
		        				tileEntity.getWorld().getBlockState(tileEntity.getPos()), tileEntity.getPos(), buffer, true);
		        	}
		        	catch (Throwable e) {
					}
	        	for(EnumFacing face : EnumFacing.values())
	     	        if(renderState.isOpaqueCube())
	     	        	if(!(world.getBlockState(tileEntity.getPos().offset(face)).getBlock() instanceof ISecretBlock) && world.getBlockState(tileEntity.getPos().offset(face)).shouldSideBeRendered(world, tileEntity.getPos().offset(face), face.getOpposite()) && 
	     	        			Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(world.getBlockState(tileEntity.getPos().offset(face)).getActualState(world, tileEntity.getPos().offset(face))) != Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.AIR.getDefaultState()))
		     	        		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, new OneSidedRender(world.getBlockState(tileEntity.getPos().offset(face)).getActualState(world, tileEntity.getPos().offset(face)), face.getOpposite()), 
		     	        				world.getBlockState(tileEntity.getPos().offset(face)).getBlock().getExtendedState(world.getBlockState(tileEntity.getPos().offset(face)), world, tileEntity.getPos().offset(face)), tileEntity.getPos().offset(face), buffer, true);
     	        		
	     	        		else if(world.getBlockState(tileEntity.getPos().offset(face)).getBlock() instanceof ISecretBlock && ISecretTileEntity.getMirrorState(world, tileEntity.getPos().offset(face)) != null && ISecretTileEntity.getMirrorState(world, tileEntity.getPos().offset(face)).shouldSideBeRendered(world, tileEntity.getPos().offset(face), face.getOpposite()) &&
		     	        			Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(ISecretTileEntity.getMirrorState(world, tileEntity.getPos().offset(face)).getActualState(world, tileEntity.getPos().offset(face))) != Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.AIR.getDefaultState()) &&
		     	        			((ISecretBlock)world.getBlockState(tileEntity.getPos().offset(face)).getBlock()).phaseModel(new FakeBlockModel(Blocks.STONE.getDefaultState())).getClass() == FakeBlockModel.class)
	     	        			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(world, new OneSidedRender(((ISecretBlock)world.getBlockState(tileEntity.getPos().offset(face)).getBlock()).phaseModel(new FakeBlockModel(ISecretTileEntity.getMirrorState(world, tileEntity.getPos().offset(face)).getActualState(world, tileEntity.getPos().offset(face)))), face.getOpposite()), 
			     	        			ISecretTileEntity.getMirrorState(world, currentPos).getBlock().getExtendedState(ISecretTileEntity.getMirrorState(world, currentPos), world, tileEntity.getPos().offset(face)), tileEntity.getPos().offset(face), buffer, true);
	        }
	       
	        tessellator.draw();
	        GlStateManager.shadeModel(7424);
	        Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);
        	GlStateManager.disableBlend();
	        RenderHelper.enableStandardItemLighting();
        }
    	GlStateManager.popMatrix();
	}

}
