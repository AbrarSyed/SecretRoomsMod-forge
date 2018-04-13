package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import scala.collection.parallel.ParIterableLike.Min;

public class SecretItemBlock extends ItemBlock
{

	public SecretItemBlock(Block block) {
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		IBlockState mirrorState = worldIn.getBlockState(pos).getBlock() instanceof ISecretBlock ? ISecretTileEntity.getMirrorState(worldIn, pos) : worldIn.getBlockState(pos);
		IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity)null))
        {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

            if(!mirrorState.isNormalCube() && SecretConfig.BLOCK_FUNCTIONALITY.onlyFullBlocks) {
    			mirrorState = Blocks.STONE.getDefaultState();
    		}
            
            ISecretTileEntity.getMap(worldIn).put(pos, mirrorState);
            
            if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
            	if(worldIn.getTileEntity(pos) instanceof ISecretTileEntity) {
            		((ISecretTileEntity)worldIn.getTileEntity(pos)).setMirrorState(mirrorState);
            	}
                iblockstate1 = worldIn.getBlockState(pos);
                SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                itemstack.shrink(1);
            } else {
                ISecretTileEntity.getMap(worldIn).remove(pos);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
	}
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, IBlockState newState) {
		boolean flag =  super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
		if(nbttagcompound != null) {
			TileEntity tileentity =  world.getTileEntity(pos);
			if(tileentity != null) {
				NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
		        NBTTagCompound nbttagcompound2 = nbttagcompound1.copy();
		        nbttagcompound1.merge(nbttagcompound);
		        nbttagcompound1.setInteger("x", pos.getX());
		        nbttagcompound1.setInteger("y", pos.getY());
		        nbttagcompound1.setInteger("z", pos.getZ());
		        if (!nbttagcompound1.equals(nbttagcompound2)) {
	                tileentity.readFromNBT(nbttagcompound1);
		        }
			}
		}
		return flag;
	}

}
