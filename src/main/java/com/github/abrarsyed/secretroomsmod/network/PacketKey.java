package com.github.abrarsyed.secretroomsmod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketKey extends PacketBase
{

    @Override
    public void encode(ByteArrayDataOutput output) { }

    @Override
    public void decode(ByteArrayDataInput input) { }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) { }

    @Override
    public void actionServer(World world, EntityPlayerMP player)
    {
        if (FMLCommonHandler.instance().getSide().isServer())
            SecretRooms.proxy.onKeyPress(player.getUniqueID());
    }

}
