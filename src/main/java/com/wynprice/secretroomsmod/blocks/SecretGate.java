package com.wynprice.secretroomsmod.blocks;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.BaseFakeBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightFaceDiffrentModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SecretGate extends BaseFakeBlock
{
	public static final PropertyBool POWERED = PropertyBool.create("powered");
	public static final PropertyDirection FACING = BlockDirectional.FACING;
	
	private static final int MAX_LEVELS = 10;
	
	public SecretGate() {
		super("secret_gate", Material.IRON);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public TrueSightModel phaseTrueModel(TrueSightModel model) {
		return new TrueSightFaceDiffrentModel(model, FakeBlockModel.getModel(new ResourceLocation(SecretRooms5.MODID, "block/ghost_block")));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) 
	{
		boolean flag = state.getValue(POWERED);
		boolean flag1 = worldIn.isBlockPowered(pos) || worldIn.isBlockIndirectlyGettingPowered(pos) > 0;
		if(flag != flag1)
			if(flag1)
				buildGate(worldIn, pos);
			else if(!flag1)
			{
				destroyGate(worldIn, pos, worldIn.getBlockState(pos));
				worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, worldIn.getBlockState(pos).getValue(FACING)).withProperty(POWERED, false));
			}

	}
	
	protected void buildGate(World worldIn, BlockPos pos)
	{
		EnumFacing direction = worldIn.getBlockState(pos).getValue(FACING);
		IBlockState state = getState(worldIn, pos);
		worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING, direction).withProperty(POWERED, true));
		((ISecretTileEntity)worldIn.getTileEntity(pos)).setMirrorState(state, BlockPos.ORIGIN);
		for(int i = 1; i < MAX_LEVELS + 1; i++)
		{
			BlockPos position = new BlockPos(pos.getX() + (i * direction.getFrontOffsetX()), pos.getY() + (i * direction.getFrontOffsetY()), pos.getZ() + (i * direction.getFrontOffsetZ()));
			if(!worldIn.getBlockState(position).getBlock().isReplaceable(worldIn, position))
				break;
			worldIn.setBlockState(position, SecretBlocks.SECRET_GATE_BLOCK.getDefaultState());
			worldIn.getTileEntity(position).markDirty();
			((ISecretBlock)worldIn.getBlockState(pos).getBlock()).forceBlockState(worldIn, position, BlockPos.ORIGIN, getState(worldIn, pos));
		}

	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) 
	{
		destroyGate(worldIn, pos, state);
		super.breakBlock(worldIn, pos, state);
	}
	
	protected void destroyGate(World worldIn, BlockPos pos, IBlockState blockstate)
	{
		EnumFacing direction = blockstate.getValue(FACING);
		IBlockState state = getState(worldIn, pos);
		((ISecretTileEntity)worldIn.getTileEntity(pos)).setMirrorState(state, BlockPos.ORIGIN);
		for(int i = 1; i < MAX_LEVELS + 1; i++)
			if(worldIn.getBlockState(new BlockPos(pos.getX() + (i * direction.getFrontOffsetX()), pos.getY() + (i * direction.getFrontOffsetY()), pos.getZ() + (i * direction.getFrontOffsetZ())))
					.getBlock() == SecretBlocks.SECRET_GATE_BLOCK)
				worldIn.setBlockToAir(new BlockPos(pos.getX() + (i * direction.getFrontOffsetX()), pos.getY() + (i * direction.getFrontOffsetY()), pos.getZ() + (i * direction.getFrontOffsetZ())));
			else
				break;
	}
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		return new BlockStateContainer(this, POWERED, FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return this.getDefaultState().withProperty(POWERED, false).withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}
	
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }
	
	@Override
	public void onMessageRecieved(World world, BlockPos pos) 
	{
		if(world.isBlockPowered(pos) || world.isBlockIndirectlyGettingPowered(pos) > 0)
			buildGate(world, pos);
	}

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing)state.getValue(FACING)).getIndex();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

}
