package com.github.AbrarSyed.secretroomsmod.client;

import java.util.UUID;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.github.AbrarSyed.secretroomsmod.common.ProxyCommon;
import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.network.PacketSRM2Key;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class ProxyClient extends ProxyCommon
{
	public static KeyBinding	key_OneWayFace;
	private boolean				oneWayFaceTowards	= true;

	public ProxyClient()
	{
		key_OneWayFace = new KeyBinding("Change OneWayBlock face", Keyboard.KEY_BACKSLASH);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void loadRenderStuff()
	{
		SecretRooms.render3DId = RenderingRegistry.getNextAvailableRenderId();
		SecretRooms.renderFlatId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BlockRenderer(SecretRooms.render3DId));
		RenderingRegistry.registerBlockHandler(new BlockRenderer(SecretRooms.renderFlatId));
	}

	@Override
	public void loadKeyStuff()
	{
		KeyBindingRegistry.registerKeyBinding(new SecretKey(key_OneWayFace));
	}

	@Override
	public void onServerStop(FMLServerStoppingEvent e)
	{
		super.onServerStop(e);
		oneWayFaceTowards = true;
	}

	@Override
	public void onKeyPress(UUID uuid)
	{
		oneWayFaceTowards = !oneWayFaceTowards;
		PacketDispatcher.sendPacketToServer(new PacketSRM2Key().getPacket250());
	}

	@Override
	public boolean getFaceTowards(String username)
	{
		return oneWayFaceTowards;
	}

}
