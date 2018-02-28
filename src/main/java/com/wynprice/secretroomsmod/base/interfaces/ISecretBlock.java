package com.wynprice.secretroomsmod.base.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.handler.ParticleHandler;
import com.wynprice.secretroomsmod.render.FakeBlockAccess;
import com.wynprice.secretroomsmod.render.RenderStateUnlistedProperty;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.SecretBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

/**
 * The interface used by all SRM blocks. Most methods here are called directly from the block, as to store all the same code in the same place. 
 * Extends {@link ITileEntityProvider} as all SRM blocks have TileEntities. 
 * <br>Not needed but means,
 * <br>{@code public class SRMBlock implements ISecretBlock, ITileEntityProvider}
 * <br>becomes:
 * <br>{@code public class SRMBlock implements ISecretBlock}
 * @author Wyn Price
 *
 */
public interface ISecretBlock extends ITileEntityProvider
{
	
	/**
	 * The unlisted property used to store the RenderState
	 */
	IUnlistedProperty<IBlockState> RENDER_PROPERTY = new RenderStateUnlistedProperty();
	
	/**
	 * Used to get the renderState from the World and the position
	 * @param world the world the blocks in
	 * @param pos the position of the block
	 * @return the rendered state of the block, or null if there is none
	 */
	default public IBlockState getState(IBlockAccess world, BlockPos pos)
	{
		return world.getTileEntity(pos) instanceof ISecretTileEntity ? ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() : null;
	}
	
	/**
	 * Used to force the block render state to the {@link ISecretTileEntity}, if it exists.
	 * @param world the world the SRM blocks in
	 * @param tePos the position of the block 
	 * @param state the state of which to force the block to
	 */
	default public void forceBlockState(World world, BlockPos tePos, IBlockState state)
	{
		if(world.getTileEntity(tePos) instanceof ISecretTileEntity)
			((ISecretTileEntity)world.getTileEntity(tePos)).setMirrorStateForcable(state);
	}
	
