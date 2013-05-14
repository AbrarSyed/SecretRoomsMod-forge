package com.github.AbrarSyed.secretroomsmod.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.github.AbrarSyed.secretroomsmod.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoFull;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockOneWay;
import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamo;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class Render3D implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		SRMRenderHelper.render3DInventory(block, metadata, modelID, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block instanceof BlockCamoFull)
			return SRMRenderHelper.renderFullCamo(world, x, y, z, renderer, block);
		else if (block instanceof BlockOneWay)
			return SRMRenderHelper.renderOneSideCamo(world, x, y, z, renderer, block);
		else
			return renderer.renderStandardBlock(block, x, y, z);
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return SecretRooms.render3DId;
	}
}
