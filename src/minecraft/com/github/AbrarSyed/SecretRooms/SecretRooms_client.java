package com.github.AbrarSyed.SecretRooms;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.github.AbrarSyed.SecretRooms.Proxy;
import com.github.AbrarSyed.SecretRooms.SecretRooms;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

@SideOnly(value=Side.CLIENT)
public class SecretRooms_client extends Proxy
{
    @SideOnly( value = Side.CLIENT)
    protected static KeyBinding key_OneWayFace = new KeyBinding("Change OneWayBlock face", Keyboard.KEY_BACKSLASH);
    
	@Override
	public void doRenderStuff()
	{
		//System.out.println("renderring stuff is happenning");
		
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
    	Object[] properties = ((BlockOneWay) SecretRooms.oneWay).getOtherProperties(world, i, j , k, metadata);
    	TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(i, j, k);

    	entity.setTexturePath((String)properties[1]);
    	entity.setTexture((Integer)properties[0]);
    	PacketDispatcher.sendPacketToServer(entity.getAuxillaryInfoPacket());
		
	}
	
	@Override
	public Configuration getConfig()
	{
		return new Configuration(new File(FMLClientHandler.instance().getClient().getMinecraftDir(), "/config/SecretRoomsMod.prop"));
	}

}
