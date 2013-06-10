package com.github.AbrarSyed.secretroomsmod.client;

import org.lwjgl.opengl.GL11;

import com.github.AbrarSyed.secretroomsmod.blocks.TileEntityCamo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public final class SRMRenderHelper
{

	private SRMRenderHelper()
	{
		// nothing
	}
	
    /**
     * Renders a torch at the given coordinates, with the base slanting at the given delta
     */
    public static void renderTorchAtAngle(Icon icon, Block block, double par2, double par4, double par6, double par8, double par10, int par12)
    {
        Tessellator tessellator = Tessellator.instance;

        double d5 = (double)icon.getMinU();
        double d6 = (double)icon.getMinV();
        double d7 = (double)icon.getMaxU();
        double d8 = (double)icon.getMaxV();
        double d9 = (double)icon.getInterpolatedU(7.0D);
        double d10 = (double)icon.getInterpolatedV(6.0D);
        double d11 = (double)icon.getInterpolatedU(9.0D);
        double d12 = (double)icon.getInterpolatedV(8.0D);
        double d13 = (double)icon.getInterpolatedU(7.0D);
        double d14 = (double)icon.getInterpolatedV(13.0D);
        double d15 = (double)icon.getInterpolatedU(9.0D);
        double d16 = (double)icon.getInterpolatedV(15.0D);
        par2 += 0.5D;
        par6 += 0.5D;
        double d17 = par2 - 0.5D;
        double d18 = par2 + 0.5D;
        double d19 = par6 - 0.5D;
        double d20 = par6 + 0.5D;
        double d21 = 0.0625D;
        double d22 = 0.625D;
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) - d21, par4 + d22, par6 + par10 * (1.0D - d22) - d21, d9, d10);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) - d21, par4 + d22, par6 + par10 * (1.0D - d22) + d21, d9, d12);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) + d21, par4 + d22, par6 + par10 * (1.0D - d22) + d21, d11, d12);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) + d21, par4 + d22, par6 + par10 * (1.0D - d22) - d21, d11, d10);
        tessellator.addVertexWithUV(par2 + d21 + par8, par4, par6 - d21 + par10, d15, d14);
        tessellator.addVertexWithUV(par2 + d21 + par8, par4, par6 + d21 + par10, d15, d16);
        tessellator.addVertexWithUV(par2 - d21 + par8, par4, par6 + d21 + par10, d13, d16);
        tessellator.addVertexWithUV(par2 - d21 + par8, par4, par6 - d21 + par10, d13, d14);
        tessellator.addVertexWithUV(par2 - d21, par4 + 1.0D, d19, d5, d6);
        tessellator.addVertexWithUV(par2 - d21 + par8, par4 + 0.0D, d19 + par10, d5, d8);
        tessellator.addVertexWithUV(par2 - d21 + par8, par4 + 0.0D, d20 + par10, d7, d8);
        tessellator.addVertexWithUV(par2 - d21, par4 + 1.0D, d20, d7, d6);
        tessellator.addVertexWithUV(par2 + d21, par4 + 1.0D, d20, d5, d6);
        tessellator.addVertexWithUV(par2 + par8 + d21, par4 + 0.0D, d20 + par10, d5, d8);
        tessellator.addVertexWithUV(par2 + par8 + d21, par4 + 0.0D, d19 + par10, d7, d8);
        tessellator.addVertexWithUV(par2 + d21, par4 + 1.0D, d19, d7, d6);
        tessellator.addVertexWithUV(d17, par4 + 1.0D, par6 + d21, d5, d6);
        tessellator.addVertexWithUV(d17 + par8, par4 + 0.0D, par6 + d21 + par10, d5, d8);
        tessellator.addVertexWithUV(d18 + par8, par4 + 0.0D, par6 + d21 + par10, d7, d8);
        tessellator.addVertexWithUV(d18, par4 + 1.0D, par6 + d21, d7, d6);
        tessellator.addVertexWithUV(d18, par4 + 1.0D, par6 - d21, d5, d6);
        tessellator.addVertexWithUV(d18 + par8, par4 + 0.0D, par6 - d21 + par10, d5, d8);
        tessellator.addVertexWithUV(d17 + par8, par4 + 0.0D, par6 - d21 + par10, d7, d8);
        tessellator.addVertexWithUV(d17, par4 + 1.0D, par6 - d21, d7, d6);
    }
    
    public static void render3DInventory(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
    	Tessellator tessellator = Tessellator.instance;
    	
		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
    
	public static boolean renderFullCamo(IBlockAccess world, int x, int y, int z, RenderBlocks renderblocks, Block block)
	{
		int rawColors = block.colorMultiplier(world, x, y, z);
		float blockColorRed = (rawColors >> 16 & 0xff) / 255F;
		float blockColorGreen = (rawColors >> 8 & 0xff) / 255F;
		float blockColorBlue = (rawColors & 0xff) / 255F;
		// get Copied ID
		int copyId = ((TileEntityCamo) world.getBlockTileEntity(x, y, z)).getCopyID();
		if (copyId == 0)
			return renderblocks.renderStandardBlock(block, x, y, z);
		Block fakeBlock = Block.blocksList[copyId];

		boolean flag;
		if (rawColors == 0xffffff && Minecraft.isAmbientOcclusionEnabled())
			return renderblocks.renderStandardBlock(block, x, y, z);

		renderblocks.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		flag = false;
		float bottomWeight = 0.5F;
		float topWeight = 1.0F;
		float frontWeight = 0.8F;
		float sideWeight = 0.6F;
		float var14 = topWeight * blockColorRed;
		float var15 = topWeight * blockColorGreen;
		float var16 = topWeight * blockColorBlue;
		float var17 = bottomWeight;
		float var18 = frontWeight;
		float var19 = sideWeight;
		float var20 = bottomWeight;
		float var21 = frontWeight;
		float var22 = sideWeight;
		float var23 = bottomWeight;
		float var24 = frontWeight;
		float var25 = sideWeight;
		if (fakeBlock != Block.grass || copyId != Block.grass.blockID)// (!(fakeBlock instanceof BlockGrass))
		{
			var17 = bottomWeight * blockColorRed;
			var18 = frontWeight * blockColorRed;
			var19 = sideWeight * blockColorRed;
			var20 = bottomWeight * blockColorGreen;
			var21 = frontWeight * blockColorGreen;
			var22 = sideWeight * blockColorGreen;
			var23 = bottomWeight * blockColorBlue;
			var24 = frontWeight * blockColorBlue;
			var25 = sideWeight * blockColorBlue;
		}

		int brightness = block.getMixedBrightnessForBlock(world, x, y, z);

		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x, y - 1, z, 0))
		{
			tessellator.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(world, x, y - 1, z));
			tessellator.setColorOpaque_F(var17, var20, var23);
			renderblocks.renderFaceYNeg(block, x, y, z, renderblocks.getBlockIcon(block, world, x, y, z, 0));
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x, y + 1, z, 1))
		{
			tessellator.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : block.getMixedBrightnessForBlock(world, x, y + 1, z));
			tessellator.setColorOpaque_F(var14, var15, var16);
			renderblocks.renderFaceYPos(block, x, y, z, renderblocks.getBlockIcon(block, world, x, y, z, 1));
			flag = true;
		}

		Icon texture;
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x, y, z - 1, 2))
		{
			tessellator.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : block.getMixedBrightnessForBlock(world, x, y, z - 1));
			tessellator.setColorOpaque_F(var18, var21, var24);
			texture = renderblocks.getBlockIcon(block, world, x, y, z, 2);
			renderblocks.renderFaceZNeg(block, x, y, z, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
				renderblocks.renderFaceZNeg(block, x, y, z, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x, y, z + 1, 3))
		{
			tessellator.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(world, x, y, z + 1));
			tessellator.setColorOpaque_F(var18, var21, var24);
			texture = renderblocks.getBlockIcon(block, world, x, y, z, 3);
			renderblocks.renderFaceZPos(block, x, y, z, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
				renderblocks.renderFaceZPos(block, x, y, z, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x - 1, y, z, 4))
		{
			tessellator.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : block.getMixedBrightnessForBlock(world, x - 1, y, z));
			tessellator.setColorOpaque_F(var19, var22, var25);
			texture = renderblocks.getBlockIcon(block, world, x, y, z, 4);
			renderblocks.renderFaceXNeg(block, x, y, z, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
				renderblocks.renderFaceXNeg(block, x, y, z, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(world, x + 1, y, z, 5))
		{
			tessellator.setBrightness(renderblocks.renderMaxX < 1.0D ? brightness : block.getMixedBrightnessForBlock(world, x + 1, y, z));
			tessellator.setColorOpaque_F(var19, var22, var25);
			texture = renderblocks.getBlockIcon(block, world, x, y, z, 5);
			renderblocks.renderFaceXPos(block, x, y, z, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
				renderblocks.renderFaceXPos(block, x, y, z, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		return flag;
	}
	
	public static boolean renderOneSideCamo(IBlockAccess blockAccess, int i, int j, int k, RenderBlocks renderblocks, Block block)
	{
		// get colors
		int rawColors = block.colorMultiplier(blockAccess, i, j, k);
		float blockColorRed = (rawColors >> 16 & 0xff) / 255F;
		float blockColorGreen = (rawColors >> 8 & 0xff) / 255F;
		float blockColorBlue = (rawColors & 0xff) / 255F;

		float whiteColor = 0xff / 255F;

		// get Copied Texture
		TileEntityCamo entity = (TileEntityCamo) blockAccess.getBlockTileEntity(i, j, k);
		boolean grassed = entity.getCopyID() == Block.grass.blockID;
		if (!grassed && rawColors == 0xffffff)
			return renderblocks.renderStandardBlock(block, i, j, k);

		// get metadata
		int metadata = blockAccess.getBlockMetadata(i, j, k);

		renderblocks.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		float bottomWeight = 0.5F;
		float topWeight = 1.0F;
		float frontWeight = 0.8F;
		float sideWeight = 0.6F;

		float topColorRed = topWeight * blockColorRed;
		float topColorGreen = topWeight * blockColorGreen;
		float topColorBlue = topWeight * blockColorBlue;

		float bottomColorRed = bottomWeight * blockColorRed;
		float frontColorRed = frontWeight * blockColorRed;
		float sideColorRed = sideWeight * blockColorRed;

		float bottomColorGreen = bottomWeight * blockColorGreen;
		float frontColorGreen = frontWeight * blockColorGreen;
		float sideColorGreen = sideWeight * blockColorGreen;

		float bottomColorBlue = bottomWeight * blockColorBlue;
		float frontColorBlue = frontWeight * blockColorBlue;
		float sideColorBlue = sideWeight * blockColorBlue;

		// ensures white if the top is uncolorized...

		// changes colors for grassed stuff
		if (grassed)
		{
			bottomColorRed = bottomWeight;
			frontColorRed = frontWeight;
			sideColorRed = sideWeight;

			bottomColorGreen = bottomWeight;
			frontColorGreen = frontWeight;
			sideColorGreen = sideWeight;

			bottomColorBlue = bottomWeight;
			frontColorBlue = frontWeight;
			sideColorBlue = sideWeight;
		}

		int brightness = block.getMixedBrightnessForBlock(blockAccess, i, j, k);

		for (int side = 0; side < 6; side++)
		{
			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0)) && side == 0)
			{
				tessellator.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(bottomColorRed, bottomColorGreen, bottomColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				renderblocks.renderFaceYNeg(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 0));
			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1)) && side == 1)
			{
				tessellator.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(topColorRed, topColorGreen, topColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				renderblocks.renderFaceYPos(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 1));
			}

			Icon tempTexture;

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2)) && side == 2)
			{
				tessellator.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(frontColorRed, frontColorGreen, frontColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				tessellator.setColorOpaque_F(frontColorRed, frontColorGreen, frontColorBlue);
				tempTexture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 2);
				renderblocks.renderFaceZNeg(block, i, j, k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
					renderblocks.renderFaceZNeg(block, i, j, k, BlockGrass.getIconSideOverlay());
				}

			}

			if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3) && side == 3)
			{
				tessellator.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(frontColorRed, frontColorGreen, frontColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				tempTexture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 3);
				renderblocks.renderFaceZPos(block, i, j, k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
					renderblocks.renderFaceZPos(block, i, j, k, BlockGrass.getIconSideOverlay());
				}
			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4)) && side == 4)
			{
				tessellator.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(sideColorRed, sideColorGreen, sideColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				tempTexture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 4);
				renderblocks.renderFaceXNeg(block, i, j, k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
					renderblocks.renderFaceXNeg(block, i, j, k, BlockGrass.getIconSideOverlay());
				}

			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5)) && side == 5)
			{
				tessellator.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k));
				if (metadata == side)
				{
					tessellator.setColorOpaque_F(sideColorRed, sideColorGreen, sideColorBlue);
				}
				else
				{
					tessellator.setColorOpaque_F(whiteColor, whiteColor, whiteColor);
				}
				tempTexture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 5);
				renderblocks.renderFaceXPos(block, i, j, k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
					renderblocks.renderFaceXPos(block, i, j, k, BlockGrass.getIconSideOverlay());
				}

			}
		}

		return true;
	}

}
