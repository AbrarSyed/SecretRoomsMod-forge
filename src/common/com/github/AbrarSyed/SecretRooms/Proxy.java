package com.github.AbrarSyed.SecretRooms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;

public class Proxy
{
	public void doRenderStuff()
	{
		
	}
	
	public void doKeyStuff()
	{
		
	}
	
	public void doOneWayStuff(World world, int i, int j, int k, EntityLiving entityliving)
	{
	}
	
	public Configuration getConfig()
	{
		return new Configuration(new File("./SecretRoomsMod.prop"));
	}
}
