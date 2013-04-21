package mods.SecretRoomsMod.blocks;

import java.util.Random;

import net.minecraft.block.Block;

/**
 * @author AbrarSyed
 */
public class BlockCamoGateExt extends BlockCamoFull
{
	public BlockCamoGateExt(int i)
	{
		super(i);
		setHardness(2.5F);
		setStepSound(Block.soundWoodFootstep);
		setCreativeTab(null);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public int idDropped(int i, Random random, int j)
	{
		return 0;
	}

	@Override
	public int getMobilityFlag()
	{
		return 2;
	}
}
