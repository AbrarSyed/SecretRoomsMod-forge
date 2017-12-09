package com.wynprice.secretroomsmod.proxy;

import java.lang.reflect.Field;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.gui.GuiProgrammableSwitchProbe;
import com.wynprice.secretroomsmod.handler.HandlerUpdateChecker;
import com.wynprice.secretroomsmod.handler.ProbeSwitchRenderHander;
import com.wynprice.secretroomsmod.handler.SecretKeyBindings;
import com.wynprice.secretroomsmod.render.FakeChunkRenderFactory;
import com.wynprice.secretroomsmod.render.SecretOptifine;
import com.wynprice.secretroomsmod.render.TERenders.TileEntityInfomationHolderRenderPlate;
import com.wynprice.secretroomsmod.render.TERenders.TileEntityInfomationHolderRenderer;
import com.wynprice.secretroomsmod.render.TERenders.TileEntityInfomationHolderRendererDispenser;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretDispenser;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretPressurePlate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.ItemStackHandler;

public class ClientProxy extends CommonProxy 
{
	public static Object secretOptifine;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		super.preInit(event);
				
		try
		{
			for (ASMDataTable.ASMData asmData : event.getAsmData().getAll(SecretOptifine.class.getCanonicalName()))
				secretOptifine = Class.forName(asmData.getClassName()).newInstance();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
				
		SecretItems.regRenders();
		
		SecretBlocks.regRenders();
				
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfomationHolder.class, new TileEntityInfomationHolderRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySecretDispenser.class, new TileEntityInfomationHolderRendererDispenser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySecretPressurePlate.class, new TileEntityInfomationHolderRenderPlate());
		
		Object[] handlers = {
				new ProbeSwitchRenderHander(),
				new SecretKeyBindings(),
				new HandlerUpdateChecker()
    	};
    	for(Object o : handlers)
    		MinecraftForge.EVENT_BUS.register(o);
	}
	
	@Override
	public void displayGui(int guiID, Object... objects) 
	{
		GuiScreen gui = null;
		switch (guiID) 
		{
		case 0:
			gui = new GuiProgrammableSwitchProbe((ItemStack) objects[0]);
			break;

		default:
			break;
		}
		
		Minecraft.getMinecraft().displayGuiScreen(gui);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		try
		{
			for(Field field : RenderGlobal.class.getDeclaredFields())
				if(field.getType() == IRenderChunkFactory.class)
				{
					field.setAccessible(true);
					field.set(Minecraft.getMinecraft().renderGlobal, new FakeChunkRenderFactory((IRenderChunkFactory) field.get(Minecraft.getMinecraft().renderGlobal)));
					field.setAccessible(false);
				}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		    	
    	ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

        itemColors.registerItemColorHandler((stack, tintIndex) -> 
        {
	        if(stack.hasTagCompound() && GuiScreen.isAltKeyDown())
	    	{
	        	ItemStackHandler handler = new ItemStackHandler(1);
		    	handler.deserializeNBT(stack.getTagCompound().getCompoundTag("hit_itemstack"));
		    	return itemColors.colorMultiplier(handler.getStackInSlot(0), tintIndex);
	    	}
            return 0xFFFFFF;
        }, SecretItems.SWITCH_PROBE);
        
        itemColors.registerItemColorHandler((stack, tintIndex) -> 
        {
	        if(stack.hasTagCompound() && GuiScreen.isAltKeyDown())
	    	{
	        	ItemStackHandler handler = new ItemStackHandler(1);
		    	handler.deserializeNBT(stack.getTagCompound().getCompoundTag("hit_itemstack"));
		    	return itemColors.colorMultiplier(handler.getStackInSlot(0), tintIndex);
	    	}
            return 0xFFFFFF;
        }, SecretItems.PROGRAMMABLE_SWITCH_PROBE);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
