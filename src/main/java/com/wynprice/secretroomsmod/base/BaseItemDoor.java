package com.wynprice.secretroomsmod.base;

import com.wynprice.secretroomsmod.SecretRooms2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BaseItemDoor extends Item
{
	private final BaseBlockDoor door;
	private final ItemDoor itemDoor;
	
	public BaseItemDoor(BaseBlockDoor door, String name)
	{
		this.door = door;
		this.itemDoor = new ItemDoor(door);
		setUnlocalizedName(name);
		setRegistryName(SecretRooms2.MODID, name);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		boolean isreplacable = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
		IBlockState oldReplaceState = worldIn.getBlockState(pos);
		EnumActionResult result = itemDoor.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		if(result == EnumActionResult.SUCCESS)
		{
			BlockPos basePos = isreplacable ? pos.offset(facing.getOpposite()) : pos;
			((TileEntityInfomationHolder)worldIn.getTileEntity(basePos.up(1))).setMirrorState(isreplacable ? oldReplaceState : worldIn.getBlockState(basePos), basePos);
			((TileEntityInfomationHolder)worldIn.getTileEntity(basePos.up(2))).setMirrorState(isreplacable ? oldReplaceState : worldIn.getBlockState(basePos), basePos);
		}
		return result;
	}
}
