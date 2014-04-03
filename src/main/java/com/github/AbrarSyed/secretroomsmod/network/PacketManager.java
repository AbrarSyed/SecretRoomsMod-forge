package com.github.AbrarSyed.secretroomsmod.network;

import static cpw.mods.fml.relauncher.Side.CLIENT;
import static cpw.mods.fml.relauncher.Side.SERVER;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Constructor;
import java.util.EnumMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Sharable
public class PacketManager extends FMLIndexedMessageToMessageCodec<PacketBase>
{
    private static final Logger        LOGGER   = LogManager.getLogger();
    private static final PacketManager INSTANCE = new PacketManager();
    private static final EnumMap<Side, FMLEmbeddedChannel> channels = Maps.newEnumMap(Side.class);

    public static void init()
    {
        if (!channels.isEmpty()) // avoid duplicate inits..
            return;
        
        INSTANCE.addDiscriminator(0, PacketCamo.class);
        INSTANCE.addDiscriminator(1, PacketShowToggle.class);
        INSTANCE.addDiscriminator(2, PacketKey.class);

        channels.putAll(NetworkRegistry.INSTANCE.newChannel("SecretRooms", INSTANCE));
    }
    
    // IO METHODS

    @Override
    public void encodeInto(ChannelHandlerContext ctx, PacketBase packet, ByteBuf target) throws Exception
    {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        packet.encode(output);
        target.writeBytes(output.toByteArray());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, PacketBase packet)
    {
        ByteArrayDataInput input = ByteStreams.newDataInput(source.array());
        input.skipBytes(1); // skip the packet identifier byte
        packet.decode(input);

        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            Minecraft mc = Minecraft.getMinecraft();
            packet.actionClient(mc.theWorld, mc.thePlayer);
        }
        else
        {
            EntityPlayerMP player = ((NetHandlerPlayServer)ctx.channel().attr(NetworkRegistry.NET_HANDLER).get()).playerEntity;
            packet.actionServer(player.worldObj, player);
        }
    }
    
    // UTIL SENDING METHODS
    
    public static void sendToServer(PacketBase packet)
    {
        channels.get(CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(CLIENT).writeAndFlush(packet);
    }
    
    public static void sendToPlayer(PacketBase packet, EntityPlayer player)
    {
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(SERVER).writeAndFlush(packet);
    }

    public static void sendToAllAround(PacketBase packet, NetworkRegistry.TargetPoint point)
    {
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(SERVER).writeAndFlush(packet);
    }

    public static void sendToDimension(PacketBase packet, int dimension)
    {
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimension);
        channels.get(SERVER).writeAndFlush(packet);
    }
    
    public static void sendToAll(PacketBase packet)
    {
        channels.get(SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(SERVER).writeAndFlush(packet);
    }
    
    public static Packet toMcPacket(PacketBase packet)
    {
        return channels.get(FMLCommonHandler.instance().getEffectiveSide()).generatePacketFrom(packet);
    }
    
    // OTHER METHODS

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        LOGGER.log(Level.ERROR, "Error in packet handling!", cause);
        ctx.fireExceptionCaught(cause); // for the other handlers.
    }

    @Override
    public FMLIndexedMessageToMessageCodec<PacketBase> addDiscriminator(int discriminator, Class<? extends PacketBase> type)
    {
        // double check it has an empty constructor. or fail early.
        // This is to gaurd myself against my own stupidity.

        if (!hasEmptyContructor(type))
        {
            LOGGER.log(Level.FATAL, type.getName() + "does not have an empty constructor!");
        }

        return super.addDiscriminator(discriminator, type);
    }

    @SuppressWarnings("rawtypes")
    private static boolean hasEmptyContructor(Class type)
    {
        try
        {
            for (Constructor c : type.getConstructors())
            {
                if (c.getParameterTypes().length == 0)
                {
                    return true;
                }
            }
        }
        catch (SecurityException e)
        {
            // really?
        }

        return false;
    }

}
