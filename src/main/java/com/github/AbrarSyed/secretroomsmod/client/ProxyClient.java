package com.github.AbrarSyed.secretroomsmod.client;

import java.util.UUID;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.github.AbrarSyed.secretroomsmod.common.ProxyCommon;
import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.github.AbrarSyed.secretroomsmod.network.PacketKey;
import com.github.AbrarSyed.secretroomsmod.network.PacketManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
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
		key_OneWayFace = new KeyBinding("key.secretroomsmod.oneWayface", Keyboard.KEY_BACKSLASH, "key.categories.gameplay");
		//MinecraftForge.EVENT_BUS.register(this);
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
		ClientRegistry.registerKeyBinding(key_OneWayFace);
		FMLCommonHandler.instance().bus().register(new SecretKeyHandler());
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
		PacketManager.sendToServer(new PacketKey());
	}

	@Override
	public boolean getFaceTowards(UUID uuid)
	{
		return oneWayFaceTowards;
	}

}
