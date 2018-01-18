package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketSyncEnergizedPaste extends BaseMessagePacket<MessagePacketSyncEnergizedPaste>
{
	
	public MessagePacketSyncEnergizedPaste() {
	}
	
	private IBlockState state;
	private int dim;
	private boolean set;
	private BlockPos pos;
	private NBTTagCompound nbt;
	
	public MessagePacketSyncEnergizedPaste(NBTTagCompound nbt, BlockPos pos) 
	{
		this.nbt = nbt;
		this.pos = pos == null ? BlockPos.ORIGIN : pos;
	}
	
	public MessagePacketSyncEnergizedPaste(int dim, BlockPos pos, IBlockState state, boolean set) 
	{
		this.dim = dim;
		this.state = state == null? Blocks.STONE.getDefaultState() : state;
		this.pos = pos;
		this.set = set;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		int type = buf.readInt();
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		if(type == 0)
		{
			dim = buf.readInt();
			state = Block.getStateById(buf.readInt());
			set = buf.readBoolean();
		}
		else
			nbt = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(nbt == null ? 0 : 1);
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		if(nbt == null)
		{
			buf.writeInt(dim);
			buf.writeInt(Block.getStateId(state));
			buf.writeBoolean(set);
		}
		else
			ByteBufUtils.writeTag(buf, nbt);
	}

	@Override
	public void onReceived(MessagePacketSyncEnergizedPaste message, EntityPlayer player) 
	{
		if(message.nbt != null)
			EnergizedPasteHandler.readFromNBT(message.nbt);
		else
		{
			if(!message.pos.equals(BlockPos.ORIGIN))
				if(message.set)
					EnergizedPasteHandler.putState(message.dim, message.pos, message.state, player.world.getBlockState(message.pos));
				else
					EnergizedPasteHandler.removeReplacedState(message.dim, message.pos);
		}
		if(!message.pos.equals(BlockPos.ORIGIN))
			player.world.markBlockRangeForRenderUpdate(message.pos.add(-1, -1, -1), message.pos.add(1, 1, 1));
		else
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				
				@Override
				public void run() {
					Minecraft.getMinecraft().renderGlobal.loadRenderers();
				}
			});
	}

}
