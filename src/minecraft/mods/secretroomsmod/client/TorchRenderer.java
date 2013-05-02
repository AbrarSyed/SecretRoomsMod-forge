package mods.secretroomsmod.client;

import mods.secretroomsmod.SecretRooms;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class TorchRenderer implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		// unneeded
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
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
			renderTorchAtAngle(icon, block, x - var9, y + var11, z, -var7, 0.0D, 0);
		}
		else if (metadata == 2)
		{
			renderTorchAtAngle(icon, block, x + var9, y + var11, z, var7, 0.0D, 0);
		}
		else if (metadata == 3)
		{
			renderTorchAtAngle(icon, block, x, y + var11, z - var9, 0.0D, -var7, 0);
		}
		else if (metadata == 4)
		{
			renderTorchAtAngle(icon, block, x, y + var11, z + var9, 0.0D, var7, 0);
		}
		else if (metadata == 5)
		{
			renderTorchAtAngle(icon, block, x, y, z, 0.0D, 0.0D, 0);
		}

		return true;
	}
	
    /**
     * Renders a torch at the given coordinates, with the base slanting at the given delta
     */
    public void renderTorchAtAngle(Icon icon, Block block, double par2, double par4, double par6, double par8, double par10, int par12)
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

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return SecretRooms.torchRenderId;
	}

}
