package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.SecretRooms5;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * The base packet for SRM
 * @author Wyn Price
 *
 * @param <REQ> use the class this is. Used to get the right instance of the packet, so you don't have to do casting
 */
public abstract class BaseMessagePacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ>
{
	@Override
	public REQ onMessage(REQ message, MessageContext ctx) {
		onReceived(message, ctx.side == Side.SERVER ? ctx.getServerHandler().player : SecretRooms5.proxy.getPlayer());
		return null;
	}
	
	/**
	 * Convert the packet data to a {@link ByteBuf}
	 */
	@Override
	public void toBytes(ByteBuf buf) {};
	
	/**
	 * Get the packet data from the {@link ByteBuf} 
	 */
	@Override
	public void fromBytes(ByteBuf buf) {};
	
	/**
	 * Called when the message is received
	 * @param message the message
	 * @param player the player sending the message
	 */
	public abstract void onReceived(REQ message, EntityPlayer player);

}