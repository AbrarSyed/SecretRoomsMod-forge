package com.wynprice.secretroomsmod.proxy;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.render.TileEntityInfomationHolderRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy 
{
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
		
		SecretItems.regRenders();
		
		SecretBlocks.regRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfomationHolder.class, new TileEntityInfomationHolderRenderer());
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
