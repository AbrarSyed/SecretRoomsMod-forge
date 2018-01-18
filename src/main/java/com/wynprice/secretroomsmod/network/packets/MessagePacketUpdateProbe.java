package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.items.ProgrammableSwitchProbe;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketUpdateProbe extends BaseMessagePacket<MessagePacketUpdateProbe> 
{
	public MessagePacketUpdateProbe() {
	}
	
	private NBTTagCompound compound;
	public MessagePacketUpdateProbe(NBTTagCompound compound) {
		this.compound = compound;
	}
	
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeTag(buf, compound);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void onReceived(MessagePacketUpdateProbe message, EntityPlayer player) 
	{
		ItemStack stack = player.getHeldItemMainhand().getItem() instanceof ProgrammableSwitchProbe ? player.getHeldItemMainhand() : (player.getHeldItemOffhand().getItem() instanceof ProgrammableSwitchProbe ? player.getHeldItemOffhand() : null);
		if(stack != null)
			stack.setTagCompound(message.compound);
	}

}
