package mods.SecretRoomsMod.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class HandlerServer extends HandlerBase
{

	@Override
	protected void doAction(EntityPlayerMP player, PacketSCBase packet)
	{
		World world = player.worldObj;
		packet.actionServer(world, player);
	}

}
