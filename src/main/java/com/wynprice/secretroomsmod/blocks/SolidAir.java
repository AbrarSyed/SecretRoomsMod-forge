package com.wynprice.secretroomsmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class SolidAir extends Block
{
	public SolidAir() {
		super(Material.ROCK);
		setUnlocalizedName("solid_air");
		setRegistryName("solid_air");
		setSoundType(SoundType.GLASS);
		setHardness(4f);
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

}
