package com.wynprice.secretroomsmod.blocks;

import java.util.HashMap;

import com.wynprice.secretroomsmod.base.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.handler.ParticleHandler;
import com.wynprice.secretroomsmod.network.SecretNetwork;
import com.wynprice.secretroomsmod.network.packets.MessagePacketFakeBlockPlaced;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.util.Random;

public class SecretRedstone extends BlockRedstoneWire implements ISecretBlock
{
	public SecretRedstone() {
		setRegistryName("secret_redstone");
		setUnlocalizedName("secret_redstone");
		this.setHardness(0.5f);
		this.translucent = true;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
	{
		if(source.getTileEntity(pos) instanceof TileEntityInfomationHolder && ((TileEntityInfomationHolder)source.getTileEntity(pos)).getMirrorState() != null)
			return ((TileEntityInfomationHolder)source.getTileEntity(pos)).getMirrorState().getBoundingBox(source, pos);
	    return FULL_BLOCK_AABB;
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) 
	{
		return world.getTileEntity(pos) instanceof TileEntityInfomationHolder && ((TileEntityInfomationHolder)world.getTileEntity(pos)).getMirrorState() != null ? 
				((TileEntityInfomationHolder)world.getTileEntity(pos)).getMirrorState().getBlock().getSoundType() : super.getSoundType(state, world, pos, entity);
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return ((TileEntityInfomationHolder)worldIn.getTileEntity(pos)).getMirrorState().getBlockFaceShape(worldIn, pos, face);
	}
	
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) 
	{
		if (target.getBlockPos() != null && worldObj.getTileEntity(target.getBlockPos()) instanceof TileEntityInfomationHolder && 
				((TileEntityInfomationHolder)worldObj.getTileEntity(target.getBlockPos())).getMirrorState() != null)
        {
            int i = target.getBlockPos().getX();
            int j = target.getBlockPos().getY();
            int k = target.getBlockPos().getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = ((TileEntityInfomationHolder)worldObj.getTileEntity(target.getBlockPos())).getMirrorState().getBoundingBox(worldObj, target.getBlockPos());
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
            				Block.getStateId(((TileEntityInfomationHolder)worldObj.getTileEntity(target.getBlockPos())).getMirrorState())))
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

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, java.util.Random rand) {
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
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return true;
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
			((TileEntityInfomationHolder)worldIn.getTileEntity(pos)).setMirrorState(blockstate, overPosition);
		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}

