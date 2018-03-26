package com.wynprice.secretroomsmod.integration.malisisdoors;

import com.wynprice.secretroomsmod.base.BaseBlockDoor;
import com.wynprice.secretroomsmod.base.BaseItemDoor;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.malisis.core.MalisisCore;
import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.item.DoorItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecretMalisiItemDoor extends DoorItem
{
	
	public SecretMalisiItemDoor(DoorDescriptor desc)
	{
		super(desc);
		desc.set(desc.getBlock(), this);
	}

	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{	
		if (side != EnumFacing.UP)
			return EnumActionResult.FAIL;

		ItemStack itemStack = player.getHeldItem(hand);
		IBlockState state = world.getBlockState(pos);

		if (!state.getBlock().isReplaceable(world, pos))
			pos = pos.up();

		state = state.getBlock() instanceof ISecretBlock ? ISecretTileEntity.getMirrorState(world, pos) : state;

		
		Block block = getDescriptor(itemStack).getBlock();
		if (block == null)
		{
			MalisisCore.log.error("Can't place Door : block is null for " + itemStack);
			return EnumActionResult.FAIL;
		}

		if (!player.canPlayerEdit(pos, side, itemStack) || !player.canPlayerEdit(pos.up(), side, itemStack))
			return EnumActionResult.FAIL;

		if (!block.canPlaceBlockAt(world, pos))
			return EnumActionResult.FAIL;

		BaseItemDoor.placeDoor(world, pos, state, EnumFacing.fromAngle(player.rotationYaw), block, false);
		itemStack.shrink(1);
		block.onBlockPlacedBy(world, pos, world.getBlockState(pos), player, itemStack);
		return EnumActionResult.SUCCESS;
	}
}
