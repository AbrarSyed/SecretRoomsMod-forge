package mods.SecretRoomsMod.common;

import java.util.ArrayList;
import java.util.List;

import mods.SecretRoomsMod.network.PacketSRM1ToggleShow;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public List getCommandAliases()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.add("show");
		return list;
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2)
	{
		PacketDispatcher.sendPacketToPlayer(new PacketSRM1ToggleShow().getPacket250(), (Player) CommandBase.getCommandSenderAsPlayer(var1));
	}
}
