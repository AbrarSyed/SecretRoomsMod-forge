package com.wynprice.secretroomsmod.intergration.walia;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.config.FormattingConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class SecretWaliaDataProvider implements IWailaDataProvider
{
	
	public static final SecretWaliaDataProvider INSTANCE = new SecretWaliaDataProvider();
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if(accessor.getTileEntity() instanceof ISecretTileEntity)
		{
			try
			{
				return ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getPickBlock(
						((ISecretTileEntity)accessor.getTileEntity()).getMirrorState(), 
						accessor.getMOP(), 
						accessor.getWorld(),
						accessor.getPosition(),
						accessor.getPlayer());
			}
			catch (Throwable t) 
			{
				;
			}
			return new ItemStack(((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock());
		}
		return accessor.getStack();
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) 
	{
		if(accessor.getTileEntity() instanceof ISecretTileEntity)
		{
			currenttip.clear();
			if (accessor.getBlockState().getMaterial().isLiquid())
	            return currenttip;

	        String name = null;
	        String displayName = TextFormatting.WHITE + ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getLocalizedName();

	        
	        try
			{
	        	ItemStack stack = ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getPickBlock(
						((ISecretTileEntity)accessor.getTileEntity()).getMirrorState(), 
						accessor.getMOP(), 
						accessor.getWorld(),
						accessor.getPosition(),
						accessor.getPlayer());
	        	
	        	if(!stack.isEmpty())
	        		displayName = DisplayUtil.itemDisplayNameShort(stack);
			}
			catch (Throwable t) 
			{
				;
			}
	        	        
	        int metadata = ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getMetaFromState(((ISecretTileEntity)accessor.getTileEntity()).getMirrorState());
	        if (displayName != null && !displayName.endsWith("Unnamed"))
	            name = displayName;
	        if (name != null)
	            currenttip.add(name);

	        if (itemStack.getItem() == Items.REDSTONE) {
	            String redstoneMeta = "" + metadata;
	            if (redstoneMeta.length() < 2)
	                redstoneMeta = " " + redstoneMeta;
	            currenttip.set(currenttip.size() - 1, name + " " + redstoneMeta);
	        }
	        if (currenttip.size() == 0)
	            currenttip.add("\u00a7r" + String.format(FormattingConfig.blockFormat, "< Unnamed >"));
	        else if (ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA, true) && !Strings.isNullOrEmpty(FormattingConfig.metaFormat))
	            currenttip.add("\u00a7r" + String.format(FormattingConfig.metaFormat, ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getRegistryName().toString(), metadata));

	        return currenttip;
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) 
	{
		return IWailaDataProvider.super.getWailaHead(itemStack, currenttip, accessor, config);
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) 
	{
		if(accessor.getTileEntity() instanceof ISecretTileEntity) 
		{
			currenttip.clear();
			
			String modName = ModIdentification.findModContainer(((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getRegistryName().getResourceDomain()).getName();
	        if (!Strings.isNullOrEmpty(FormattingConfig.modNameFormat))
	            currenttip.add(String.format(FormattingConfig.modNameFormat, modName));
		}
		

        return currenttip;
	}
}
