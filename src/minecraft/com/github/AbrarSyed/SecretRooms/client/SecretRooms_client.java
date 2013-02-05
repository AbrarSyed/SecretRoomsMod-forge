package com.github.AbrarSyed.SecretRooms.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import org.lwjgl.input.Keyboard;

import com.github.AbrarSyed.SecretRooms.common.BlockOneWay;
import com.github.AbrarSyed.SecretRooms.common.FakeWorld;
import com.github.AbrarSyed.SecretRooms.common.Proxy;
import com.github.AbrarSyed.SecretRooms.common.SecretRooms;
import com.github.AbrarSyed.SecretRooms.common.TileEntityCamo;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class SecretRooms_client extends Proxy
{
	public static KeyBinding	key_OneWayFace;
	private FakeWorld			fake;
	private boolean				oneWayFaceAway	= true;

	public SecretRooms_client()
	{
		key_OneWayFace = new KeyBinding("Change OneWayBlock face", Keyboard.KEY_BACKSLASH);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void loadRenderStuff()
	{
		SecretRooms.camoRenderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CamoRenderer());

		SecretRooms.torchRenderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new TorchRenderer());

		MinecraftForgeClient.preloadTexture(SecretRooms.textureFile);
	}

	@Override
	public void loadKeyStuff()
	{
		KeyBindingRegistry.registerKeyBinding(new SecretKey(key_OneWayFace));
	}

	@Override
	public void handleOneWayPlace(World world, int i, int j, int k, EntityLiving entityliving)
	{
		int metadata = world.getBlockMetadata(i, j, k);
		Object[] properties = ((BlockOneWay) SecretRooms.oneWay).getOtherProperties(world, i, j, k, metadata);
		TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(i, j, k);

		entity.setTexturePath((String) properties[1]);
		entity.setTexture((Integer) properties[0]);
		PacketDispatcher.sendPacketToServer(entity.getDescriptionPacket());
	}

	@Override
	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void onWorldLoad(Load event)
	{
		fake = FakeWorld.getFakeWorldFor(event.world);
	}

	@Override
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onWorldLoad(Unload event)
	{
		fake = null;
	}

	@Override
	public FakeWorld getFakeWorld(World world)
	{
		return fake;
	}

	@Override
	public void onKeyPress(String username)
	{
		oneWayFaceAway = !oneWayFaceAway;

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SRM-KeyEvents";
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try
		{
			data.writeUTF(username);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		packet.data = bytes.toByteArray();
		packet.length = packet.data.length;

		PacketDispatcher.sendPacketToServer(packet);

	}

	@Override
	public boolean getFaceAway(String username)
	{
		return oneWayFaceAway;
	}

}
