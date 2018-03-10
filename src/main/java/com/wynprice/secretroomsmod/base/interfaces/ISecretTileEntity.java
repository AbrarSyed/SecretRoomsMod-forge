package com.wynprice.secretroomsmod.base.interfaces;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
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
	 * Gets the mirrored state from the SRM tile entity
	 */
	public IBlockState getMirrorState();
	
	/**
	 * Used to get the mirrored state when there is no instance
	 * @param world The current world
	 * @param pos The current position
	 * @return the Mirrored State, or {@link Blocks#STONE} if there is none.
	 */
	public static IBlockState getMirrorState(World world, BlockPos pos)
	{		
		IBlockState blockstate = getMap(world).get(pos) == null && world.getTileEntity(pos) instanceof ISecretTileEntity ? ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() : getMap(world).get(pos);
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
		if(access.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)access.getTileEntity(pos)).getMirrorState() != null) {
			return ((ISecretTileEntity)access.getTileEntity(pos)).getMirrorState();
		} else if(access instanceof World) {
			returnState = getMirrorState((World)access, pos);
		} else {
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

}
