package com.wynprice.secretroomsmod.base.interfaces;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public interface ISecretTileEntity extends ITickable
{
	static final HashMap<Integer, HashMap<BlockPos, IBlockState>> RENDER_MAP = new HashMap<>();
		
	public static HashMap<BlockPos, IBlockState> getMap(World world)
	{
		if(!RENDER_MAP.containsKey(world.provider.getDimension()))
			RENDER_MAP.put(world.provider.getDimension(), new HashMap<>());
		return RENDER_MAP.get(world.provider.getDimension());
	}
	
	public void setMirrorState(IBlockState mirrorState, @Nullable BlockPos pos);
	
	public void setMirrorStateForcable(IBlockState mirrorState, @Nullable BlockPos pos);
	
	public IBlockState getMirrorState();
	
	public static IBlockState getMirrorState(World world, BlockPos pos)
	{		
		return getMap(world).get(pos) == null && world.getTileEntity(pos) instanceof ISecretTileEntity ? ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() : getMap(world).get(pos);
	}
	
	public static IBlockState getMirrorState(IBlockAccess access, BlockPos pos)
	{
		IBlockState returnState = Blocks.AIR.getDefaultState();
		if(access instanceof World)
			returnState = getMirrorState((World)access, pos);
		else
			for(int dim : RENDER_MAP.keySet())
				if(FMLCommonHandler.instance().getMinecraftServerInstance() == null)
					returnState = getMirrorState(SecretRooms5.proxy.getPlayer().world, pos);
				else if(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim) == access)
					returnState = getMirrorState(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim), pos);
		return returnState == null ? Blocks.STONE.getDefaultState() : returnState;
	}
	
	public void loadFromNBT(NBTTagCompound compound);

}
