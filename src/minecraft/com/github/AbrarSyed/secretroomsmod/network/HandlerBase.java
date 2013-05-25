package com.github.AbrarSyed.secretroomsmod.network;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.logging.Level;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import net.minecraft.entity.player.EntityPlayer;
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
		if (!packet.channel.equals(PacketSRMBase.CHANNEL))
			return;

		try
		{
			ByteArrayInputStream array = new ByteArrayInputStream(packet.data);
			ObjectInputStream stream = new ObjectInputStream(array);
			int id = stream.readInt();

			PacketSRMBase parsedPacket = null;

			switch (id)
				{
					case 0:
						parsedPacket = new PacketSRM0UpdateCamo(stream);
						break;
					case 1:
						parsedPacket = new PacketSRM1ToggleShow(stream);
						break;
					case 2:
						parsedPacket = new PacketSRM2Key(stream);
						break;
				}

			doAction((EntityPlayer) player, parsedPacket);
			stream.close();
			array.close();
		}
		catch (Throwable t)
		{
			SecretRooms.logger.log(Level.SEVERE, "Error receiving SecretRoomsMod packet! " + toString(), t);
		}

	}

	protected abstract void doAction(EntityPlayer player, PacketSRMBase packet);
}
