package com.wynprice.secretroomsmod.base.interfaces;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * An interface for all TileEntities for SRM blocks in implement
 * Extends {@link ITickable} as all SRM blocks have TileEntities. 
 * <br>Not needed but means,
 * <br>{@code public class SRMBlock implements ISecretTileEntity, ITickable}
 * <br>becomes:
 * <br>{@code public class SRMBlock implements ISecretTileEntity}
 * @author Wyn Price
 *
 */
public interface ISecretTileEntity extends ITickable
{
	/**
	 * Used as a map to hold info for when the TileEntity isn't even in place. 
	 */
	static final HashMap<Integer, HashMap<BlockPos, IBlockState>> RENDER_MAP = new HashMap<>();
		
	/**
	 * Used to get the Correct HashMap for the World
	 * @param world the current world
	 */
	public static HashMap<BlockPos, IBlockState> getMap(World world)
	{
		if(!RENDER_MAP.containsKey(world.provider.getDimension()))
			RENDER_MAP.put(world.provider.getDimension(), new HashMap<>());
		return RENDER_MAP.get(world.provider.getDimension());
	}
	
	/**
	 * Used to set the mirrored state to the block. Can be used only once. Will be locked after one use
	 * @param mirrorState The state to set the block to
	 */
	public void setMirrorState(IBlockState mirrorState);
	
	/**
	 * Forces the mirror state to a block. Can be used as many times as wanted
	 * @param mirrorState The state to set the SRM to
	 */
	public void setMirrorStateForcable(IBlockState mirrorState);
	
	/**
	 * Gets the mirrored state from the SRM tile entity, in a NoNull way
	 */
	@Nonnull
	default public IBlockState getMirrorStateSafely() {
		IBlockState mirroredState = getMirrorState();
		return mirroredState == null ? Blocks.STONE.getDefaultState() : mirroredState;
	}
	
	/**
	 * Gets the mirred state from the SRM tileEntity. Can be null if somthing went wrong
	 */
	@Nullable
	public IBlockState getMirrorState();
	/**
	 * 
	 * Used to get the mirrored state when there is no instance
	 * @param world The current world
	 * @param pos The current position
	 * @return the Mirrored State, or {@link Blocks#STONE} if there is none.
	 */
	public static IBlockState getMirrorState(World world, BlockPos pos)
	{		
		IBlockState blockstate = getMap(world).get(pos) == null && world.getTileEntity(pos) instanceof ISecretTileEntity ? ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorStateSafely() : getMap(world).get(pos);
		return blockstate == null ? Blocks.STONE.getDefaultState() : blockstate;
	}
	
	/**
	 * Used to get the mirrored state when there is no instance, and all you have is the IBlockAccess
	 * @param access The current world
	 * @param pos The current position
	 * @return the current world, or {@link Blocks#STONE} if there is none
	 */
	public static IBlockState getMirrorState(IBlockAccess access, BlockPos pos)
	{
		IBlockState returnState = Blocks.AIR.getDefaultState();
		if(access.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)access.getTileEntity(pos)).getMirrorStateSafely() != null) {
			return ((ISecretTileEntity)access.getTileEntity(pos)).getMirrorStateSafely();
		} else if(access instanceof World) {
			returnState = getMirrorState((World)access, pos);
		} else if(access.getTileEntity(pos) != null && access.getTileEntity(pos).getWorld() != null) {
			returnState = getMirrorState(access.getTileEntity(pos).getWorld(), pos);
		} else { //Last resort
			for(int dim : RENDER_MAP.keySet()) {
				if(FMLCommonHandler.instance().getMinecraftServerInstance() == null) {
					returnState = getMirrorState(SecretRooms5.proxy.getPlayer().world, pos);
				} else if(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim) == access) {
					returnState = getMirrorState(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim), pos);
				}
			}
		}
		return returnState == null ? Blocks.STONE.getDefaultState() : returnState;
		
	}
	
	/**
	 * Load from NBT. TileEntites have this by default, but this is used so its callable from an instance of {@link ISecretTileEntity}
	 * @param compound The compound to load from
	 */
	public void loadFromNBT(NBTTagCompound compound);
	
	class TileEntityData {
		private IBlockState mirroredState;
		private boolean locked;
		
		public IBlockState getMirroredState() {
			return mirroredState;
		}
		
		public TileEntityData setMirroredState(IBlockState mirroredState) {
			this.mirroredState = mirroredState;
			return this;
		}
		
		public boolean isLocked() {
			return locked;
		}
		
		public TileEntityData setLocked(boolean locked) {
			this.locked = locked;
			return this;
		}
	}
	
	/**
	 * Default readFromNBT performed on tile entities
	 * @param compound
	 * @param tileData
	 * @return
	 */
	default public TileEntityData readDataFromNBT(NBTTagCompound compound) {
		IBlockState mirrorState = null;
		boolean locked;
		if(!compound.hasKey("SecretRoomsMod", 10)) { //Old method
			locked = compound.getBoolean("locked");
			Block testBlock = Block.REGISTRY.getObject(new ResourceLocation(compound.getString("MirrorBlock")));
			if(testBlock != Blocks.AIR) {
				mirrorState = testBlock.getStateFromMeta(compound.getInteger("MirrorMeta"));
			}
			if(mirrorState != null && mirrorState.getBlock() instanceof ISecretBlock)
				mirrorState = null;
		} else {
			NBTTagCompound nbt = compound.getCompoundTag("SecretRoomsMod");
			int v = nbt.getInteger("DataVersion");
			if(v == 1) {
				locked = nbt.getBoolean("locked");
				mirrorState = NBTUtil.readBlockState(nbt.getCompoundTag("MirroredState"));
			} else {
				throw new IllegalArgumentException("Secret Tile Entites Dont know how to handle data version " + v);
			}
		}
	
		if(mirrorState == null) {
			new NullPointerException("NBT Compound returned a null state. NBTTAG: " + compound.toString()).printStackTrace(new PrintStream(new OutputStream() {
				
				@Override
				public void write(int b) throws IOException {
					System.out.print((char)b);
				}
			}));
			mirrorState = Blocks.STONE.getDefaultState();
		}
		
		if(!ISecretBlock.ALL_SECRET_TILE_ENTITIES.contains(this)) {
			ISecretBlock.ALL_SECRET_TILE_ENTITIES.add((TileEntity) this);
		}
		
		return new TileEntityData().setMirroredState(mirrorState).setLocked(locked);
	}
	
	
	default public void writeDataToNBT(NBTTagCompound compound, TileEntityData data) {
		NBTTagCompound nbt = new NBTTagCompound();
		if(data.getMirroredState() == null) {
			SecretRooms5.LOGGER.error("Caught null mirrored state in saving file at {}. Saving state as Stone", ((TileEntity)this).getPos());
			data.setMirroredState(Blocks.STONE.getDefaultState());
		}
//		getTileData().setString("MirrorBlock", getMirrorState().getBlock().getRegistryName().toString());
//		getTileData().setInteger("MirrorMeta", getMirrorState().getBlock().getMetaFromState(getMirrorState()));
		nbt.setTag("MirroredState", NBTUtil.writeBlockState(new NBTTagCompound(), data.getMirroredState()));	
		nbt.setInteger("DataVersion", 1);
		nbt.setBoolean("locked", data.isLocked());
		compound.setTag("SecretRoomsMod", nbt);
	}


}
