package com.wynprice.secretroomsmod.integration.ctm;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

public class SecretCompatCTM {
	
	public static List<BakedQuad> getQuads(IBakedModel model, IBlockState state, EnumFacing side, long rand) {
		BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
		ForgeHooksClient.setRenderLayer(state.getBlock().getBlockLayer());
		List<BakedQuad> ret = model.getQuads(state, side, rand);
		ForgeHooksClient.setRenderLayer(layer);
		return ret;
	}
}
