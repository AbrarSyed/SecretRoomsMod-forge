package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.SecretRooms5;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class BaseMessagePacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ>
{
	@Override
	public REQ onMessage(REQ message, MessageContext ctx) {
		onReceived(message, ctx.side == Side.SERVER ? ctx.getServerHandler().player : SecretRooms5.proxy.getPlayer());
		return null;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {};
	
	@Override
	public void fromBytes(ByteBuf buf) {};
	
	
	public abstract void onReceived(REQ message, EntityPlayer player);

}