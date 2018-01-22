package com.wynprice.secretroomsmod.handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.renderer.block.model.VariantList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SecretModelHelper 
{
	public static ModelBlock getModelBlock(ResourceLocation location) throws IOException
	{
		if(CACHE_MODELS.containsKey(location))
			return CACHE_MODELS.get(location);
		ModelBlock block = ModelBlock.deserialize(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location.getResourceDomain(), "models/" + location.getResourcePath() + ".json")).getInputStream(), StandardCharsets.UTF_8));
		CACHE_MODELS.put(location, block);
		return block;
	}
	
	public static ModelBlock loadBlockAndFillParents(ResourceLocation location) throws IOException
	{
		ModelBlock base = getModelBlock(location);
		ModelBlock currentBlock = base;
		while(currentBlock.getParentLocation() != null)
		{
			ResourceLocation parentLocation = currentBlock.getParentLocation();
			ModelBlock parentBlock = getModelBlock(parentLocation);
			if(parentBlock.parent != null)
				break;
			Map<ResourceLocation, ModelBlock> models = Maps.<ResourceLocation, ModelBlock>newLinkedHashMap();
			models.put(parentLocation, parentBlock);
			currentBlock.getParentFromMap(models);
			currentBlock = parentBlock;
		}
		return base;
	}
	
	private static final HashMap<IBlockState, HashMap<Integer, HashMap<EnumFacing, BlockFaceUV>>> CACHE_BLOCKUV = new HashMap<>();
	private static final HashMap<ResourceLocation, ModelBlock> CACHE_MODELS = new HashMap<>();

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onModelBakeEvent(ModelBakeEvent event)
	{
		CACHE_BLOCKUV.clear();
		CACHE_MODELS.clear();
	}
	
	public static BlockFaceUV getUVFromState(IBlockState state, EnumFacing side, int t) throws IOException
	{
		if(CACHE_BLOCKUV.containsKey(state) && CACHE_BLOCKUV.get(state).containsKey(t) && CACHE_BLOCKUV.get(state).get(t).containsKey(side))
			return CACHE_BLOCKUV.get(state).get(t).get(side);
		ResourceLocation location = state.getBlock().getRegistryName();
		ModelBlockDefinition definition = ModelBlockDefinition.parseFromReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(location.getResourceDomain(), "blockstates/" + location.getResourcePath() + ".json")).getInputStream(), StandardCharsets.UTF_8), location);
		ModelResourceLocation modelLocation = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getBlockStateMapper().getVariants(state.getBlock()).get(state);
		BlockFaceUV blockuv = null;
		ArrayList<Variant> variantList = new ArrayList<>();
		if(definition.hasMultipartData())
			for(VariantList vList : definition.getMultipartVariants())
				variantList.addAll(vList.getVariantList());
		else
			variantList.addAll(definition.getVariant(modelLocation.getVariant()).getVariantList());
		ResourceLocation modLocation = null;
		ModelRotation rotation = null;
		Variant variant = t < variantList.size() ? variantList.get(t) : variantList.get(0);
		rotation = variant.getRotation();
		modLocation = variant.getModelLocation();
		ModelBlock modelblock = loadBlockAndFillParents(modLocation);
		if(!modelblock.getElements().isEmpty() && side != null && modelblock.getElements().get(0).mapFaces.get(rotation.rotate(side)) != null)
			blockuv = modelblock.getElements().get(0).mapFaces.get(rotation.rotate(side)).blockFaceUV;
		if(blockuv != null)
		{
			if(!CACHE_BLOCKUV.containsKey(state))
				CACHE_BLOCKUV.put(state, new HashMap<>());
			if(!CACHE_BLOCKUV.get(state).containsKey(t))
				CACHE_BLOCKUV.get(state).put(t, new HashMap<>());
			CACHE_BLOCKUV.get(state).get(t).put(side, blockuv);
		}
		return blockuv;
	}
}
