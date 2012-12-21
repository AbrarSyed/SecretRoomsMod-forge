package com.github.AbrarSyed.SecretRooms;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * @author AbrarSyed
 */

public class ItemCamoDoor extends Item
{
	private Material	doorMaterial;

	public ItemCamoDoor(int par1, Material par2Material)
	{
		super(par1);
		doorMaterial = par2Material;
		setCreativeTab(SecretRooms.tab);
		setTextureFile(SecretRooms.textureFile);
		setIconIndex(0);
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
	 */
	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float something1, float something2, float something3)
	{
		if (par7 != 1)
			return false;

		Block block;
		par5++;

		// change based on material.
		if (doorMaterial.equals(Material.iron))
			block = SecretRooms.camoDoorIron;
		else
			block = SecretRooms.camoDoorWood;

		// check permissions
		if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack) || !par2EntityPlayer.canPlayerEdit(par4, par5 + 1, par6, par7, par1ItemStack))
			return false;

		if (!block.canPlaceBlockAt(par3World, par4, par5, par6))
			return false;
		else
		{
			int i = MathHelper.floor_double((par2EntityPlayer.rotationYaw + 180F) * 4F / 360F - 0.5D) & 3;
			placeDoorBlock(par3World, par4, par5, par6, i, block);
			par1ItemStack.stackSize--;
			return true;
		}
	}

	public static void placeDoorBlock(World par0World, int par1, int par2, int par3, int par4, Block par5Block)
	{
		byte byte0 = 0;
		byte byte1 = 0;

		if (par4 == 0)
			byte1 = 1;

		if (par4 == 1)
			byte0 = -1;

		if (par4 == 2)
			byte1 = -1;

		if (par4 == 3)
			byte0 = 1;

		int i = (par0World.isBlockNormalCube(par1 - byte0, par2, par3 - byte1) ? 1 : 0) + (par0World.isBlockNormalCube(par1 - byte0, par2 + 1, par3 - byte1) ? 1 : 0);
		int j = (par0World.isBlockNormalCube(par1 + byte0, par2, par3 + byte1) ? 1 : 0) + (par0World.isBlockNormalCube(par1 + byte0, par2 + 1, par3 + byte1) ? 1 : 0);
		boolean flag = par0World.getBlockId(par1 - byte0, par2, par3 - byte1) == par5Block.blockID || par0World.getBlockId(par1 - byte0, par2 + 1, par3 - byte1) == par5Block.blockID;
		boolean flag1 = par0World.getBlockId(par1 + byte0, par2, par3 + byte1) == par5Block.blockID || par0World.getBlockId(par1 + byte0, par2 + 1, par3 + byte1) == par5Block.blockID;
		boolean flag2 = false;

		if (flag && !flag1)
			flag2 = true;
		else if (j > i)
			flag2 = true;

		par0World.editingBlocks = true;
		par0World.setBlockAndMetadataWithNotify(par1, par2, par3, par5Block.blockID, par4);
		par0World.setBlockAndMetadataWithNotify(par1, par2 + 1, par3, par5Block.blockID, 8 | (flag2 ? 1 : 0));
		par0World.editingBlocks = false;
		par0World.notifyBlocksOfNeighborChange(par1, par2, par3, par5Block.blockID);
		par0World.notifyBlocksOfNeighborChange(par1, par2 + 1, par3, par5Block.blockID);

		if (!par0World.isRemote)
			return;

		par5Block.onBlockAdded(par0World, par1, par2, par3);
		TileEntityCamo entity = (TileEntityCamo) par0World.getBlockTileEntity(par1, par2, par3);
		PacketDispatcher.sendPacketToServer(entity.getDescriptionPacket());
	}
}
