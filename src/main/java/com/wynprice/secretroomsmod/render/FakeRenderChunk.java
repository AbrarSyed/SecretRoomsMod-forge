package com.wynprice.secretroomsmod.render;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public class FakeRenderChunk extends RenderChunk 
{
	private final RenderChunk oldRender;
	private FakeChunkCache worldView;
	
	public FakeRenderChunk(World worldIn, RenderGlobal renderGlobalIn, int indexIn, RenderChunk render) 
	{
		super(worldIn, renderGlobalIn, indexIn);
		this.oldRender = render;
	}
	
	@Override
	protected ChunkCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) 
	{
		try
		{
			ChunkCache oldCache = null;
			ArrayList<Method> allMethods = new ArrayList<>(Arrays.asList(this.oldRender.getClass().getDeclaredMethods()));
			//possible superclass method
			Class claz = this.oldRender.getClass().getSuperclass();
			while(claz != null)
			{
				allMethods.addAll(Arrays.asList(claz.getDeclaredMethods()));
				claz = claz.getSuperclass();
			}
			for(Method method : allMethods)
				if(method.getParameterCount() == 4 && method.getParameterTypes()[0] == World.class && method.getParameterTypes()[1] == BlockPos.class
					&& method.getParameterTypes()[2] == BlockPos.class && method.getParameterTypes()[3] == int.class)
				{
					method.setAccessible(true);
					oldCache = (ChunkCache) method.invoke(this.oldRender, world, from, to, subtract);
					method.setAccessible(false);
				}
			worldView = new FakeChunkCache(world, from, to, subtract, oldCache);
			return worldView;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return super.createRegionRenderCache(world, from, to, subtract);
	}

	@Override
	public void deleteGlResources() {
		super.deleteGlResources();
		this.oldRender.deleteGlResources();
	}
}
