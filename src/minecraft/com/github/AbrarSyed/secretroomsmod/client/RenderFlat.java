package com.github.AbrarSyed.secretroomsmod.client;

import com.github.AbrarSyed.secretroomsmod.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockCamoFull;
import com.github.AbrarSyed.secretroomsmod.blocks.BlockOneWay;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class RenderFlat implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		// unneeded
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block == SecretRooms.torchLever)
			return renderTorch(world, x, y, z, block);
		else if (block instanceof BlockCamoFull)
			return SRMRenderHelper.renderFullCamo(world, x, y, z, renderer, block);
		else if (block instanceof BlockOneWay)
			return SRMRenderHelper.renderOneSideCamo(world, x, y, z, renderer, block);
		else
			return renderer.renderStandardBlock(block, x, y, z);
	}
	
	private boolean renderTorch(IBlockAccess world, int x, int y, int z, Block block)
	{
		Icon icon = block.getBlockTexture(world, x, y, z, 0);
		int metadata = world.getBlockMetadata(x, y, z) & 7;
		Tessellator teselator = Tessellator.instance;
		teselator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		teselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var7 = 0.4000000059604645D;
		double var9 = 0.5D - var7;
		double var11 = 0.20000000298023224D;

		if (metadata == 1)
		{
			SRMRenderHelper.renderTorchAtAngle(icon, block, x - var9, y + var11, z, -var7, 0.0D, 0);
		}
		else if (metadata == 2)
		{
			SRMRenderHelper.renderTorchAtAngle(icon, block, x + var9, y + var11, z, var7, 0.0D, 0);
		}
		else if (metadata == 3)
		{
			SRMRenderHelper.renderTorchAtAngle(icon, block, x, y + var11, z - var9, 0.0D, -var7, 0);
		}
		else if (metadata == 4)
		{
			SRMRenderHelper.renderTorchAtAngle(icon, block, x, y + var11, z + var9, 0.0D, var7, 0);
		}
		else if (metadata == 5)
		{
			SRMRenderHelper.renderTorchAtAngle(icon, block, x, y, z, 0.0D, 0.0D, 0);
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return SecretRooms.renderFlatId;
	}

}
