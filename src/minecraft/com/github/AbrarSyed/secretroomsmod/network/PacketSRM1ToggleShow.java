package com.github.AbrarSyed.secretroomsmod.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSRM1ToggleShow extends PacketSRMBase
{

	public PacketSRM1ToggleShow()
	{
	}

	public PacketSRM1ToggleShow(ObjectInputStream stream) throws IOException
	{
	}

	@Override
	public void writeToStream(ObjectOutputStream stream) throws IOException
	{
	}

	@Override
	public int getID()
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player)
	{
		SecretRooms.displayCamo = !SecretRooms.displayCamo;

		if (SecretRooms.displayCamo)
		{
			player.addChatMessage(EnumChatFormatting.YELLOW + "Camo blocks made secret");
		}
		else
		{
			player.addChatMessage(EnumChatFormatting.YELLOW + "Camo blocks made obvious");
		}

		int rad = 20; // update radius
		world.markBlockRangeForRenderUpdate((int) player.posX - rad, (int) player.posY - rad, (int) player.posZ - rad, (int) player.posX + rad, (int) player.posY + rad, (int) player.posZ + rad);
	}

	@Override
	public void actionServer(World world, EntityPlayer player)
	{
		// nothing.
	}

}
