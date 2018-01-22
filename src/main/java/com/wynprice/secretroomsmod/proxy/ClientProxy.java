package com.wynprice.secretroomsmod.proxy;

import java.awt.Color;
import java.lang.reflect.Field;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.gui.GuiProgrammableSwitchProbe;
import com.wynprice.secretroomsmod.handler.HandlerUpdateChecker;
import com.wynprice.secretroomsmod.handler.ProbeSwitchRenderHander;
import com.wynprice.secretroomsmod.handler.ReloadTrueSightModelsHandler;
import com.wynprice.secretroomsmod.handler.SecretKeyBindings;
import com.wynprice.secretroomsmod.optifinehelpers.EOACV;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifine;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifineHelper;
import com.wynprice.secretroomsmod.render.FakeChunkRenderFactory;
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
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.items.ItemStackHandler;

public class ClientProxy extends CommonProxy 
{	
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		for(ASMData data : event.getAsmData().getAll(SecretOptifine.class.getCanonicalName()))
			try {
				EOACV.valueOf(((EnumHolder)data.getAnnotationInfo().get("version")).getValue()).arrayCache = Class.forName(data.getClassName()).newInstance();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		
		super.preInit(event);

		
		SecretItems.regRenders();
		
		SecretBlocks.regRenders();
				
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInfomationHolder.class, new TileEntityInfomationHolderRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySecretDispenser.class, new TileEntityInfomationHolderRendererDispenser());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySecretPressurePlate.class, new TileEntityInfomationHolderRenderPlate());
		
		Object[] handlers = {
				new ProbeSwitchRenderHander(),
				new SecretKeyBindings(),
				new HandlerUpdateChecker(),
				new ReloadTrueSightModelsHandler()
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
		if(SecretOptifineHelper.IS_OPTIFINE)
			SecretRooms5.LOGGER.info("Enabling support for Optifine CTM");

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
        
        itemColors.registerItemColorHandler(new IItemColor() 
        {
        	private int currentID = 0;
        	private long previousCurrentMills = System.currentTimeMillis();
        	
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) 
			{
				return stack.getMetadata() == 1? color(stack.hasTagCompound() && stack.getTagCompound().hasKey("hit_color", 99) ? stack.getTagCompound().getInteger("hit_color") == 0 ? 0xFFFFFF : stack.getTagCompound().getInteger("hit_color") : 0xFFFFFF) : -1;
			}
			
			private int color(int stackcolor)
			{
				if(System.currentTimeMillis() - previousCurrentMills > 0)//Prevent color moving faster if more instances of item
				{
					previousCurrentMills = System.currentTimeMillis();
					currentID += 10;
				}
				Color color1 = new Color(Color.HSBtoRGB((currentID % 4000) / 4000f, 1f, 1f));
				Color color2 = new Color(stackcolor);
				double totalAlpha = color1.getAlpha() + color2.getAlpha();
			    double weight0 = 0.25f;
			    double weight1 = 0.75f;
			    double r = weight0 * color1.getRed() + weight1 * color2.getRed();
			    double g = weight0 * color1.getGreen() + weight1 * color2.getGreen();
			    double b = weight0 * color1.getBlue() + weight1 * color2.getBlue();
			    double a = Math.max(color1.getAlpha(), color2.getAlpha());
			    return new Color((int) r, (int) g, (int) b, (int) a).getRGB();
			}
		}, SecretItems.CAMOUFLAGE_PASTE);
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}
