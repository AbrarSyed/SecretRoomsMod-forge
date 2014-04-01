package com.github.AbrarSyed.secretroomsmod.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSRM2Key extends PacketSRMBase
{

	public PacketSRM2Key()
	{
		// nothing
	}

	public PacketSRM2Key(ObjectInputStream stream) throws IOException
	{
		// nothing
	}

	@Override
	public void writeToStream(ObjectOutputStream stream) throws IOException
	{
		// nothing
	}

	@Override
	public int getID()
	{
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player)
	{
		// nothing
	}

	@Override
	public void actionServer(World world, EntityPlayer player)
	{
		if (FMLCommonHandler.instance().getSide().isServer())
			SecretRooms.proxy.onKeyPress(player.getUniqueID());
	}

}
