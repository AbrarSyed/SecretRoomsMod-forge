package com.wynprice.secretroomsmod.base;

import java.util.ArrayList;
import java.util.HashMap;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.SecretItems;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.ParticleHandler;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketFakeBlockPlaced;
import com.wynprice.secretroomsmod.render.CustomRenderMaps;
import com.wynprice.secretroomsmod.render.fakemodels.DoorFakeModel;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public class BaseBlockDoor extends BlockDoor implements ISecretBlock
{

	public BaseBlockDoor(String name, Material materialIn) {
		super(materialIn);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setHardness(0.5f);
		this.translucent = true;
    }
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this == SecretBlocks.SECRET_IRON_DOOR ? SecretItems.SECRET_IRON_DOOR : SecretItems.SECRET_WOODEN_DOOR);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, java.util.Random rand, int fortune) {
		return this == SecretBlocks.SECRET_IRON_DOOR ? SecretItems.SECRET_IRON_DOOR : SecretItems.SECRET_WOODEN_DOOR;
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
	public IBlockState overrideThisState(World world, BlockPos pos, IBlockState defaultState) {
		return defaultState.getValue(HALF) != EnumDoorHalf.UPPER || world.getBlockState(pos.down()).getBlock() != this ? defaultState : 
			defaultState.withProperty(OPEN, world.getBlockState(pos.down()).getValue(OPEN)).withProperty(FACING, world.getBlockState(pos.down()).getValue(FACING))
			.withProperty(HINGE, world.getBlockState(pos.down()).getValue(HINGE)).withProperty(POWERED, world.getBlockState(pos.down()).getValue(POWERED));
	}
	
	@Override
	public Material getMaterial(IBlockState state) 
	{
		for(WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worlds)
		{
			ArrayList<TileEntity> list = new ArrayList<>(world.loadedTileEntityList);
			for(TileEntity te : list)
				if(world.getBlockState(te.getPos()) == state)
					return ((ISecretTileEntity)world.getTileEntity(te.getPos())).getMirrorState().getMaterial();
		}
		return super.getMaterial(state);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return world.getTileEntity(pos) instanceof ISecretTileEntity && ((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState() != null ? 
				((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState().getBlock().getSoundType() : super.getSoundType(state, world, pos, entity);
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		if (state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER)
        {
            BlockPos blockpos = pos.down();
            IBlockState iblockstate = worldIn.getBlockState(blockpos);

            if (iblockstate.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
            else if (blockIn != this)
            {
                iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
            }
        }
        else
        {
            boolean flag1 = false;
            BlockPos blockpos1 = pos.up();
            IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

            if (iblockstate1.getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;
            }

            if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn,  pos.down(), EnumFacing.UP))
            {
                worldIn.setBlockToAir(pos);
                flag1 = true;

                if (iblockstate1.getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos1);
                }
            }

            if (flag1)
            {
                if (!worldIn.isRemote)
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            else
            {
                boolean flag = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos1);

                if (blockIn != this && (flag || blockIn.getDefaultState().canProvidePower()) && flag != ((Boolean)iblockstate1.getValue(POWERED)).booleanValue())
                {
                    worldIn.setBlockState(blockpos1, iblockstate1.withProperty(POWERED, Boolean.valueOf(flag)), 2);

                    if (flag != ((Boolean)state.getValue(OPEN)).booleanValue())
                    {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
                        worldIn.markBlockRangeForRenderUpdate(pos, pos);
//                        worldIn.playEvent((EntityPlayer)null, flag ? this.getOpenSound() : this.getCloseSound(), pos, 0);
                    }
                }
            }
        }
    }
	
    @SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) 
	{
		if (target.getBlockPos() != null && worldObj.getTileEntity(target.getBlockPos()) instanceof ISecretTileEntity && 
				((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState() != null)
        {
            int i = target.getBlockPos().getX();
            int j = target.getBlockPos().getY();
            int k = target.getBlockPos().getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = ((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState().getBoundingBox(worldObj, target.getBlockPos());
            double d0 = (double)i + new Random().nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
            double d1 = (double)j + new Random().nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
            double d2 = (double)k + new Random().nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
            if (target.sideHit == EnumFacing.DOWN) d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.UP) d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
            if (target.sideHit == EnumFacing.NORTH) d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.SOUTH) d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
            if (target.sideHit == EnumFacing.WEST) d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
            if (target.sideHit == EnumFacing.EAST) d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
            manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
            		.createParticle(0, worldObj, d0, d1, d2, 0, 0, 0, 
            				Block.getStateId(((ISecretTileEntity)worldObj.getTileEntity(target.getBlockPos())).getMirrorState())))
            		.setBlockPos(target.getBlockPos()).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
            return true;
        }
		return false;
	}
		
	@SideOnly(Side.CLIENT)
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) 
	{

		if(ParticleHandler.BLOCKBRAKERENDERMAP.get(pos) != null)
		{
			IBlockState state = ParticleHandler.BLOCKBRAKERENDERMAP.get(pos).getActualState(world, pos);
	        int i = 4;
	        for (int j = 0; j < 4; ++j)
	        {
	            for (int k = 0; k < 4; ++k)
	            {
	                for (int l = 0; l < 4; ++l)
	                {
	                    double d0 = ((double)j + 0.5D) / 4.0D;
	                    double d1 = ((double)k + 0.5D) / 4.0D;
	                    double d2 = ((double)l + 0.5D) / 4.0D;
	                    manager.addEffect(((net.minecraft.client.particle.ParticleDigging)new net.minecraft.client.particle.ParticleDigging.Factory()
	                    		.createParticle(0, world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2,
	                    				d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, Block.getStateId(state))).setBlockPos(pos));
	                }
	            }
	        }
		}
		return super.addDestroyEffects(world, pos, manager);
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
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) 
	{	
		if(worldIn.isRemote && worldIn.getBlockState(net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos())
				.getBlock().isReplaceable(worldIn, net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos()) && 
				!(worldIn.getBlockState(net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock() instanceof IFluidBlock)
				&& !(worldIn.getBlockState(net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos()).getBlock() instanceof BlockLiquid))
			REPLACEABLE_BLOCK_MAP.put(pos, worldIn.getBlockState(net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos()));
		return super.canPlaceBlockOnSide(worldIn, pos, side);
	}
	
	private static final HashMap<BlockPos, IBlockState> REPLACEABLE_BLOCK_MAP = new HashMap<>();
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if(worldIn.isRemote)
		{
			IBlockState blockstate = Blocks.AIR.getDefaultState();
			BlockPos overPosition = new BlockPos(pos);
			if(REPLACEABLE_BLOCK_MAP.containsKey(pos))
			{
				blockstate = REPLACEABLE_BLOCK_MAP.get(pos);
				REPLACEABLE_BLOCK_MAP.remove(pos);
			}
			if(blockstate.getBlock() == Blocks.AIR)
			{
				overPosition = net.minecraft.client.Minecraft.getMinecraft().objectMouseOver.getBlockPos();
				blockstate = worldIn.getBlockState(overPosition);
			}
			SecretNetwork.sendToServer(new MessagePacketFakeBlockPlaced(pos, overPosition, blockstate));
			((ISecretTileEntity)worldIn.getTileEntity(pos)).setMirrorState(blockstate, overPosition);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}