	@Override
	default TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityInfomationHolder();
	}

	/**
	 * Used so individual blocks can change the BlockModel for rendering. Used for things like Glass and Doors to make them different
	 * @param model the original model, will be returned if the Block dosn't need different visuals
	 * @return the model that will be rendered
	 */
	@SideOnly(Side.CLIENT)
	default FakeBlockModel phaseModel(FakeBlockModel model) {
		return model;
	}
	
	/**
	 * Used so individual blocks can change the BlockModel for rendering, when the Helmet of True Sight is on.
	 * @param model the original model, will be returned if the Block dosn't need different visuals
	 * @return the model that will be rendered
	 */
	@SideOnly(Side.CLIENT)
	default TrueSightModel phaseTrueModel(TrueSightModel model) {
		return model;
	}
	
	/**
	 * Used as an override to SRM blocks. Used to attempt to get the Mirrored State bounding box
	 * @param state The input state. not needed what so ever. <b> Exists because {@link Block#getBoundingBox(IBlockState, IBlockAccess, BlockPos)} has the {@link IBlockState} as a field. </b>
	 * @param source the world
	 * @param pos the position
	 * @return the bounding box of the Mirrored State, or {@link Block#FULL_BLOCK_AABB} if does there is no tileEntity at the position.
	 */
	default AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(source.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)source.getTileEntity(pos)).getMirrorState() != null)
			return ((ISecretTileEntity)source.getTileEntity(pos)).getMirrorState().getBoundingBox(source, pos);
		return Block.FULL_BLOCK_AABB;
	}
	
	/**
	 * Used as an override to SRM blocks. Used to run {@link Block#canBeConnectedTo(IBlockAccess, BlockPos, EnumFacing)} on the MirrorState
     * @param world The current world
     * @param pos The position of the block
     * @param facing The side the connecting block is on
     * @return True to allow another block to connect to this block
     */
	default boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return getState(world, pos).getBlock().canBeConnectedTo(world, pos, facing);
	}
	
	/**
	 * Used as an override to SRM blocks. Used to run {@link Block#getBlockFaceShape(IBlockAccess, IBlockState, BlockPos, EnumFacing)} on Mirror State.
	 * @param worldIn The current world
	 * @param state The {@link IBlockState} of the current position
	 * @param pos The position of the block
	 * @param face The side thats being checked
	 * @return an approximation of the form of the given face.
	 */
	default BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return ISecretTileEntity.getMirrorState(worldIn, pos).getBlockFaceShape(worldIn, pos, face);
	}
	
	/**
	 * A list of all ISecretTileEntities. Used to get Material and things like that
	 */
	public static final ArrayList<TileEntity> ALL_SECRET_TILE_ENTITIES = new ArrayList<>();
	
	/**
	 * Used as an override to SRM blocks. Used to run {@link Block#getMaterial(IBlockState)}, if exists. 
	 * @param state The BlockState of the SRM block
	 * @param material The default material of the SRM block
	 * @return the material of the Mirrored IBlockState, or {@code material} if it cannot be found
	 */
	default Material getMaterial(IBlockState state, Material material) 
	{
		IBlockState blockstate = null;
		ArrayList<TileEntity> list = new ArrayList<>(ALL_SECRET_TILE_ENTITIES);
		for(TileEntity tileentity : list)
			if(tileentity.getWorld() != null && tileentity.getWorld().isBlockLoaded(tileentity.getPos()) && tileentity.getWorld().getBlockState(tileentity.getPos()) == state && tileentity instanceof ISecretTileEntity)
				blockstate = ISecretTileEntity.getMirrorState(tileentity.getWorld(), tileentity.getPos());
		return blockstate != null && !(blockstate.getBlock() instanceof ISecretBlock) && blockstate.getMaterial() != Material.WATER? blockstate.getMaterial() : material;
	}
	
	/**
	 * Used as an override to SRM blocks. Used to run {@link Block#isSideSolid(IBlockState, IBlockAccess, BlockPos, EnumFacing)} on the mirrored state
	 * @param base_state The base state, getActualState should be called first
     * @param world The current world
     * @param pos Block position in world
     * @param side The side to check
     * @return True if the mirrored state is solid on the specified side.
	 */
	default boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ISecretTileEntity.getMirrorState(world, pos).isSideSolid(world, pos, side);
	}
	
	/**
	 * Used as an override for SRM blocks. Used to run {@link Block#addCollisionBoxToList(IBlockState, World, BlockPos, AxisAlignedBB, List, Entity, boolean)} on the mirrored state
	 * @param state The current BlockState
	 * @param worldIn The current World
	 * @param pos The current BlockPos
	 * @param entityBox The colliding Entities bounding box
	 * @param collidingBoxes The list of bounding boxes
	 * @param entityIn The Entity Colliding with the block
	 * @param isActualState 
	 */
	default void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) 
	{
		if(worldIn.getTileEntity(pos) instanceof ISecretTileEntity && ISecretTileEntity.getMirrorState(worldIn, pos) != null)
			ISecretTileEntity.getMirrorState(worldIn, pos).addCollisionBoxToList(worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
		else
			Blocks.STONE.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
	}
	
	/**
	 * Used as an override for SRM blocks. Used to run {@link Block#getSoundType(IBlockState, World, BlockPos, Entity)} on the mirrored state
     * @param state The state
     * @param world The world
     * @param pos The position. Note that the world may not necessarily have {@code state} here!
     * @param entity The entity that is breaking/stepping on/placing/hitting/falling on this block, or null if no entity is in this context
     * @return A SoundType to use
     */
	default SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return world.getTileEntity(pos) instanceof ISecretTileEntity && ISecretTileEntity.getMirrorState(world, pos) != null ? 
				ISecretTileEntity.getMirrorState(world, pos).getBlock().getSoundType() : SoundType.STONE;
	}
	
	/**
	 * Used to override the vanilla hit effects of a block. Called from SRM blocks ({@link Block#addHitEffects(IBlockState, World, RayTraceResult, ParticleManager)})
	 * @param state The current BlockState
	 * @param worldObj The current world
	 * @param target The rayTrace used to get the Hit Effects
	 * @param manager The ParticleManager in use
	 * @return true if the blocks position has an SRM block, meaning that the hit effects are overridden
	 */
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
	
	/**
	 * Used to override the vanilla destroy effects of a block. Called from SRM blocks ({@link Block#addDestroyEffects(World, BlockPos, ParticleManager)})
	 * @param world The current world 
	 * @param pos The current BlockPos
	 * @param manager The particle manager in used
	 * @return true if the blocks position has an SRM block, meaning that the destroy effects are overridden
	 */
	@SideOnly(Side.CLIENT)
	default boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) 
	{
		if(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos) != null)
		{
			IBlockState state = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos);
			try
			{
				state = state.getActualState(world, pos);
			}
			catch (Exception e) 
			{
				;
			}
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
	
	/**
	 * Called from SRM blocks ({@link Block#getActualState(IBlockState, IBlockAccess, BlockPos)})
	 * Used to remove the {@link IUnlistedProperty} from the blockstate ({@link ISecretBlock#RENDER_PROPERTY})
	 * @param state The current blockstate. Can be null, not used
	 * @param worldIn The current world. Can be null, not used
	 * @param pos The current BlockPos. Can be null, not used
	 * @param superState The state used from calling {@code super.getActualState(IBlockState, IBlockAccess, BlockPos)} in SRM block classes
	 * @return the IBlockState, with its {@link ISecretBlock#RENDER_PROPERTY} stripped from it
	 */
	default public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos, IBlockState superState)
	{
		if(superState instanceof IExtendedBlockState)
			return ((IExtendedBlockState)superState).withProperty(RENDER_PROPERTY, null);//Make sure the unlisted property is removed for 
		return superState;
	}
	
	/**
	 * Used as an override to SRM blocks. Used to run {@link Block#canRenderInLayer(IBlockState, BlockRenderLayer)} on the mirrored state
	 * @param state The current IBlockState
	 * @param layer the layer used
	 * @return if the block can render in the mirrored state, or if the layer is {@link BlockRenderLayer#SOLID}, if it does not exist
	 */
	@SideOnly(Side.CLIENT)
	default public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
	{
		if(state instanceof IExtendedBlockState)
		{
			IBlockState renderState = ((IExtendedBlockState)state).getValue(RENDER_PROPERTY);
			if(renderState != null)
				return renderState.getBlock().getBlockLayer() == layer;
		}
    	return layer == BlockRenderLayer.SOLID;
	}
	
	/**
	 * Used to get the extended state from a SRM block. Used to put {@link RenderStateUnlistedProperty} into the block.
	 * <br><br>
	 * Also used to set {@link SecretBlockModel#AO} to if the models blockstates model is ambient occlusion
	 * @param state The current state
	 * @param world The current world 
	 * @param pos the current position
	 * @return {@code state}, or the IBlockState with {@link ISecretBlock#RENDER_PROPERTY} set to the mirror state
	 */
	default public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) 
	{
		
		if(state instanceof IExtendedBlockState && world.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() !=  null)
		{
			IBlockState renderState = ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState();
			try
			{
				renderState = renderState.getActualState(new FakeBlockAccess(world), pos);
			}
			catch (Exception e) 
			{
				;
			}
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
			if(model instanceof SecretBlockModel)
				((SecretBlockModel)model).AO.set(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(renderState).isAmbientOcclusion());
			state = ((IExtendedBlockState)state).withProperty(RENDER_PROPERTY, renderState);
		}
		return state;
	}
}
