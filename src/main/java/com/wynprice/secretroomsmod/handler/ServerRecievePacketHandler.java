package com.wynprice.secretroomsmod.handler;

import java.util.HashMap;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ServerRecievePacketHandler 
{
	public final static HashMap<BlockPos, ObjectInfo> UPDATE_MAP = new HashMap<>();
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		HashMap<BlockPos, ObjectInfo> newMap = new HashMap<>();
		if(!event.world.isRemote)
			for(BlockPos pos : UPDATE_MAP.keySet())
				if(event.world.getTileEntity(pos) instanceof ISecretTileEntity)
				{
					((ISecretTileEntity)event.world.getTileEntity(pos)).setMirrorState(UPDATE_MAP.get(pos).state, UPDATE_MAP.get(pos).pos);
					((ISecretBlock)event.world.getBlockState(pos).getBlock()).onMessageRecieved(event.world, pos);
				}
				else
					newMap.put(pos, UPDATE_MAP.get(pos));
		UPDATE_MAP.clear();
		UPDATE_MAP.putAll(newMap);
	}
	
	public static class ObjectInfo
	{
		public final IBlockState state;
		public final BlockPos pos;
		
		public ObjectInfo(IBlockState state, BlockPos pos) 
		{
			this.state = state;
			this.pos = pos;
		}
		
		@Override
		public String toString() {
			return String.valueOf(pos) + "@"+  String.valueOf(state);
		}
	}
}
