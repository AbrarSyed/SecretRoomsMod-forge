package com.wynprice.secretroomsmod.handler;

import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.render.fakemodels.SwitchProbeRenderChange;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProbeSwitchRenderHander 
{
	 @SubscribeEvent
	 public void onModelBake(ModelBakeEvent e) 
	 {
		 for(ModelResourceLocation model : e.getModelRegistry().getKeys())
	            if(model.getResourceDomain().equals(SecretRooms5.MODID) && (model.getResourcePath().equals(SecretItems.SWITCH_PROBE.getRegistryName().getResourcePath())
	            		|| model.getResourcePath().equals(SecretItems.PROGRAMMABLE_SWITCH_PROBE.getRegistryName().getResourcePath())))
	            	e.getModelRegistry().putObject(model, new SwitchProbeRenderChange(e.getModelRegistry().getObject(model)));
	 }
}
