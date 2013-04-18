package mods.SecretRoomsMod.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mods.SecretRoomsMod.SecretRooms;
import mods.SecretRoomsMod.common.FakeWorld;
import mods.SecretRoomsMod.common.ProxyCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import org.lwjgl.input.Keyboard;


import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class ProxyClient extends ProxyCommon
{
	public static KeyBinding	key_OneWayFace;
	private FakeWorld			fake;
	private boolean				oneWayFaceAway	= true;

	public ProxyClient()
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
	}

	@Override
	public void loadKeyStuff()
	{
		KeyBindingRegistry.registerKeyBinding(new SecretKey(key_OneWayFace));
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
		if (fake == null)
		{
			fake = FakeWorld.getFakeWorldFor(Minecraft.getMinecraft().theWorld);
		}
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
