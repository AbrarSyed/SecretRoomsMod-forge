package mods.SecretRoomsMod.client;

import mods.SecretRoomsMod.SecretRooms;
import mods.SecretRoomsMod.blocks.BlockCamoFull;
import mods.SecretRoomsMod.blocks.BlockOneWay;
import mods.SecretRoomsMod.blocks.TileEntityCamoFull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class CamoRenderer implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		// copied vanilla render code....

		Tessellator tessellator = Tessellator.instance;

		block.setBlockBoundsForItemRender();
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
		tessellator.draw();

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block instanceof BlockCamoFull)
			return renderFullCamo(world, x, y, z, renderer, block);
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

		float whiteColor = 0xff / 255F;

		// get Copied Texture
		TileEntityCamoFull entity = (TileEntityCamoFull) blockAccess.getBlockTileEntity(i, j, k);
		boolean grassed = entity.getCopyID() == Block.grass.blockID;
		boolean currentlyBound = false;

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
				renderblocks.renderBottomFace(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 0));
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
				renderblocks.renderTopFace(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 1));
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
				renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
					renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
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
				renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(frontColorRed * blockColorRed, frontColorGreen * blockColorGreen, frontColorBlue * blockColorBlue);
					renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
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
				renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
					renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
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
				renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, tempTexture);

				if (RenderBlocks.fancyGrass && tempTexture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
				{
					tessellator.setColorOpaque_F(sideColorRed * blockColorRed, sideColorGreen * blockColorGreen, sideColorBlue * blockColorBlue);
					renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
				}

			}
		}

		return true;
	}

	public boolean renderFullCamo(IBlockAccess blockAccess, int i, int j, int k, RenderBlocks renderblocks, Block block)
	{
		int rawColors = block.colorMultiplier(blockAccess, i, j, k);
		float blockColorRed = (rawColors >> 16 & 0xff) / 255F;
		float blockColorGreen = (rawColors >> 8 & 0xff) / 255F;
		float blockColorBlue = (rawColors & 0xff) / 255F;
		// get Copied ID
		int copyId = ((TileEntityCamoFull) blockAccess.getBlockTileEntity(i, j, k)).getCopyID();
		if (copyId == 0)
			return renderblocks.renderStandardBlock(block, i, j, k);
		Block fakeBlock = Block.blocksList[copyId];

		// check for OneWay ID
		//		if (copyId == SecretRooms.oneWay.blockID)
		//			return renderOneSideCamo(SecretRooms.proxy.getFakeWorld(Minecraft.getMinecraft().theWorld), i, j, k, renderblocks, fakeBlock);

		// not need to render anything?? great.
		boolean flag;
		if (rawColors == 0xffffff && Minecraft.isAmbientOcclusionEnabled())
		{
			return renderblocks.renderStandardBlock(block, i, j, k);
		}

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

		int brightness = block.getMixedBrightnessForBlock(blockAccess, i, j, k);

		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j - 1, k, 0))
		{
			tessellator.setBrightness(renderblocks.renderMinY > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j - 1, k));
			tessellator.setColorOpaque_F(var17, var20, var23);
			renderblocks.renderBottomFace(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 0));
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j + 1, k, 1))
		{
			tessellator.setBrightness(renderblocks.renderMaxY < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j + 1, k));
			tessellator.setColorOpaque_F(var14, var15, var16);
			renderblocks.renderTopFace(block, i, j, k, renderblocks.getBlockIcon(block, blockAccess, i, j, k, 1));
			flag = true;
		}

		Icon texture;
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k - 1, 2))
		{
			tessellator.setBrightness(renderblocks.renderMinZ > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j, k - 1));
			tessellator.setColorOpaque_F(var18, var21, var24);
			texture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 2);
			renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
				renderblocks.renderEastFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i, j, k + 1, 3))
		{
			tessellator.setBrightness(renderblocks.renderMaxZ < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i, j, k + 1));
			tessellator.setColorOpaque_F(var18, var21, var24);
			texture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 3);
			renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var18 * blockColorRed, var21 * blockColorGreen, var24 * blockColorBlue);
				renderblocks.renderWestFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i - 1, j, k, 4))
		{
			tessellator.setBrightness(renderblocks.renderMinX > 0.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i - 1, j, k));
			tessellator.setColorOpaque_F(var19, var22, var25);
			texture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 4);
			renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
				renderblocks.renderNorthFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		if (renderblocks.renderAllFaces || block.shouldSideBeRendered(blockAccess, i + 1, j, k, 5))
		{
			tessellator.setBrightness(renderblocks.renderMaxX < 1.0D ? brightness : block.getMixedBrightnessForBlock(blockAccess, i + 1, j, k));
			tessellator.setColorOpaque_F(var19, var22, var25);
			texture = renderblocks.getBlockIcon(block, blockAccess, i, j, k, 5);
			renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, texture);
			if (RenderBlocks.fancyGrass && texture.getIconName().equals("grass_side") && !renderblocks.hasOverrideBlockTexture())
			{
				tessellator.setColorOpaque_F(var19 * blockColorRed, var22 * blockColorGreen, var25 * blockColorBlue);
				renderblocks.renderSouthFace(block, (double) i, (double) j, (double) k, BlockGrass.getIconSideOverlay());
			}
			flag = true;
		}
		return flag;
	}
}
