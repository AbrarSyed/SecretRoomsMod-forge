package com.wynprice.secretroomsmod.render;

import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.language.bm.Rule.RPattern;

import com.google.common.collect.ImmutableMap;
import com.wynprice.secretroomsmod.render.fakemodels.ProxyBlock;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStateProxy extends BlockStateBase
{
	private final IBlockState base;
	private final BlockPos pos;
	
	public BlockStateProxy(IBlockState base, BlockPos pos) 
	{
		this.base = base;
		this.pos = pos;
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, BlockPos pos, int id, int param) {
		return base.onBlockEventReceived(worldIn, pos, id, param);
	}

	@Override
	public void neighborChanged(World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		base.neighborChanged(worldIn, pos, blockIn, fromPos);
	}

	@Override
	public Material getMaterial() {
		return base.getMaterial();
	}

	@Override
	public boolean isFullBlock() {
		return base.isFullBlock();
	}

	@Override
	public boolean canEntitySpawn(Entity entityIn) {
		return base.canEntitySpawn(entityIn);
	}

	@Override
	public int getLightOpacity() {
		return base.getLightOpacity();
	}

	@Override
	public int getLightOpacity(IBlockAccess world, BlockPos pos) {
		return base.getLightOpacity(world, pos);
	}

	@Override
	public int getLightValue() {
		return base.getLightValue();
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		return base.getLightValue(world, pos);
	}

	@Override
	public boolean isTranslucent() {
		return Minecraft.getMinecraft().world.getBlockState(pos).isTranslucent();
	}

	@Override
	public boolean useNeighborBrightness() {
		return base.useNeighborBrightness();
	}

	@Override
	public MapColor getMapColor(IBlockAccess p_185909_1_, BlockPos p_185909_2_) {
		return base.getMapColor(p_185909_1_, p_185909_2_);
	}

	@Override
	public IBlockState withRotation(Rotation rot) {
		return base.withRotation(rot);
	}

	@Override
	public IBlockState withMirror(Mirror mirrorIn) {
		return base.withMirror(mirrorIn);
	}

	@Override
	public boolean isFullCube() {
		return base.isFullCube();
	}

	@Override
	public boolean hasCustomBreakingProgress() {
		return base.hasCustomBreakingProgress();
	}

	@Override
	public EnumBlockRenderType getRenderType() {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public int getPackedLightmapCoords(IBlockAccess source, BlockPos pos) {
		return base.getPackedLightmapCoords(source, pos);
	}

	@Override
	public float getAmbientOcclusionLightValue() {
		return base.getAmbientOcclusionLightValue();
	}

	@Override
	public boolean isBlockNormalCube() {
		return base.isBlockNormalCube();
	}

	@Override
	public boolean isNormalCube() {
		return base.isNormalCube();
	}

	@Override
	public boolean canProvidePower() {
		return base.canProvidePower();
	}

	@Override
	public int getWeakPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return base.getWeakPower(blockAccess, pos, side);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return base.hasComparatorInputOverride();
	}

	@Override
	public int getComparatorInputOverride(World worldIn, BlockPos pos) {
		return base.getComparatorInputOverride(worldIn, pos);
	}

	@Override
	public float getBlockHardness(World worldIn, BlockPos pos) {
		return base.getBlockHardness(worldIn, pos);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer player, World worldIn, BlockPos pos) {
		return base.getPlayerRelativeBlockHardness(player, worldIn, pos);
	}

	@Override
	public int getStrongPower(IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return base.getStrongPower(blockAccess, pos, side);
	}

	@Override
	public EnumPushReaction getMobilityFlag() {
		return base.getMobilityFlag();
	}

	@Override
	public IBlockState getActualState(IBlockAccess blockAccess, BlockPos pos) {
		return base.getActualState(blockAccess, pos);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return base.getSelectedBoundingBox(worldIn, pos);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess blockAccess, BlockPos pos, EnumFacing facing) {
		return base.shouldSideBeRendered(blockAccess, pos, facing);
	}

	@Override
	public boolean isOpaqueCube() {
		return base.isOpaqueCube();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockAccess worldIn, BlockPos pos) {
		return base.getCollisionBoundingBox(worldIn, pos);
	}

	@Override
	public void addCollisionBoxToList(World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185908_6_) {
		base.addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, p_185908_6_);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, BlockPos pos) {
		return base.getBoundingBox(blockAccess, pos);
	}

	@Override
	public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
		return base.collisionRayTrace(worldIn, pos, start, end);
	}

	@Override
	public boolean isTopSolid() {
		return base.isTopSolid();
	}

	@Override
	public boolean doesSideBlockRendering(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return Minecraft.getMinecraft().world.getBlockState(pos).doesSideBlockRendering(world, pos, side);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return base.isSideSolid(world, pos, side);
	}

	@Override
	public Vec3d getOffset(IBlockAccess access, BlockPos pos) {
		return base.getOffset(access, pos);
	}

	@Override
	public boolean causesSuffocation() {
		return base.causesSuffocation();
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
		return base.getBlockFaceShape(worldIn, pos, facing);
	}

	@Override
	public Collection<IProperty<?>> getPropertyKeys() {
		return base.getPropertyKeys();
	}

	@Override
	public <T extends Comparable<T>> T getValue(IProperty<T> property) {
		return base.getValue(property);
	}

	@Override
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
		return base.withProperty(property, value);
	}

	@Override
	public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property) {
		return base.cycleProperty(property);
	}

	@Override
	public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
		return base.getProperties();
	}

	@Override
	public Block getBlock() {
		ProxyBlock.isOpaque = base.isOpaqueCube();
		return new ProxyBlock(base);
	}
}
