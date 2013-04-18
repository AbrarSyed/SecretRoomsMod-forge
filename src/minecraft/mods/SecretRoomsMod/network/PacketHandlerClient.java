package mods.SecretRoomsMod.network;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import mods.SecretRoomsMod.SecretRooms;
import mods.SecretRoomsMod.common.BlockHolder;
import mods.SecretRoomsMod.common.TileEntityCamoFull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
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

		if (channel.equals("SRM-TE-CamoFull"))
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

				NBTTagCompound nbt = (NBTTagCompound) NBTBase.readNamedTag(dataStream);
				holder = BlockHolder.buildFromNBT(nbt);

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
