package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
	
	@Override
    public ItemStack getActualPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        return null;
    }
    
	@Override
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
