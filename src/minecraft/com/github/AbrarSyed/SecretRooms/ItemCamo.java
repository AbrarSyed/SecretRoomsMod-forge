package com.github.AbrarSyed.SecretRooms;

import net.minecraft.item.Item;

/**
 * @author AbrarSyed
 */
public class ItemCamo extends Item
{

	protected ItemCamo(int par1)
	{
		super(par1);
		setCreativeTab(SecretRooms.tab);
		setTextureFile(SecretRooms.textureFile);
		setIconIndex(1);
	}

}
