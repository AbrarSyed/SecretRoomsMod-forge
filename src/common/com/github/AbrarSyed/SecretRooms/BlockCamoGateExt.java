package com.github.AbrarSyed.SecretRooms;

import java.util.Random;


public class BlockCamoGateExt extends BlockCamoFull
{
    public BlockCamoGateExt(int i)
    {
        super(i);
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
