package com.wynprice.secretroomsmod.render;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class CustomRenderMaps 
{
	public static final HashMap<Block, HashMap<Integer, IBakedModel>> ONE_WAY_GLASS_RENDER_MAP = new HashMap<>();
	
	public static final HashMap<Block, HashMap<Integer, IBakedModel>> DOOR_RENDER_MAP = new HashMap<>();

	public static final HashMap<Block, HashMap<Integer, IBakedModel>> TRAPDOOR_RENDER_MAP = new HashMap<>();

}
