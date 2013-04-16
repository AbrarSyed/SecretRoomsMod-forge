package com.github.AbrarSyed.SecretRooms.client;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import com.github.AbrarSyed.SecretRooms.common.SecretRooms;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class SecretKey extends KeyHandler
{
	public SecretKey(KeyBinding keyBinding)
	{
		super(new KeyBinding[] { keyBinding }, new boolean[] { false });
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel()
	{
		return "SecretRoomsFacing-key";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
	{
		// do nothing
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		if (!tickEnd)
			return;

		EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
		
		if(player == null)
			return;

		SecretRooms.proxy.onKeyPress(player.username);

		if (SecretRooms.proxy.getFaceAway(player.username))
		{
			chat(PacketHandlerClient.getColorThing() + "-- !!! OneWayBlock facing set to Towards !!! --");
			System.out.println("chat true");
		}
		else
		{
			chat(PacketHandlerClient.getColorThing() + "-- !!! OneWayBlock facing set to Away !!! --");
			System.out.println("chat false");
		}
	}

	public static void chat(String s)
	{
		FMLClientHandler.instance().getClient().thePlayer.addChatMessage(s);
	}
}
