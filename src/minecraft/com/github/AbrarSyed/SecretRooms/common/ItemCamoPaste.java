package com.github.AbrarSyed.SecretRooms.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class ItemCamoPaste extends Item
{

	protected ItemCamoPaste(int par1)
	{
		super(par1);
		setCreativeTab(SecretRooms.tab);
	}

	@SideOnly(Side.CLIENT)
	public void func_94581_a(IconRegister par1IconRegister)
	{
		iconIndex = par1IconRegister.registerIcon(SecretRooms.TEXTURE_ITEM_PASTE);
	}

}
