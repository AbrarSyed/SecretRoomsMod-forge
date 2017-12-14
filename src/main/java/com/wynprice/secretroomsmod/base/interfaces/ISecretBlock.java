package com.wynprice.secretroomsmod.base.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.handler.ParticleHandler;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public interface ISecretBlock extends ITileEntityProvider
{
	default public IBlockState getState(IBlockAccess world, BlockPos pos)
	{
		return world.getTileEntity(pos) instanceof ISecretTileEntity ? ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() : null;
	}
	
	default public void forceBlockState(World world, BlockPos tePos, BlockPos hitPos, IBlockState state)
	{
		if(world.getTileEntity(tePos) instanceof ISecretTileEntity)
			((ISecretTileEntity)world.getTileEntity(tePos)).setMirrorStateForcable(state, hitPos);
	}
	
	@Override
	default TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityInfomationHolder();
	}
	
	default IBlockAccess phaseBlockAccess(IBlockAccess world) {
		return new IBlockAccess() {
			
			@Override
			public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
				return world.isSideSolid(pos, side, _default);
			}
			
			@Override
			public boolean isAirBlock(BlockPos pos) {
				return world.isAirBlock(pos);
			}
			
			@Override
			public WorldType getWorldType() {
				return world.getWorldType();
			}
			
			@Override
			public TileEntity getTileEntity(BlockPos pos) {
				return world.getTileEntity(pos);
			}
			
			@Override
			public int getStrongPower(BlockPos pos, EnumFacing direction) {
				return world.getStrongPower(pos, direction);
			}
			
			@Override
			public int getCombinedLight(BlockPos pos, int lightValue) {
				return world.getCombinedLight(pos, lightValue);
			}
			
			@Override
			public IBlockState getBlockState(BlockPos pos) 
			{
				return ISecretTileEntity.getMirrorState(world, pos).getBlock() == Blocks.AIR ? world.getBlockState(pos) : ISecretTileEntity.getMirrorState(world, pos);
			}
			
			@Override
			public Biome getBiome(BlockPos pos) {
				return world.getBiome(pos);
			}
		};
	}
	
	default boolean allowForcedBlockColors() {
		return true;
	}
	
	default void onMessageRecieved(World world, BlockPos pos){
	}
	
	@SideOnly(Side.CLIENT)
	default FakeBlockModel phaseModel(FakeBlockModel model){
		return model;
	}
	
	@SideOnly(Side.CLIENT)
	default TrueSightModel phaseTrueModel(TrueSightModel model)
	{
		return model;
	}
	
	@SideOnly(Side.CLIENT)
	default IBlockState overrideThisState(World world, BlockPos pos, IBlockState defaultState)
	{
		return defaultState;
	}
	
	
	
	default AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(source.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)source.getTileEntity(pos)).getMirrorState() != null)
			return ((ISecretTileEntity)source.getTileEntity(pos)).getMirrorState().getBoundingBox(source, pos);
		return Block.FULL_BLOCK_AABB;
	}
	
	default boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return getState(world, pos).getBlock().canBeConnectedTo(world, pos, facing);
	}
	
	default BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return ISecretTileEntity.getMirrorState(worldIn, pos).getBlockFaceShape(worldIn, pos, face);
	}
	
	public static final ArrayList<TileEntity> ALL_SECRET_TILE_ENTITIES = new ArrayList<>();
	
	default Material getMaterial(IBlockState state, Material material) 
	{
		IBlockState blockstate = null;
		ArrayList<TileEntity> list = new ArrayList<>(ALL_SECRET_TILE_ENTITIES);
		for(TileEntity tileentity : list)
			if(tileentity.getWorld() != null && tileentity.getWorld().isBlockLoaded(tileentity.getPos()) && tileentity.getWorld().getBlockState(tileentity.getPos()) == state && tileentity instanceof ISecretTileEntity)
				blockstate = ISecretTileEntity.getMirrorState(tileentity.getWorld(), tileentity.getPos());
		return blockstate != null && !(blockstate.getBlock() instanceof ISecretBlock) && blockstate.getMaterial() != Material.WATER? blockstate.getMaterial() : material;
	}
	
	default boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ISecretTileEntity.getMirrorState(world, pos).isSideSolid(world, pos, side);
	}
	
	default void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) 
	{
		if(worldIn.getTileEntity(pos) instanceof ISecretTileEntity && ISecretTileEntity.getMirrorState(worldIn, pos) != null)
			ISecretTileEntity.getMirrorState(worldIn, pos).addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
		else
			Blocks.STONE.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
	}
	
	default SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return world.getTileEntity(pos) instanceof ISecretTileEntity && ISecretTileEntity.getMirrorState(world, pos) != null ? 
				ISecretTileEntity.getMirrorState(world, pos).getBlock().getSoundType() : SoundType.STONE;
	}
	
	@SideOnly(Side.CLIENT)
	default boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) 
	{
		if (target.getBlockPos() != null && worldObj.getTileEntity(target.getBlockPos()) instanceof ISecretTileEntity && 
				((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState() != null)
        {
            int i = target.getBlockPos().getX();
            int j = target.getBlockPos().getY();
            int k = target.getBlockPos().getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = ((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState().getBoundingBox(worldObj, target.getBlockPos());
            double d0 = (double)i + new Random().nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
            double d1 = (double)j + new Random().nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
            double d2 = (double)k + new Random().nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
            if (target.sideHit == EnumFacing.DOWN) d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.UP) d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
            if (target.sideHit == EnumFacing.NORTH) d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.SOUTH) d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
            if (target.sideHit == EnumFacing.WEST) d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.EAST) d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
            manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
            		.createParticle(0, worldObj, d0, d1, d2, 0, 0, 0, 
            				Block.getStateId(((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState())))
            		.setBlockPos(target.getBlockPos()).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
            return true;
        }
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	default boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) 
	{
		if(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos) != null)
		{
			IBlockState state = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getActualState(world, pos);
	        int i = 4;

	        for (int j = 0; j < 4; ++j)
	        {
	            for (int k = 0; k < 4; ++k)
	            {
	                for (int l = 0; l < 4; ++l)
	                {
	                    double d0 = ((double)j + 0.5D) / 4.0D;
	                    double d1 = ((double)k + 0.5D) / 4.0D;
	                    double d2 = ((double)l + 0.5D) / 4.0D;
	                    manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
	                    		.createParticle(0, world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2,
	                    				d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, Block.getStateId(state))).setBlockPos(pos));
	                }
	            }
	        }
		}
		return false;
	}
}
