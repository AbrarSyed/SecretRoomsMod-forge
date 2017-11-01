package com.wynprice.secretroomsmod.network;

import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.network.packets.MessagePacketFakeBlockPlaced;
import com.wynprice.secretroomsmod.network.packets.MessagePacketUpdateClient;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SecretNetwork 
{
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SecretRooms2.MODID);

	
	public static void preInit()
	{
		registerMessage(MessagePacketFakeBlockPlaced.class, Side.SERVER);
		registerMessage(MessagePacketUpdateClient.class, Side.CLIENT);
	}
	
	private static int idCount = -1;
    public static void registerMessage(Class claz, Side recievingSide)
    {
    	INSTANCE.registerMessage(claz, claz, idCount++, recievingSide);
    }
    
	public static void sendToServer(IMessage message)
	{
		INSTANCE.sendToServer(message);
	}
	
	public static void sendToPlayer(EntityPlayer player, IMessage message)
	{
		if(!player.world.isRemote)
			INSTANCE.sendTo(message, (EntityPlayerMP) player);
	}
	
	public static void sendToPlayersInWorld(World world, IMessage message)
	{
		if(world == null)
			sendToAll(message);
		else if(!world.isRemote)
			INSTANCE.sendToDimension(message, world.provider.getDimension());
	}
	
	public static void sendToAll(IMessage message)
	{
		INSTANCE.sendToAll(message);
	}	
}
