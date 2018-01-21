package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class MessagePacketSwingArm extends BaseMessagePacket<MessagePacketSwingArm>
{
	
	public MessagePacketSwingArm() {
	}
	
	private EnumHand hand;
	
	public MessagePacketSwingArm(EnumHand hand) 
	{
		this.hand = hand;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.hand == EnumHand.MAIN_HAND ? 0 : 1);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.hand = buf.readInt() == 0? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
	}
	
	
	@Override
	public void onReceived(MessagePacketSwingArm message, EntityPlayer player) 
	{
		player.swingArm(message.hand);
	}

}
