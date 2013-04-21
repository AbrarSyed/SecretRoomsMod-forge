package mods.SecretRoomsMod.client;

import mods.SecretRoomsMod.SecretRooms;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
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
		int metadata = world.getBlockMetadata(x, y, z) & 7;
		Tessellator teselator = Tessellator.instance;
		teselator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
		teselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		double var7 = 0.4000000059604645D;
		double var9 = 0.5D - var7;
		double var11 = 0.20000000298023224D;

		if (metadata == 1)
		{
			renderer.renderTorchAtAngle(block, x - var9, y + var11, z, -var7, 0.0D, 0);
		}
		else if (metadata == 2)
		{
			renderer.renderTorchAtAngle(block, x + var9, y + var11, z, var7, 0.0D, 0);
		}
		else if (metadata == 3)
		{
			renderer.renderTorchAtAngle(block, x, y + var11, z - var9, 0.0D, -var7, 0);
		}
		else if (metadata == 4)
		{
			renderer.renderTorchAtAngle(block, x, y + var11, z + var9, 0.0D, var7, 0);
		}
		else if (metadata == 5)
		{
			renderer.renderTorchAtAngle(block, x, y, z, 0.0D, 0.0D, 0);
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
		return SecretRooms.torchRenderId;
	}

}
