package com.github.AbrarSyed.secretroomsmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketShowToggle extends PacketBase
{
    private static final ChatStyle yellowStyle = new ChatStyle().setColor(EnumChatFormatting.YELLOW);
    private static final IChatComponent SECRET = new ChatComponentTranslation("message.secretroomsmod.commandShow.hide").setChatStyle(yellowStyle);
    private static final IChatComponent OBVIOUS = new ChatComponentTranslation("message.secretroomsmod.commandShow.show").setChatStyle(yellowStyle);
    
    @Override
    public void encode(ByteArrayDataOutput output) { }

    @Override
    public void decode(ByteArrayDataInput input) { }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player)
	{
		SecretRooms.displayCamo = !SecretRooms.displayCamo;

		if (SecretRooms.displayCamo)
			player.addChatMessage(SECRET);
		else
			player.addChatMessage(OBVIOUS);

		int rad = 20; // update radius
		world.markBlockRangeForRenderUpdate((int) player.posX - rad, (int) player.posY - rad, (int) player.posZ - rad, (int) player.posX + rad, (int) player.posY + rad, (int) player.posZ + rad);
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) { }
}
