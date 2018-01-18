package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.network.SecretNetwork;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class MessagePacketRemoveEnergizedPaste extends BaseMessagePacket<MessagePacketRemoveEnergizedPaste>
{
	
	public MessagePacketRemoveEnergizedPaste() {
	}
	
	private BlockPos position;
	
	public MessagePacketRemoveEnergizedPaste(BlockPos position) 
	{
		this.position = position;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.position.getX());
		buf.writeInt(this.position.getY());
		buf.writeInt(this.position.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.position = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void onReceived(MessagePacketRemoveEnergizedPaste message, EntityPlayer player)
	{
		if(EnergizedPasteHandler.hasReplacedState(player.world, message.position))
		{
			IBlockState state = EnergizedPasteHandler.getReplacedState(player.world, message.position);
			EnergizedPasteHandler.removeReplacedState(player.world.provider.getDimension(), message.position);
			SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(player.world.provider.getDimension(), message.position, null, false));
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("hit_block", state.getBlock().getRegistryName().toString());
			nbt.setInteger("hit_meta", state.getBlock().getMetaFromState(state));
			nbt.setInteger("hit_color", state.getMapColor(player.world, message.position).colorValue);
			ItemStack stack = new ItemStack(SecretItems.CAMOUFLAGE_PASTE, 1, 1);
			stack.setTagCompound(nbt);
			player.world.spawnEntity(new EntityItem(player.world, message.position.getX() + 0.5d, message.position.getY() + 1d, message.position.getZ() + 0.5d, stack));
		}
	}

}
