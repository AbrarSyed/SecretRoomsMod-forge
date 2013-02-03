package com.github.AbrarSyed.SecretRooms.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import com.github.AbrarSyed.SecretRooms.common.SecretRooms;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamo;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamoFull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerClient implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player useless)
	{
		String channel = packet.channel;
		byte[] data = packet.data;

		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
		World world = player.worldObj;

		if (channel.equals("SRM-TE-Camo"))
		{
			if (data.length <= 0)
				return;

			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));
			int coords[] = new int[3];
			int texture = -1;
			boolean forged = false;
			String texturePath = null;
			try
			{
				for (int i = 0; i < 3; i++)
					coords[i] = dataStream.readInt();

				texture = dataStream.readInt();
				forged = dataStream.readBoolean();

				if (forged)
				{
					int texturePathLength = dataStream.readInt();

					char[] string = new char[texturePathLength];

					for (int i = 0; i < texturePathLength; i++)
						string[i] = dataStream.readChar();

					texturePath = new String(string);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			// System.out.println("CLIENT-RECIEVED: "+texture);

			if (texture == -1)
				// System.out.println(player.username+"-SET: *invalid*");
				return;

			TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(coords[0], coords[1], coords[2]);

			if (entity == null)
			{

				if (!world.blockExists(coords[0], coords[1], coords[2]))
					// System.out.println(player.username+"-SET: *null-block*");
					return;

				entity = new TileEntityCamo();
				entity.xCoord = coords[0];
				entity.yCoord = coords[1];
				entity.zCoord = coords[2];
				world.addTileEntity(entity);

				// System.out.println(player.username+"-SET: *null-entity*");
				return;
			}

			entity.setTexture(texture);

			if (forged)
				entity.setTexturePath(texturePath);

			world.markBlockForRenderUpdate(coords[0], coords[1], coords[2]);
			// System.out.println(player.username+"-SET: "+texture);
		}

		else if (channel.equals("SRM-TE-CamoFull"))
		{
			if (data.length <= 0)
				return;

			DataInputStream dataStream = new DataInputStream(new ByteArrayInputStream(data));
			int coords[] = new int[3];
			int copyCoords[] = null;
			int texture = -1;
			boolean hasCoords = false;
			try
			{
				for (int i = 0; i < 3; i++)
					coords[i] = dataStream.readInt();

				texture = dataStream.readInt();
				hasCoords = dataStream.readBoolean();

				if (hasCoords)
				{
					copyCoords = new int[3];
					for (int i = 0; i < 3; i++)
						copyCoords[i] = dataStream.readInt();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			if (texture == -1)
				return;

			TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(coords[0], coords[1], coords[2]);

			if (entity == null)
				return;

			entity.setCopyID(texture);

			if (hasCoords)
			{
				entity.setCopyCoordX(copyCoords[0]);
				entity.setCopyCoordY(copyCoords[1]);
				entity.setCopyCoordZ(copyCoords[2]);
			}

			world.markBlockForRenderUpdate(coords[0], coords[1], coords[2]);
		}
		else if (channel.equals("SRM-Display"))
		{
			SecretRooms.displayCamo = !SecretRooms.displayCamo;

			if (SecretRooms.displayCamo)
				player.addChatMessage(getColorThing() + "Camo blocks made secret");
			else
				player.addChatMessage(getColorThing() + "Camo blocks made obvious");

			int rad = 10; // update radius
			world.markBlockRangeForRenderUpdate((int) player.posX - rad, (int) player.posY - rad, (int) player.posZ - rad, (int) player.posX + rad, (int) player.posY + rad, (int) player.posZ + rad);
		}
	}

	public static String getColorThing()
	{
		// System.out.print("");
		return "\u00a7e";
	}

}
