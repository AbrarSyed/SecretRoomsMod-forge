package com.wynprice.secretroomsmod.render;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class FakeRenderChunkListed extends FakeRenderChunk 
{
	
	public FakeRenderChunkListed(World worldIn, RenderGlobal renderGlobalIn, int indexIn, RenderChunk render) 
	{
		super(worldIn, renderGlobalIn, indexIn, render);
	}
	
	private final int baseDisplayList = GLAllocation.generateDisplayLists(BlockRenderLayer.values().length);

	public int getDisplayList(BlockRenderLayer layer, CompiledChunk p_178600_2_)
	{
		return !p_178600_2_.isLayerEmpty(layer) ? this.baseDisplayList + layer.ordinal() : -1;
	}

	public void deleteGlResources()
	{
		super.deleteGlResources();
		GLAllocation.deleteDisplayLists(this.baseDisplayList, BlockRenderLayer.values().length);
	}
}
