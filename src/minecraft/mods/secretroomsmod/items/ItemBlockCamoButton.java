package mods.secretroomsmod.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockCamoButton extends ItemBlockWithMetadata
{
	private Block block;

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
    		return block.getUnlocalizedName()+".wood";
    		
    	return block.getUnlocalizedName()+".stone";
    }

}
