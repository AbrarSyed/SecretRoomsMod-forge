package com.wynprice.secretroomsmod.blocks;

import com.wynprice.secretroomsmod.tileentity.TileEntitySecretChest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SecretTrappedChest extends SecretChest
{

	public SecretTrappedChest() {
		super("secret_trapped_chest");
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (!blockState.canProvidePower() || !PLAYERS_USING_MAP.get(false).containsKey(pos))
            return 0;
        else
        	return MathHelper.clamp(PLAYERS_USING_MAP.get(false).get(pos), 0, 15);
    }

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP ? blockState.getWeakPower(blockAccess, pos, side) : 0;
    }

}
