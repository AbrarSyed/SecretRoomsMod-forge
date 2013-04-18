package mods.SecretRoomsMod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class HandlerServer extends HandlerBase
{

	@Override
	protected void doAction(EntityPlayer player, PacketSRMBase packet)
	{
		World world = player.worldObj;
		packet.actionServer(world, player);
	}

}
