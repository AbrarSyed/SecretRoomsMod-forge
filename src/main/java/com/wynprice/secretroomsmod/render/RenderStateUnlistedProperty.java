package com.wynprice.secretroomsmod.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * The unlisted Property containing the render state
 * @author Wyn Price
 *
 */
public class RenderStateUnlistedProperty implements IUnlistedProperty<IBlockState>
{

	@Override
	public String getName() 
	{
		return "UnlistedMirrorState";
	}

	@Override
	public boolean isValid(IBlockState value) 
	{
		return true;
	}

	@Override
	public Class<IBlockState> getType() 
	{
		return IBlockState.class;
	}

	@Override
	public String valueToString(IBlockState value) 
	{
		return value.toString();
	}

}
