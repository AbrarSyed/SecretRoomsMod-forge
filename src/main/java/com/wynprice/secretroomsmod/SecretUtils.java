package com.wynprice.secretroomsmod;

import java.util.HashMap;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class SecretUtils
{
	public static final HashMap<Block, HashMap<Integer, IBakedModel>> BAKEDMODELMAP = new HashMap<>();
	
	public static IBakedModel getModel(ISecretBlock base, IBlockState state) 
	{
		if(state == null || state.getBlock() == Blocks.AIR)
			return null;
		HashMap<Block, HashMap<Integer, IBakedModel>> map = base.getMap();
		if(!map.containsKey(state.getBlock()) || !map.get(state.getBlock()).containsKey(state.getBlock().getMetaFromState(state)))
		{
			if(!map.containsKey(state.getBlock()))
				map.put(state.getBlock(), createNewMap(state.getBlock().getMetaFromState(state), base.phaseModel(new FakeBlockModel(state))));
			map.get(state.getBlock()).put(state.getBlock().getMetaFromState(state), base.phaseModel(new FakeBlockModel(state)));
		}
		return map.get(state.getBlock()).get(state.getBlock().getMetaFromState(state));
	}
	
	public static <K, V> HashMap<K, V> createNewMap(K key, V value)
	{
		HashMap<K, V> map = new HashMap<>();
		map.put(key, value);
		return map;
	}
	
	public static IBakedModel getModel(ResourceLocation resourceLocation) 
	{
		IBakedModel bakedModel;
		IModel model;
		try {
	        model = ModelLoaderRegistry.getModel(resourceLocation);
		} catch (Exception e) {
          throw new RuntimeException(e);
		}
		bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.BLOCK,
				location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
	    return bakedModel;
	}
}
