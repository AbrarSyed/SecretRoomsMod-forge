package mods.SecretRoomsMod.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

/**
 * @author AbrarSyed
 */
public class CommandShow extends CommandBase
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
