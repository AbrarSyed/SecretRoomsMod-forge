package com.wynprice.secretroomsmod.render;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockPosUnlistedProperty implements IUnlistedProperty<BlockPos>
{

	@Override
	public String getName() 
	{
		return "UnlistedPosition";
	}

	@Override
	public boolean isValid(BlockPos value) 
	{
		return true;
	}

	@Override
	public Class<BlockPos> getType() 
	{
		return BlockPos.class;
	}

	@Override
	public String valueToString(BlockPos value) 
	{
		return value.toString();
	}

}
