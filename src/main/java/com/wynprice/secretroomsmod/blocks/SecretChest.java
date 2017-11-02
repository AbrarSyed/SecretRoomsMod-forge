package com.wynprice.secretroomsmod.blocks;

import com.wynprice.secretroomsmod.SecretRooms2;
import com.wynprice.secretroomsmod.base.BaseFakeBlock;
import com.wynprice.secretroomsmod.handler.GuiHandler;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretChest;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecretChest extends BaseFakeBlock
{

	public SecretChest(String name) {
		super(name, Material.WOOD);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntitySecretChest();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		for(int i = 0; i < ((TileEntitySecretChest)worldIn.getTileEntity(pos)).getHandler().getSlots(); i++)
			InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileEntitySecretChest)worldIn.getTileEntity(pos)).getHandler().getStackInSlot(i));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		playerIn.openGui(SecretRooms2.instance, GuiHandler.SECRET_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

}
