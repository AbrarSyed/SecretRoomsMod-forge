package com.github.AbrarSyed.secretroomsmod.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockCamoButton extends ItemBlockWithMetadata
{

	public ItemBlockCamoButton(Block block)
	{
		super(block, block);
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack stack)
	{
	    // TODO: fix localizations. 
		if (stack.getItemDamage() == 1)
			return "mod_SRM.SecretButton.wood";

		return "mod_SRM.SecretButton.stone";
	}

}
