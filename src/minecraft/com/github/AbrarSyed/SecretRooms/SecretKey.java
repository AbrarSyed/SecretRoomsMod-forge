package com.github.AbrarSyed.SecretRooms;

import java.util.EnumSet;

import org.lwjgl.input.Keyboard;

import com.github.AbrarSyed.SecretRooms.SecretRooms;

import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.Packet250CustomPayload;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(value=Side.CLIENT)
public class SecretKey extends KeyHandler {

	public SecretKey(KeyBinding keyBinding, boolean repeating)
	{
		super(new KeyBinding[] {keyBinding}, new boolean[] {repeating});
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.WORLDLOAD);
	}

	@Override
	public String getLabel()
	{
		return null;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,	boolean tickEnd, boolean isRepeat)
	{
		// do nothing.
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		/*
    	// check if its in SMP
    	if (ModLoader.getMinecraftInstance().theWorld != null && ModLoader.getMinecraftInstance().theWorld.isRemote)
    	{
    		// create packet AND set channel
    		Packet250CustomPayload packet = new Packet250CustomPayload();
    		packet.channel = "SRM-KeyEvents";
    		
    		// set byte[] data
    		packet.data = SecretRooms.intToByteArray(Keyboard.KEY_BACKSLASH);
    		
    		// set length        		
    		packet.length = packet.data.length;

    		// send packet.
    		ModLoader.clientSendPacket(packet);
    	}
    	*/
    	
        SecretRooms.OneWayFaceTowards = !SecretRooms.OneWayFaceTowards;
        if (!ModLoader.getMinecraftInstance().theWorld.isRemote)
        {
        	if (SecretRooms.OneWayFaceTowards)
        		chat("§e-- !!! OneWayBlock facing set to Towards !!! --");
        	else
        		chat("§e-- !!! OneWayBlock facing set to Away !!! --");
        }
	}
	
	@SideOnly( value = Side.CLIENT)
    public static void chat(String s)
    {
		FMLClientHandler.instance().getClient().thePlayer.addChatMessage(s);
    }

}
