package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketUpdateClient extends BaseMessagePacket<MessagePacketUpdateClient>
{
	
	public MessagePacketUpdateClient() {
	}
	
	private NBTTagCompound compound;
	
	public MessagePacketUpdateClient(NBTTagCompound compound) {
		this.compound = compound;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, compound);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void onReceived(MessagePacketUpdateClient message, EntityPlayer player) 
	{
		TileEntity te = player.world.getTileEntity(new BlockPos(message.compound.getInteger("x"), message.compound.getInteger("y"), message.compound.getInteger("z")));
		if(te instanceof ISecretTileEntity)
			((ISecretTileEntity)te).loadFromNBT(message.compound);
	}

}
