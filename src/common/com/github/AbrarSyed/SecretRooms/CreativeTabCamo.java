package com.github.AbrarSyed.SecretRooms;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;

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
        return SecretRooms.camoGhost.blockID;
    }

}
