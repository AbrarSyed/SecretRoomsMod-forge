package com.github.AbrarSyed.SecretRooms;

import net.minecraft.src.CreativeTabs;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class CreativeTabCamo extends CreativeTabs
{
	public CreativeTabCamo()
	{
		super("Secret Items");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getTabIconItemIndex()
	{
		return SecretRooms.camoPaste.shiftedIndex;
	}

}
