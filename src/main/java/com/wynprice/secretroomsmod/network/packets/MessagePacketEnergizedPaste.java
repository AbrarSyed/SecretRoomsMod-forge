package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.items.ProgrammableSwitchProbe;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MessagePacketEnergizedPaste extends BaseMessagePacket<MessagePacketEnergizedPaste>
{
	public MessagePacketEnergizedPaste() {
	}
	
	private BlockPos pos;
	
	public MessagePacketEnergizedPaste(BlockPos pos) 
	{
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	private boolean isItem(ItemStack stack)
	{
		return stack.getMetadata() == 1 && stack.getItem() instanceof CamouflagePaste;
	}
	
	@Override
	public void onReceived(MessagePacketEnergizedPaste message, EntityPlayer player) 
	{
		ItemStack stack = isItem(player.getHeldItemMainhand()) ? player.getHeldItemMainhand() : (isItem(player.getHeldItemOffhand()) ? player.getHeldItemOffhand() : null);
		if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("hit_block", 8) && stack.getTagCompound().hasKey("hit_meta", 99))
		{
			Block block = Block.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("hit_block")));
			if(block != Blocks.AIR)
				EnergizedPasteHandler.putState(player.world, message.pos, block.getStateFromMeta(stack.getTagCompound().getInteger("hit_meta")));
		}
	}

}
