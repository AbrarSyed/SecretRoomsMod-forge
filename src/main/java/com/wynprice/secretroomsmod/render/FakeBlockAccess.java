package com.wynprice.secretroomsmod.render;

import org.apache.logging.log4j.core.util.Loader;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;

public class FakeBlockAccess implements IBlockAccess
{
	
	private final World base;
	
	public FakeBlockAccess(World base) {
		this.base = base;
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		return base.getTileEntity(pos);
	}

	@Override
	public int getCombinedLight(BlockPos pos, int lightValue) {
		return base.getCombinedLight(pos, lightValue);
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		return base.getBlockState(pos).getBlock() instanceof ISecretBlock && ISecretTileEntity.getMirrorState(base, pos) != null
				? ISecretTileEntity.getMirrorState(base, pos) : base.getBlockState(pos);
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		return base.isAirBlock(pos);
	}

	@Override
	public Biome getBiome(BlockPos pos) {
		return base.getBiome(pos);
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return base.getStrongPower(pos, direction);
	}

	@Override
	public WorldType getWorldType() {
		return base.getWorldType();
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		return base.isSideSolid(pos, side, _default);
	}

}
