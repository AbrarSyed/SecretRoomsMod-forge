package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoDummy extends BlockCamoFull
{
	public BlockCamoDummy()
	{
		super();
		setHardness(2.5F);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(null);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
    @SideOnly(Side.CLIENT)
    public ItemStack getActualPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return null;
    }
	
    public Item getItemDropped(int i, Random random, int j)
    {
        return null;
    }

	@Override
	public int getMobilityFlag()
	{
		return 2;
	}
}
