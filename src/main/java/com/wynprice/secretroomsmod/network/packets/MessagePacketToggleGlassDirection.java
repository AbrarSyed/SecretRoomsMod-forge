package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;

import net.minecraft.entity.player.EntityPlayer;

public class MessagePacketToggleGlassDirection extends BaseMessagePacket<MessagePacketToggleGlassDirection>
{

	@Override
	public void onReceived(MessagePacketToggleGlassDirection message, EntityPlayer player) 
	{
		player.getEntityData().setBoolean("glassDirection", !player.getEntityData().getBoolean("glassDirection"));
	}

}
