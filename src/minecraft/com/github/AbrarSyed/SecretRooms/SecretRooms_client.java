package com.github.AbrarSyed.SecretRooms;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(value = Side.CLIENT)
public class SecretRooms_client extends Proxy
{
	@SideOnly(value = Side.CLIENT)
	protected static KeyBinding	key_OneWayFace	= new KeyBinding("Change OneWayBlock face", Keyboard.KEY_BACKSLASH);

	@Override
	public void doRenderStuff()
	{
		// System.out.println("renderring stuff is happenning");

		SecretRooms.camoRenderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new CamoRenderer());

		SecretRooms.torchRenderId = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new TorchRenderer());

		MinecraftForgeClient.preloadTexture(SecretRooms.textureFile);
	}

	@Override
	public void doKeyStuff()
	{
		KeyBindingRegistry.registerKeyBinding(new SecretKey(key_OneWayFace, false));
	}

	@Override
	public void doOneWayStuff(World world, int i, int j, int k, EntityLiving entityliving)
	{
		int metadata = world.getBlockMetadata(i, j, k);
		Object[] properties = ((BlockOneWay) SecretRooms.oneWay).getOtherProperties(world, i, j, k, metadata);
		TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(i, j, k);

		entity.setTexturePath((String) properties[1]);
		entity.setTexture((Integer) properties[0]);
		PacketDispatcher.sendPacketToServer(entity.getDescriptionPacket());

	}

}
