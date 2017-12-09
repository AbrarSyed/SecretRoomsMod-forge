package com.wynprice.secretroomsmod.render.fakemodels;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

public class ProxyBlock extends Block
{
	public static boolean isOpaque;
	
	private boolean hasLoaded = false;
	
	private final IBlockState state;
	
	public ProxyBlock(IBlockState state) 
	{
		super(state.getMaterial());
		this.state = state;
		hasLoaded = true;
		setDefaultState(this.state.getBlock().getDefaultState());
	}

	@Override
	public boolean hasTileEntity(IBlockState state) 
	{
		return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return hasLoaded ? this.state.isOpaqueCube() : this.isOpaque;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return this.state.getRenderType();
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return this.state.getBlock().canRenderInLayer(this.state, layer);
	}
	
	
}

