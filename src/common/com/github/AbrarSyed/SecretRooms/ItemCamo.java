package com.github.AbrarSyed.SecretRooms;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemCamo extends Item {

	protected ItemCamo(int par1)
	{
		super(par1);
		this.setTabToDisplayOn(CreativeTabs.tabMaterials);
		this.setTextureFile(SecretRooms.textureFile);
		this.setIconIndex(1);
	}

}
