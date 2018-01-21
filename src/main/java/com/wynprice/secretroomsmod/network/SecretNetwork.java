package com.wynprice.secretroomsmod.network;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.network.packets.MessagePacketEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketRemoveEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketSwingArm;
import com.wynprice.secretroomsmod.network.packets.MessagePacketSyncEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketToggleGlassDirection;
import com.wynprice.secretroomsmod.network.packets.MessagePacketUpdateClient;
import com.wynprice.secretroomsmod.network.packets.MessagePacketUpdateProbe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class SecretNetwork 
{
	private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SecretRooms5.MODID);

	
	public static void preInit()
	{
		registerMessage(MessagePacketUpdateClient.class, Side.CLIENT);
		registerMessage(MessagePacketToggleGlassDirection.class, Side.SERVER);
		registerMessage(MessagePacketUpdateProbe.class, Side.SERVER);
		registerMessage(MessagePacketEnergizedPaste.class, Side.SERVER);
		registerMessage(MessagePacketSyncEnergizedPaste.class, Side.CLIENT);
		registerMessage(MessagePacketRemoveEnergizedPaste.class, Side.SERVER);
		registerMessage(MessagePacketSwingArm.class, Side.CLIENT);
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
