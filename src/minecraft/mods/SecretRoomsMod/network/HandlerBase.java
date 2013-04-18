package mods.SecretRoomsMod.network;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;

import mods.SecretRoomsMod.SecretRooms;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public abstract class HandlerBase implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		// no packet stuff
		if (!packet.channel.equals(PacketSCBase.CHANNEL))
			return;

		try
		{
			ByteArrayInputStream array = new ByteArrayInputStream(packet.data);
			ObjectInputStream stream = new ObjectInputStream(array);
			int id = stream.readInt();

			PacketSCBase parsedPacket = null;

			switch (id)
				{
					case 0:
						parsedPacket = new PacketSC0MountEntity(stream);
						break;
					case 1:
						parsedPacket = new PacketSC1StartBoat(stream);
						break;
					case 2:
						parsedPacket = new PacketSC2BuildBoat(stream);
						break;
					case 3:
						parsedPacket = new PacketSC3ToggleAnchor(stream);
						break;
				}

			doAction((EntityPlayerMP) player, parsedPacket);
			stream.close();
			array.close();
		}
		catch (Throwable t)
		{
			SecretRooms.logger.log(Level.SEVERE, "Error receiving SeaCraft packet! " + this.toString(), t);
		}

	}

	protected abstract void doAction(EntityPlayerMP player, PacketSCBase packet);
}
