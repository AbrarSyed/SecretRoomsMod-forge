package com.github.AbrarSyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

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
