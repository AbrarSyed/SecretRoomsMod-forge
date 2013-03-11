package com.github.AbrarSyed.SecretRooms.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

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
        this.iconIndex = par1IconRegister.func_94245_a(SecretRooms.TEXTURE_ITEM_PASTE);
    }

}
