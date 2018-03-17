package com.wynprice.secretroomsmod.integration.walia;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.EnergizedPasteHandler;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.config.FormattingConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class SecretWaliaDataProvider implements IWailaDataProvider
{
	
	public static final SecretWaliaDataProvider INSTANCE = new SecretWaliaDataProvider();
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		ItemStack ret = ItemStack.EMPTY;
		if(accessor.getTileEntity() instanceof ISecretTileEntity && ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState() != null  && !TrueSightHelmet.isHelmet(accessor.getPlayer()))
		{
			try
			{
				ret = ((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock().getPickBlock(
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
			ret = new ItemStack(((ISecretTileEntity)accessor.getTileEntity()).getMirrorState().getBlock());
		}
		else if(EnergizedPasteHandler.hasReplacedState(accessor.getWorld(), accessor.getPosition()))
		{
			IBlockState state = EnergizedPasteHandler.getReplacedState(accessor.getWorld(), accessor.getPosition());
			ret = new ItemStack(state.getBlock());
			try
			{
				ret = state.getBlock().getPickBlock(state, accessor.getMOP(), accessor.getWorld(), accessor.getPosition(), accessor.getPlayer());
			}
			catch (Exception e) 
			{
				;
			}
		}
		return ret;
	}
}
