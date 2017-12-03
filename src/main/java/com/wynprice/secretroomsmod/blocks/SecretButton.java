package com.wynprice.secretroomsmod.blocks;

import java.util.ArrayList;
import java.util.List;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.BlockButton;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SecretButton extends BlockButton implements ISecretBlock
{
	private final boolean wooden;
	
	public SecretButton(boolean wooden) 
	{
		super(wooden);
		this.wooden = wooden;
		setUnlocalizedName("secret_" + (wooden ? "wooden" : "stone") + "_button");
		setRegistryName("secret_" + (wooden ? "wooden" : "stone") + "_button");
		this.setHardness(0.5f);
		this.translucent = true;
    }
	
	@Override
	public Material getMaterial(IBlockState state) {
		return ISecretBlock.super.getMaterial(state, super.getMaterial(state));
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return ISecretBlock.super.canBeConnectedTo(world, pos, facing);
	}
	
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (this.wooden)
            {
                if (!((Boolean)state.getValue(POWERED)).booleanValue())
                {
                    this.checkPressed(state, worldIn, pos);
                }
            }
        }
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return ISecretBlock.super.getBlockFaceShape(worldIn, state, pos, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ISecretBlock.super.isSideSolid(base_state, world, pos, side);
	}
	
	 private void checkPressed(IBlockState state, World worldIn, BlockPos pos)
	 {
		 List <? extends Entity > list = new ArrayList<>();
		 if(worldIn.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)worldIn.getTileEntity(pos)).getMirrorState() != null)
			 list = worldIn.<Entity>getEntitiesWithinAABB(EntityArrow.class, ((ISecretTileEntity)worldIn.getTileEntity(pos)).getMirrorState().getBoundingBox(worldIn, pos).offset(pos));
		 boolean flag = !list.isEmpty();
	     boolean flag1 = ((Boolean)state.getValue(POWERED)).booleanValue();

	     if (flag && !flag1)
	     {
	         worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)));
	         worldIn.notifyNeighborsOfStateChange(pos, this, false);
	         worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)state.getValue(FACING).getOpposite()), this, false);
	         worldIn.markBlockRangeForRenderUpdate(pos, pos);
	         this.playClickSound((EntityPlayer)null, worldIn, pos);
	     }

	     if (!flag && flag1)
	     {
	         worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
	         worldIn.notifyNeighborsOfStateChange(pos, this, false);
	         worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)state.getValue(FACING).getOpposite()), this, false);
	         worldIn.markBlockRangeForRenderUpdate(pos, pos);
	         this.playReleaseSound(worldIn, pos);
	     }

	     if (flag)
	     {
	         worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
	     }
	 }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return ISecretBlock.super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) 
	{
		ISecretBlock.super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return ISecretBlock.super.getSoundType(state, world, pos, entity);
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
    @SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) 
	{
		return ISecretBlock.super.addHitEffects(state, worldObj, target, manager);
	}
		
    @SideOnly(Side.CLIENT)
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) 
	{
		return ISecretBlock.super.addDestroyEffects(world, pos, manager);
	}
	
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getAmbientOcclusionLightValue(IBlockState state)
    {
        return 1.0F;
    }

	@Override
	protected void playClickSound(EntityPlayer player, World worldIn, BlockPos pos) {
        worldIn.playSound(player, pos, wooden ? SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
	}

	@Override
	protected void playReleaseSound(World worldIn, BlockPos pos) {
        worldIn.playSound((EntityPlayer)null, pos, wooden ? SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
	}

}