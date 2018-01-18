package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.network.SecretNetwork;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MessagePacketEnergizedPaste extends BaseMessagePacket<MessagePacketEnergizedPaste>
{
	public MessagePacketEnergizedPaste() {
	}
	
	private BlockPos pos;
	private boolean set;
	
	public MessagePacketEnergizedPaste(BlockPos pos, boolean set) 
	{
		this.pos = pos;
		this.set = set;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.set = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeBoolean(set);
	}
	
	private boolean isItem(ItemStack stack)
	{
		return stack.getMetadata() == 1 && stack.getItem() instanceof CamouflagePaste;
	}
	
	@Override
	public void onReceived(MessagePacketEnergizedPaste message, EntityPlayer player) 
	{
		if(message.set)
		{
			EnumHand hand = isItem(player.getHeldItemMainhand()) ? EnumHand.MAIN_HAND : (isItem(player.getHeldItemOffhand()) ? EnumHand.OFF_HAND : null);
			if(hand != null)
			{
				ItemStack stack = player.getHeldItem(hand);
				if(stack.hasTagCompound() && stack.getTagCompound().hasKey("hit_block", 8) && stack.getTagCompound().hasKey("hit_meta", 99))
				{
					Block block = Block.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("hit_block")));
					if(block != Blocks.AIR)
					{
						IBlockState state = block.getStateFromMeta(stack.getTagCompound().getInteger("hit_meta"));
						if(EnergizedPasteHandler.canBlockBeMirrored(block, player.world,  state, message.pos) && EnergizedPasteHandler.canBlockBeReplaced(player.world.getBlockState( message.pos).getBlock(), player.world, player.world.getBlockState(message.pos), message.pos))
						{
							EnergizedPasteHandler.putState(player.world, message.pos, state);
							SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(player.world.provider.getDimension(), message.pos, state, true));
							player.swingArm(hand);
						}
					}
				}
			}
		}
		else
		{
			EnergizedPasteHandler.removeReplacedState(player.world.provider.getDimension(), message.pos);
			SecretNetwork.sendToAll(new MessagePacketSyncEnergizedPaste(player.world.provider.getDimension(), message.pos, null, false));
		}
		
	}

}
