package com.wynprice.secretroomsmod.base.interfaces;

import java.util.HashMap;

import com.wynprice.secretroomsmod.SecretUtils;
import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISecretBlock extends ITileEntityProvider
{
	default public IBlockState getState(World world, BlockPos pos)
	{
		return world.getTileEntity(pos) instanceof TileEntityInfomationHolder ? ((TileEntityInfomationHolder)world.getTileEntity(pos)).getMirrorState() : null;
	}
	
	default public void forceBlockState(World world, BlockPos tePos, BlockPos hitPos, IBlockState state)
	{
		if(world.getTileEntity(tePos) instanceof TileEntityInfomationHolder)
			((TileEntityInfomationHolder)world.getTileEntity(tePos)).setMirrorStateForcable(state, hitPos);
	}
	
	@Override
	default TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityInfomationHolder();
	}
		
	@SideOnly(Side.CLIENT)
	default FakeBlockModel phaseModel(FakeBlockModel model)
	{
		return model;
	}
	
	@SideOnly(Side.CLIENT)
	default HashMap<Block, HashMap<Integer, IBakedModel>> getMap()
	{
		return SecretUtils.BAKEDMODELMAP;
	}
	
	@SideOnly(Side.CLIENT)
	default IBlockState overrideThisState(World world, BlockPos pos, IBlockState defaultState)
	{
		return defaultState;
	}
}
