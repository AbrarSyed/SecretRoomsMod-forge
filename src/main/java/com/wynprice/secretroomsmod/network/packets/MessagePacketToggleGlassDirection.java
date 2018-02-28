package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Used to update the direction of glass. Saves to the players NBT so therefore, it keeps the same between sessions
 * @author Wyn Price
 *
 */
public class MessagePacketToggleGlassDirection extends BaseMessagePacket<MessagePacketToggleGlassDirection>
{

	@Override
	public void onReceived(MessagePacketToggleGlassDirection message, EntityPlayer player) 
	{
		player.getEntityData().setBoolean("glassDirection", !player.getEntityData().getBoolean("glassDirection"));
	}

}
