package com.wynprice.secretroomsmod.handler;

import org.lwjgl.input.Keyboard;

import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketToggleGlassDirection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class SecretKeyBindings
{
	
	private static final KeyBinding CHANGE_GLASS_DIRECTION = registerKey(new KeyBinding("key.secretroomsmod.oneWayface", Keyboard.KEY_BACKSLASH, "key.categories.secretroomsmod"));
	
	@SubscribeEvent
	public void onKeyPressed(KeyInputEvent event)
	{
		if(CHANGE_GLASS_DIRECTION.isPressed())
		{
			Minecraft.getMinecraft().player.getEntityData().setBoolean("glassDirection", !Minecraft.getMinecraft().player.getEntityData().getBoolean("glassDirection"));
			SecretNetwork.sendToServer(new MessagePacketToggleGlassDirection());
			Minecraft.getMinecraft().player.sendStatusMessage(new TextComponentTranslation("message.secretroomsmod.oneWay."
					+ (Minecraft.getMinecraft().player.getEntityData().getBoolean("glassDirection") ? "towards" : "away")), true);
		}
	}
	
	private static KeyBinding registerKey(KeyBinding key)
	{
		ClientRegistry.registerKeyBinding(key);
		return key;
	}
}
