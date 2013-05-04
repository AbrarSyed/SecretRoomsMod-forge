package mods.secretroomsmod.items;

import com.google.common.base.Strings;

import mods.secretroomsmod.SecretRooms;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

public class ItemBlockCamoButton extends ItemBlockWithMetadata
{
	private Block	block;

	public ItemBlockCamoButton(int par1, Block par2Block)
	{
		super(par1, par2Block);
		block = par2Block;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	public String getUnlocalizedName(ItemStack stack)
	{
		if (stack.getItemDamage() == 1)
			return SecretRooms.camoButton.getUnlocalizedName2() + ".wood";

		return SecretRooms.camoButton.getUnlocalizedName2() + ".stone";
	}
	
	@Override
    public String getItemDisplayName(ItemStack par1ItemStack)
    {
        return this.getLocalizedName(par1ItemStack);
    }

}
