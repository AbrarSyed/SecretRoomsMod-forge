package com.wynprice.secretroomsmod.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class FakeChunkRenderFactory implements IRenderChunkFactory 
{
	private final IRenderChunkFactory base;
	
	public FakeChunkRenderFactory(IRenderChunkFactory base) {
		this.base = base;
	}
	
	@Override
	public RenderChunk create(World worldIn, RenderGlobal renderGlobalIn, int index) {
		return OpenGlHelper.useVbo() ? new FakeRenderChunk(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index)) : new FakeRenderChunkListed(worldIn, renderGlobalIn, index, base.create(worldIn, renderGlobalIn, index));
	}

}
