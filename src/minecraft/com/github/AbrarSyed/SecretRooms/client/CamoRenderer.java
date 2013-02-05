package com.github.AbrarSyed.SecretRooms.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import com.github.AbrarSyed.SecretRooms.common.BlockCamoFull;
import com.github.AbrarSyed.SecretRooms.common.BlockOneWay;
import com.github.AbrarSyed.SecretRooms.common.FakeWorld;
import com.github.AbrarSyed.SecretRooms.common.SecretRooms;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamo;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamoFull;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class CamoRenderer implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		Tessellator tessellator = Tessellator.instance;

		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		// FULL BLOCK CAMO RENDERING
		if (block instanceof BlockCamoFull)
			return renderFullCamo(world, x, y, z, modelId, renderer, block);
		else if (block instanceof BlockOneWay)
			return renderOneSideCamo(world, x, y, z, renderer, block);
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
		return SecretRooms.camoRenderId;
	}

	public boolean renderOneSideCamo(IBlockAccess blockAccess, int i, int j, int k, RenderBlocks renderblocks, Block block)
	{
		// get colors
		int rawColors = block.colorMultiplier(blockAccess, i, j, k);
		float blockColorRed = (rawColors >> 16 & 0xff) / 255F;
		float blockColorGreen = (rawColors >> 8 & 0xff) / 255F;
		float blockColorBlue = (rawColors & 0xff) / 255F;

		float whiteColorRed = (0xffffff >> 16 & 0xff) / 255F;
		float whiteColorGreen = (0xffffff >> 8 & 0xff) / 255F;
		float whiteColorBlue = (0xffffff & 0xff) / 255F;

		// get Copied Texture
		TileEntityCamo entity = (TileEntityCamo) blockAccess.getBlockTileEntity(i, j, k);
		int texture = 0;
		boolean grassed = false;
		boolean currentlyBound = false;
		String textureFile = entity.getTexturePath();

		if (textureFile == null)
			textureFile = "/terrain.png";

		if (entity != null)
		{
			texture = entity.getTexture();
			grassed = (texture == 3 || texture == 0 || texture == 38) && textureFile.equals("/terrain.png");
		}

		if (textureFile.equals("/terrain.png") && !grassed && rawColors == 0xffffff)
			return renderblocks.renderStandardBlock(block, i, j, k);

		// get metadata
		int metadata = blockAccess.getBlockMetadata(i, j, k);

		if (rawColors == 0xffffff && textureFile.equals("/terrain.png"))
			return renderblocks.renderStandardBlock(block, i, j, k);

		renderblocks.enableAO = false;
		Tessellator var8 = Tessellator.instance;
		boolean var9 = false;
		float bottomWeight = 0.5F;
		float topWeight = 1.0F;
		float frontWeight = 0.8F;
		float sideWeight = 0.6F;

		float topR = topWeight * blockColorRed;
		float topG = topWeight * blockColorGreen;
		float topB = topWeight * blockColorBlue;

		float var17 = bottomWeight * blockColorRed;
		float var18 = frontWeight * blockColorRed;
		float var19 = sideWeight * blockColorRed;

		float var20 = bottomWeight * blockColorGreen;
		float var21 = frontWeight * blockColorGreen;
		float var22 = sideWeight * blockColorGreen;

		float var23 = bottomWeight * blockColorBlue;
		float var24 = frontWeight * blockColorBlue;
		float var25 = sideWeight * blockColorBlue;

		// ensures white if the top is uncolorized...
		if (metadata != 1)
		{
			topR = topWeight * whiteColorRed;
			topG = topWeight * whiteColorGreen;
			topB = topWeight * whiteColorBlue;
		}

		// changes colors for grassed stuff
		if (grassed)
		{
			var17 = bottomWeight;
			var18 = frontWeight;
			var19 = sideWeight;

			var20 = bottomWeight;
			var21 = frontWeight;
			var22 = sideWeight;

			var23 = bottomWeight;
			var24 = frontWeight;
			var25 = sideWeight;
		}

		int brightness = block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j, k);

		for (int side = 0; side < 6; side++)
		{
			if (!textureFile.equals("/terrain.png"))
			{
				// System.out.println("renderring special >> "+textureFile);
				ForgeHooksClient.bindTexture(textureFile, 0);
				currentlyBound = true;
			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i, j - 1, k, 0)) && side == 0)
			{
				var8.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j - 1, k));
				var8.setColorOpaque_F(var17, var20, var23);
				renderblocks.renderBottomFace(block, (double) i, (double) j, (double) k, block.getBlockTexture(renderblocks.blockAccess, i, j, k, 0));
			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i, j + 1, k, 1)) && side == 1)
			{
				var8.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j + 1, k));
				var8.setColorOpaque_F(topR, topG, topB);
				renderblocks.renderTopFace(block, (double) i, (double) j, (double) k, block.getBlockTexture(renderblocks.blockAccess, i, j, k, 1));
			}

			int tempTexture;

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i, j, k - 1, 2)) && side == 2)
			{
				var8.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j, k - 1));
				var8.setColorOpaque_F(var18, var21, var24);
				tempTexture = block.getBlockTexture(renderblocks.blockAccess, i, j, k, 2);
				renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (Tessellator.instance.defaultTexture && renderblocks.fancyGrass && tempTexture == 3 && renderblocks.overrideBlockTexture < 0)
				{
					var8.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
					renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, 38);
				}

			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i, j, k + 1, 3) && side == 3))
			{
				var8.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i, j, k + 1));
				var8.setColorOpaque_F(var18, var21, var24);
				tempTexture = block.getBlockTexture(renderblocks.blockAccess, i, j, k, 3);
				renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (Tessellator.instance.defaultTexture && renderblocks.fancyGrass && tempTexture == 3 && renderblocks.overrideBlockTexture < 0)
				{
					var8.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
					renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, 38);
				}
			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i - 1, j, k, 4)) && side == 4)
			{
				var8.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i - 1, j, k));
				var8.setColorOpaque_F(var19, var22, var25);
				tempTexture = block.getBlockTexture(renderblocks.blockAccess, i, j, k, 4);
				renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (Tessellator.instance.defaultTexture && renderblocks.fancyGrass && tempTexture == 3 && renderblocks.overrideBlockTexture < 0)
				{
					var8.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
					renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, 38);
				}

			}

			if ((renderblocks.renderAllFaces || block.shouldSideBeRendered(renderblocks.blockAccess, i + 1, j, k, 5)) && side == 5)
			{
				var8.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(renderblocks.blockAccess, i + 1, j, k));
				var8.setColorOpaque_F(var19, var22, var25);
				tempTexture = block.getBlockTexture(renderblocks.blockAccess, i, j, k, 5);
				renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (Tessellator.instance.defaultTexture && renderblocks.fancyGrass && tempTexture == 3 && renderblocks.overrideBlockTexture < 0)
				{
					var8.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
					renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, 38);
				}

			}

			if (currentlyBound)
			{
				ForgeHooksClient.unbindTexture();
				currentlyBound = false;
			}

		}

		return true;
	}

	public boolean renderFullCamo(IBlockAccess blockAccess, int i, int j, int k, int l, RenderBlocks renderblocks, Block block)
	{
		int rawColors = block.colorMultiplier(blockAccess, i, j, k);
		float blockColorRed = (rawColors >> 16 & 0xff) / 255F;
		float blockColorGreen = (rawColors >> 8 & 0xff) / 255F;
		float blockColorBlue = (rawColors & 0xff) / 255F;

		// get Copied ID
		int copyId = ((TileEntityCamoFull) blockAccess.getBlockTileEntity(i, j, k)).getCopyID();
		FakeWorld fake = SecretRooms.proxy.getFakeWorld(blockAccess.getBlockTileEntity(i, j, k).worldObj);

		if (copyId == 0)
		{
			return renderblocks.renderStandardBlock(block, i, j, k);
		}
		Block fakeBlock = block; //Block.blocksList[copyId];

		ForgeHooksClient.bindTexture(fakeBlock.getTextureFile(), 0);

		boolean flag;

		if (rawColors == 0xffffff && Minecraft.isAmbientOcclusionEnabled() && fakeBlock.getLightValue(blockAccess, i, j, k) == 0)
		{
			flag = renderblocks.renderStandardBlock(fakeBlock, i, j, k);
			ForgeHooksClient.unbindTexture();
			return flag;
		}

 		renderblocks.enableAO = false;
	Tessellator tessellator = Tessellator.instance;
	flag = false;
	float bottomWeight = 0.5F;
	float topWeight = 1.0F;
	float frontWeight = 0.8F;
	float sideWeight = 0.6F;

	float topR = topWeight * blockColorRed;
	float topG = topWeight * blockColorGreen;
	float topB = topWeight * blockColorBlue;

	float bottomColorRed = bottomWeight * blockColorRed;
	float frontColorRed = frontWeight * blockColorRed;
	float sideColorRed = sideWeight * blockColorRed;

	float bottomColorGreen = bottomWeight * blockColorGreen;
	float frontColorGreen = frontWeight * blockColorGreen;
	float sideColorGreen = sideWeight * blockColorGreen;

	float bottomColorBlue = bottomWeight * blockColorBlue;
	float frontColorBlue = frontWeight * blockColorBlue;
	float sideColorBlue = sideWeight * blockColorBlue;

	// changes colors for grassed stuff
	if (fakeBlock instanceof BlockGrass)
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

	// START------

	int brightness = fakeBlock.getMixedBrightnessForBlock(fake, i, j, k);

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i, j - 1, k, 0))
	{
		tessellator.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i, j - 1, k));
		tessellator.setColorOpaque_F(bottomColorRed, bottomColorGreen, bottomColorBlue);
		renderblocks.renderBottomFace(fakeBlock, (double) i, (double) j, (double) k, fakeBlock.getBlockTexture(fake, i, j, k, 0));
		flag = true;
	}

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i, j + 1, k, 1))
	{
		tessellator.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i, j + 1, k));
		tessellator.setColorOpaque_F(topR, topG, topB);
		renderblocks.renderTopFace(fakeBlock, (double) i, (double) j, (double) k, fakeBlock.getBlockTexture(fake, i, j, k, 1));
		flag = true;
	}

	int var28;

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i, j, k - 1, 2))
	{
		tessellator.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i, j, k - 1));
		tessellator.setColorOpaque_F(frontColorRed, frontColorGreen, frontColorBlue);
		var28 = fakeBlock.getBlockTexture(fake, i, j, k, 2);
		renderblocks.renderEastFace(fakeBlock, (double) i, (double) j, (double) k, var28);

		if (Tessellator.instance.defaultTexture && RenderBlocks.fancyGrass && var28 == 3 && renderblocks.overrideBlockTexture < 0)
		{
			tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
			renderblocks.renderEastFace(fakeBlock, (double) i, (double) j, (double) k, 38);
		}

		flag = true;
	}

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i, j, k + 1, 3))
	{
		tessellator.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i, j, k + 1));
		tessellator.setColorOpaque_F(frontColorRed, frontColorGreen, frontColorBlue);
		var28 = fakeBlock.getBlockTexture(fake, i, j, k, 3);
		renderblocks.renderWestFace(fakeBlock, (double) i, (double) j, (double) k, var28);

		if (Tessellator.instance.defaultTexture && RenderBlocks.fancyGrass && var28 == 3 && renderblocks.overrideBlockTexture < 0)
		{
			tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
			renderblocks.renderWestFace(fakeBlock, (double) i, (double) j, (double) k, 38);
		}

		flag = true;
	}

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i - 1, j, k, 4))
	{
		tessellator.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i - 1, j, k));
		tessellator.setColorOpaque_F(sideColorRed, sideColorGreen, sideColorBlue);
		var28 = fakeBlock.getBlockTexture(fake, i, j, k, 4);
		renderblocks.renderNorthFace(fakeBlock, (double) i, (double) j, (double) k, var28);

		if (Tessellator.instance.defaultTexture && RenderBlocks.fancyGrass && var28 == 3 && renderblocks.overrideBlockTexture < 0)
		{
			tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
			renderblocks.renderNorthFace(fakeBlock, (double) i, (double) j, (double) k, 38);
		}

		flag = true;
	}

	if (renderblocks.renderAllFaces || fakeBlock.shouldSideBeRendered(fake, i + 1, j, k, 5))
	{
		tessellator.setBrightness(renderblocks.renderMaxX < 1.0D ? brightness : fakeBlock.getMixedBrightnessForBlock(fake, i + 1, j, k));
		tessellator.setColorOpaque_F(sideColorRed, sideColorGreen, sideColorBlue);
		var28 = fakeBlock.getBlockTexture(fake, i, j, k, 5);
		renderblocks.renderSouthFace(fakeBlock, (double) i, (double) j, (double) k, var28);

		if (Tessellator.instance.defaultTexture && RenderBlocks.fancyGrass && var28 == 3 && renderblocks.overrideBlockTexture < 0)
		{
			tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
			renderblocks.renderSouthFace(fakeBlock, (double) i, (double) j, (double) k, 38);
		}

		flag = true;
	}
		ForgeHooksClient.unbindTexture();

		return flag;
	}
}
