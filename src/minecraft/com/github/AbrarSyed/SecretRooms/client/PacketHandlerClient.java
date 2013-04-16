package com.github.AbrarSyed.SecretRooms.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.github.AbrarSyed.SecretRooms.common.BlockHolder;
import com.github.AbrarSyed.SecretRooms.common.SecretRooms;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamo;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamoFull;

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

		if (world == null)
			return;

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
				{
					coords[i] = dataStream.readInt();
				}

				texture = dataStream.readInt();
				forged = dataStream.readBoolean();

				if (forged)
				{
					int texturePathLength = dataStream.readInt();

					char[] string = new char[texturePathLength];

					for (int i = 0; i < texturePathLength; i++)
					{
						string[i] = dataStream.readChar();
					}

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
					return;

				entity = new TileEntityCamo();
				entity.xCoord = coords[0];
				entity.yCoord = coords[1];
				entity.zCoord = coords[2];
				entity.worldObj = world;
				entity.validate();

				return;
			}

			entity.setTexture(texture);

			if (forged)
			{
				entity.setTexturePath(texturePath);
			}

			world.markBlockForRenderUpdate(coords[0], coords[1], coords[2]);
		}

		else if (channel.equals("SRM-TE-CamoFull"))
		{
			if (data.length <= 0)
				return;

			int coords[] = new int[3];
			BlockHolder holder = null;
			try
			{
				ObjectInputStream dataStream = new ObjectInputStream(new ByteArrayInputStream(data));

				for (int i = 0; i < 3; i++)
				{
					coords[i] = dataStream.readInt();
				}

				boolean hasHolder = dataStream.readBoolean();

				if (hasHolder)
				{
					NBTTagCompound nbt = (NBTTagCompound) NBTBase.readNamedTag(dataStream);
					holder = BlockHolder.buildFromNBT(nbt);
				}

				dataStream.close();

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(coords[0], coords[1], coords[2]);

			if (entity == null)
				return;

			entity.setBlockHolder(holder);

			world.markBlockForRenderUpdate(coords[0], coords[1], coords[2]);
		}
		else if (channel.equals("SRM-Display"))
		{
			SecretRooms.displayCamo = !SecretRooms.displayCamo;

			if (SecretRooms.displayCamo)
			{
				player.addChatMessage(getColorThing() + "Camo blocks made secret");
			}
			else
			{
				player.addChatMessage(getColorThing() + "Camo blocks made obvious");
			}

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
