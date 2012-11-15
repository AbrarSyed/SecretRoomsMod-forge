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

public class SecretKey extends KeyHandler {

	public SecretKey(KeyBinding keyBinding, boolean repeating)
	{
		super(new KeyBinding[] {keyBinding}, new boolean[] {repeating});
		//System.out.println("TEEEEEEEEEEEEEEEEEEST");
	}

	@Override
	public EnumSet<TickType> ticks() 
	{
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel()
	{
		return "SecretRoomsFacing-key";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,	boolean tickEnd, boolean isRepeat)
	{
		// do nothing
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd)
	{
		if (!tickEnd)
			return;
		
        SecretRooms.OneWayFaceTowards = !SecretRooms.OneWayFaceTowards;
        
    	if (SecretRooms.OneWayFaceTowards)
    	{
    		chat(PacketHandlerClient.getColorThing()+"-- !!! OneWayBlock facing set to Towards !!! --");
    		System.out.println("chat true");
    	}
    	else
    	{
    		chat(PacketHandlerClient.getColorThing()+"-- !!! OneWayBlock facing set to Away !!! --");
    		System.out.println("chat false");
    	}
	}
	
    public static void chat(String s)
    {
		FMLClientHandler.instance().getClient().thePlayer.addChatMessage(s);
    }

}
