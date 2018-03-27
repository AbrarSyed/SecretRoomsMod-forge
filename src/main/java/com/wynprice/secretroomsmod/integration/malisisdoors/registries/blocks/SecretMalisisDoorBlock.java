package com.wynprice.secretroomsmod.integration.malisisdoors.registries.blocks;

import java.util.Collection;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.integration.malisisdoors.SecretBlockIconProvider;
import com.wynprice.secretroomsmod.integration.malisisdoors.SecretBlockIconProvider.BlockType;
import com.wynprice.secretroomsmod.integration.malisisdoors.malisisrenders.SecretDoorRenderer;
import com.wynprice.secretroomsmod.integration.malisisdoors.malisisrenders.SecretMalisisItemRenderer;
import com.wynprice.secretroomsmod.integration.malisisdoors.registries.tileentities.SecretMalisisTileEntityDoor;
import com.wynprice.secretroomsmod.render.fakemodels.DoorFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.malisis.core.renderer.MalisisRendered;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.DoorDescriptor.RedstoneBehavior;
import net.malisis.doors.block.Door;
import net.malisis.doors.renderer.DoorRenderer;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MalisisRendered(block = SecretDoorRenderer.class, item = SecretMalisisItemRenderer.class)
public class SecretMalisisDoorBlock extends Door implements ISecretBlock
{

	public SecretMalisisDoorBlock(DoorDescriptor desc) {
		super(desc);
		desc.set(this, null);
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (isTop(state))
			return ISecretBlock.super.createNewTileEntity(world, getMetaFromState(state));
		return new SecretMalisisTileEntityDoor();
	}
	
	@Override
	protected IIconProvider getIconProvider() {
		return new SecretBlockIconProvider(SecretBlockIconProvider.BlockType.DOOR);
	}
	
	@Override
	protected ItemStack getDoorItemStack(IBlockAccess world, BlockPos pos) {
		return new ItemStack(this == SecretBlocks.SECRET_IRON_DOOR ? SecretItems.SECRET_IRON_DOOR : SecretItems.SECRET_WOODEN_DOOR);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, java.util.Random rand, int fortune) {
		return state.getValue(HALF) == EnumDoorHalf.LOWER ? this == SecretBlocks.SECRET_IRON_DOOR ? SecretItems.SECRET_IRON_DOOR : SecretItems.SECRET_WOODEN_DOOR : Items.AIR;
	}
	
	@Override
	public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return getState(world, pos).getBlock().canBeConnectedTo(world, pos, facing);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public FakeBlockModel phaseModel(FakeBlockModel model) {
		return new DoorFakeModel(model);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return ISecretBlock.super.getSoundType(state, world, pos, entity);
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
        return EnumBlockRenderType.MODEL;
    }
   
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)  {
		return ISecretBlock.super.getBlockHardness(blockState, worldIn, pos);
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
    {
		return ISecretBlock.super.canRenderInLayer(state, layer);
    }
    
    @Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) 
	{
		return super.getExtendedState(ISecretBlock.super.getExtendedState(state, world, pos), world, pos);
	}
    
    @Override
    protected BlockStateContainer createBlockState() {
    	Collection < IProperty<? >> properties = super.createBlockState().getProperties();
    	return new ExtendedBlockState(this, properties.toArray(new IProperty[properties.size()]), new IUnlistedProperty[] {RENDER_PROPERTY});
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    	return ISecretBlock.super.getActualState(state, worldIn, pos, super.getActualState(state, worldIn, pos));
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
    	return true;
    }
	
}
