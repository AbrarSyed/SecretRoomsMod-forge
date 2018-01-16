package com.wynprice.secretroomsmod.handler;

import org.lwjgl.input.Keyboard;

import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketEnergizedPaste;
import com.wynprice.secretroomsmod.network.packets.MessagePacketToggleGlassDirection;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class SecretKeyBindings
{
	
	private static final KeyBinding CHANGE_GLASS_DIRECTION = registerKey(new KeyBinding("key.secretroomsmod.oneWayface", Keyboard.KEY_BACKSLASH, "key.categories.secretroomsmod"));
	
	private static final KeyBinding USE_ENERGIZED_PASTE = registerKey(new KeyBinding("key.secretroomsmod.energizedpaste", Keyboard.KEY_B, "key.categories.secretroomsmod"));
	
	private boolean isItem(ItemStack stack)
	{
		return stack.getMetadata() == 1 && stack.getItem() instanceof CamouflagePaste;
	}
	
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
		
		if(USE_ENERGIZED_PASTE.isPressed() && Minecraft.getMinecraft().objectMouseOver.getBlockPos() != BlockPos.ORIGIN && Minecraft.getMinecraft().objectMouseOver.getBlockPos() != null)
		{
			SecretNetwork.sendToServer(new MessagePacketEnergizedPaste(Minecraft.getMinecraft().objectMouseOver.getBlockPos()));
			EntityPlayer player = Minecraft.getMinecraft().player;
			ItemStack stack = isItem(player.getHeldItemMainhand()) ? player.getHeldItemMainhand() : (isItem(player.getHeldItemOffhand()) ? player.getHeldItemOffhand() : null);
			if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("hit_block", 8) && stack.getTagCompound().hasKey("hit_meta", 99))
			{
				Block block = Block.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("hit_block")));
				if(block != Blocks.AIR)
				{
					EnergizedPasteHandler.putState(player.world, Minecraft.getMinecraft().objectMouseOver.getBlockPos(), block.getStateFromMeta(stack.getTagCompound().getInteger("hit_meta")));
					Minecraft.getMinecraft().world.markBlockRangeForRenderUpdate(Minecraft.getMinecraft().objectMouseOver.getBlockPos().add(-1, -1, -1), Minecraft.getMinecraft().objectMouseOver.getBlockPos().add(1, 1, 1));
				}
			}
		}
	}
	
	private static KeyBinding registerKey(KeyBinding key)
	{
		ClientRegistry.registerKeyBinding(key);
		return key;
	}
}
