package com.github.AbrarSyed.secretroomsmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketShowTogle extends PacketBase
{
    private static final IChatComponent SECRET = new ChatComponentText(EnumChatFormatting.YELLOW + "Camo blocks made secret");
    private static final IChatComponent OBVIOUS = new ChatComponentText(EnumChatFormatting.YELLOW + "Camo blocks made obvious");
    
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
