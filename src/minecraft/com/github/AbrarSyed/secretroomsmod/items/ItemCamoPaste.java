package com.github.AbrarSyed.secretroomsmod.items;

import com.github.AbrarSyed.secretroomsmod.common.SecretRooms;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class ItemCamoPaste extends Item
{

	public ItemCamoPaste(int par1)
	{
		super(par1);
		setCreativeTab(SecretRooms.tab);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_ITEM_PASTE);
	}

}
