package com.wynprice.secretroomsmod.integration.malisisdoors.malisisrenders;

import java.util.List;

import com.wynprice.secretroomsmod.integration.malisisdoors.registries.blocks.SecretMalisisTrapDoorBlock;

import net.malisis.doors.renderer.DoorRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class SecretMalisisItemRenderer extends DoorRenderer {
		
	public SecretMalisisItemRenderer() {
		ensureBlock(SecretMalisisTrapDoorBlock.class);
		
	}
	
	//Malisisdoors overwrites item rendering with asm. There probally is an easier way to fix this, but rendering the item the vanilla way works fine
	@Override
	public synchronized boolean renderItem(ItemStack stack, float partialTick) {
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, null, null);
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), stack);
        }

        this.renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), stack);
        tessellator.draw();
		return true;
	}
	
	 private void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, ItemStack stack)
	    {
	        int i = 0;

	        for (int j = quads.size(); i < j; ++i)
	        {
	            BakedQuad bakedquad = quads.get(i);
	            int k = -1;

	            if (bakedquad.hasTintIndex())
	            {
	                k = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

	                if (EntityRenderer.anaglyphEnable)
	                {
	                    k = TextureUtil.anaglyphColor(k);
	                }

	                k = k | -16777216;
	            }

	            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
	        }
	    }
}
