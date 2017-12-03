package com.wynprice.secretroomsmod.blocks;

import java.util.List;

import com.wynprice.secretroomsmod.SecretRooms5;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightFaceDiffrentModel;
import com.wynprice.secretroomsmod.render.fakemodels.TrueSightModel;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretDispenser;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SecretDispenser extends BlockDispenser implements ISecretBlock
{
	public SecretDispenser() {
		this.setUnlocalizedName("secret_dispenser");
		this.setRegistryName("secret_dispenser");
		this.setHardness(0.5f);
		this.translucent = true;
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySecretDispenser();
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public TrueSightModel phaseTrueModel(TrueSightModel model) {
		return new TrueSightFaceDiffrentModel(model, FakeBlockModel.getModel(new ResourceLocation(SecretRooms5.MODID, "block/ghost_block")));
	}
	
	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return ISecretBlock.super.canBeConnectedTo(world, pos, facing);
	}
	
	@Override
	public Material getMaterial(IBlockState state) 
	{
		return ISecretBlock.super.getMaterial(state, super.getMaterial(state));
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(pos.up());
		boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();
		NBTTagCompound compound = null;
	    if(flag != flag1)
	    	compound = worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound());
	    if (flag && !flag1)
	    {
	        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
	        worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
	    }
	    else if (!flag && flag1)
	        worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
	    if(flag != flag1)
	    	worldIn.getTileEntity(pos).readFromNBT(compound);
	    	
    }
	
	@Override
	protected void dispense(World worldIn, BlockPos pos) 
	{
		BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();
        if (tileentitydispenser != null)
        {
            int i = tileentitydispenser.getDispenseSlot();
            if (i < 0)
            {
                worldIn.playEvent(1001, pos, 0);
            }
            else
            {
                ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);

                if (ibehaviordispenseitem != IBehaviorDispenseItem.DEFAULT_BEHAVIOR)
                {
                    tileentitydispenser.setInventorySlotContents(i, ibehaviordispenseitem.dispense(blocksourceimpl, itemstack));
                }
            }
        }
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		return ISecretBlock.super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return ISecretBlock.super.getBlockFaceShape(worldIn, state, pos, face);
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ISecretBlock.super.isSideSolid(base_state, world, pos, side);
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
}
