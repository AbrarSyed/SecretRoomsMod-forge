package com.wynprice.secretroomsmod.base;

import java.util.List;
import java.util.Random;

import com.wynprice.secretroomsmod.tileentity.TileEntitySecretPressurePlate;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BaseFakePressurePlate extends BaseFakeBlock
{
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
	public BaseFakePressurePlate(String name, Material materialIn) {
		super(name, materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
	}
	
	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) 
	{
		calculateState(worldIn, pos);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return getState(world, pos).getBlock().canBeConnectedTo(world, pos, facing);
	}
	
	public void calculateState(World worldIn, BlockPos pos)
	{
		List<Entity> entityList = worldIn.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1.1f, pos.getZ() + 1f));
		int level = MathHelper.clamp(getLevel(worldIn, pos, entityList), 0, 15);
		if((level == 0 || worldIn.getBlockState(pos).getValue(POWER) == 0) && level != worldIn.getBlockState(pos).getValue(POWER))
	        worldIn.playSound((EntityPlayer)null, pos, level == 0 ? soundOff() : soundOn(), SoundCategory.BLOCKS, 0.3F, level == 0 ?  0.75F : 0.90000004F);

		if(worldIn.getBlockState(pos).getValue(POWER) != entityList.size())
		{
			worldIn.setBlockState(pos, setRedstoneStrength(worldIn.getBlockState(pos), level), 3);
			worldIn.notifyNeighborsOfStateChange(pos, this, false);
	        worldIn.notifyNeighborsOfStateChange(pos.down(), this, false);
		}
		if(worldIn.getBlockState(pos).getValue(POWER) > 0)
			worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
	}
	
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySecretPressurePlate();
	}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && state.getValue(POWER) > 0)
        	onEntityWalk(worldIn, pos, null);
    }
	
	protected abstract int getLevel(World world, BlockPos pos, List<Entity> entityList);
	
	protected abstract SoundEvent soundOn();
	
	protected abstract SoundEvent soundOff();
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return blockState.getValue(POWER);
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN ? blockState.getValue(POWER) : 0;
	}

    protected IBlockState setRedstoneStrength(IBlockState state, int strength)
    {
        return state.withProperty(POWER, Integer.valueOf(strength));
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 20;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWER});
    }

}
