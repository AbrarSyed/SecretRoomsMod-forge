package com.github.AbrarSyed.secretroomsmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class SecretKeyHandler
{
    private static final ChatStyle yellowStyle = new ChatStyle().setColor(EnumChatFormatting.YELLOW);
    private static final IChatComponent TOWARDS = new ChatComponentTranslation("message.secretroomsmod.oneWay.towards").setChatStyle(yellowStyle);
    private static final IChatComponent AWAY = new ChatComponentTranslation("message.secretroomsmod.oneWay.away").setChatStyle(yellowStyle);
    
    @SubscribeEvent
	public void keyPress(KeyInputEvent event)
	{
        if (GameSettings.isKeyDown(ProxyClient.key_OneWayFace)) // my button
        {
            EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

            if (player == null || player.worldObj == null || Minecraft.getMinecraft().currentScreen != null)
                return;

            SecretRooms.proxy.onKeyPress(player.getUniqueID());

            if (SecretRooms.proxy.getFaceTowards(player.getUniqueID()))
            {
                player.addChatMessage(TOWARDS);
            }
            else
            {
                player.addChatMessage(AWAY);
            }
        }
	}
}
