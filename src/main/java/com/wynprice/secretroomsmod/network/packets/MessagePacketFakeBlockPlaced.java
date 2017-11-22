package com.wynprice.secretroomsmod.network.packets;

import com.wynprice.secretroomsmod.base.BaseMessagePacket;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.ServerRecievePacketHandler;
import com.wynprice.secretroomsmod.handler.ServerRecievePacketHandler.ObjectInfo;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessagePacketFakeBlockPlaced extends BaseMessagePacket<MessagePacketFakeBlockPlaced>
{
	
	public MessagePacketFakeBlockPlaced() {
	}
	
	private BlockPos pos;
	private BlockPos lookPos;
	private IBlockState mouseOver;
	private int multiplyColor;
	
	@SideOnly(Side.CLIENT)
	public MessagePacketFakeBlockPlaced(BlockPos pos, BlockPos lookPos, IBlockState state) 
	{
		this.pos = pos;
		this.lookPos = lookPos;
		if(state == null)
			return;
		this.mouseOver = state;
		if(mouseOver != null)
			this.multiplyColor = Minecraft.getMinecraft().getBlockColors()
			.colorMultiplier(mouseOver, Minecraft.getMinecraft().world,
					Minecraft.getMinecraft().objectMouseOver.getBlockPos(), 0);
		ISecretTileEntity.getMap(Minecraft.getMinecraft().world).put(pos, mouseOver);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		
		buf.writeInt(lookPos.getX());
		buf.writeInt(lookPos.getY());
		buf.writeInt(lookPos.getZ());
		
		if(mouseOver != null)
		{
			ByteBufUtils.writeUTF8String(buf, mouseOver.getBlock().getRegistryName().toString());
			buf.writeInt(mouseOver.getBlock().getMetaFromState(mouseOver));
		}
		else
		{
			ByteBufUtils.writeUTF8String(buf, "");
			buf.writeInt(0);
		}
		
		
		buf.writeInt(multiplyColor);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		lookPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		Block testBlock = Block.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
		int meta = buf.readInt();
		if(testBlock != Blocks.AIR)
			mouseOver = testBlock.getStateFromMeta(meta);
		multiplyColor = buf.readInt();
	}

	@Override
	public void onReceived(MessagePacketFakeBlockPlaced message, EntityPlayer player) 
	{
		ISecretTileEntity.getMap(player.world).put(message.pos, message.mouseOver);
		ServerRecievePacketHandler.UPDATE_MAP.put(message.pos, new ObjectInfo(message.mouseOver, message.lookPos));
	}
	
}
