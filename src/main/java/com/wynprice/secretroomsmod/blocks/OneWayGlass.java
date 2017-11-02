package com.wynprice.secretroomsmod.blocks;

import java.util.HashMap;

import com.wynprice.secretroomsmod.base.BaseFakeBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.render.CustomRenderMaps;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.OneWayGlassFakeModel;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OneWayGlass extends BaseFakeBlock implements ISecretBlock
{
	public OneWayGlass() 
	{
		super("one_way_glass", Material.GLASS);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		this.setHardness(0.5f);
		this.translucent = true;
    }
		
	@SideOnly(Side.CLIENT)
	@Override
	public HashMap<Block, HashMap<Integer, IBakedModel>> getMap() {
		return CustomRenderMaps.ONE_WAY_GLASS_RENDER_MAP;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public FakeBlockModel phaseModel(FakeBlockModel model) {
		return new OneWayGlassFakeModel(model);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) 
	{
		return face != state.getValue(BlockDirectional.FACING) ? BlockFaceShape.SOLID : ((ISecretTileEntity)worldIn.getTileEntity(pos)).getMirrorState().getBlockFaceShape(worldIn, pos, face);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).getIndex();
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(BlockDirectional.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 3);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}
