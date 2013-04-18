package mods.SecretRoomsMod.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import mods.SecretRoomsMod.SecretRooms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

/**
 * @author AbrarSyed
 */
public class PacketHandlerServer implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player useless)
	{
		String channel = packet.channel;
		byte[] data = packet.data;

		EntityPlayer player = (EntityPlayer) useless;
		World world = player.worldObj;

		if (channel.equals("SRM-KeyEvents"))
		{
			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));
			String username = null;
			try
			{
				username = dataStream.readUTF();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (username != null)
			{
				SecretRooms.proxy.onKeyPress(username);
			}
		}
	}

}
