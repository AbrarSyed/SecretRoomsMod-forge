package com.github.AbrarSyed.SecretRooms;

import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Packet250CustomPayload;

/**
 * @author AbrarSyed
 */
public class CommandShowSecretBlocks extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "srm-show";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{		
		Packet250CustomPayload packet = new Packet250CustomPayload();
		
		packet.data = new byte[] {};
		packet.channel = "SRM-Display";
		packet.length = 0;
		
		PacketDispatcher.sendPacketToPlayer(packet, (Player) CommandBase.getCommandSenderAsPlayer(var1));
	}
}
