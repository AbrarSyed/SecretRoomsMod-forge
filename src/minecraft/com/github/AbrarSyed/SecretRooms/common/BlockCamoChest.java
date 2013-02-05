package com.github.AbrarSyed.SecretRooms.common;

import static net.minecraftforge.common.ForgeDirection.DOWN;

import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/**
 * @author Alexbegt, AbrarSyed
 */
public class BlockCamoChest extends BlockCamoFull
{
	private Random	random	= new Random();

	protected BlockCamoChest(int par1)
	{
		super(par1);
		setHardness(1.5F);
		setCreativeTab(SecretRooms.tab);
		setLightOpacity(255);
		setTextureFile(SecretRooms.textureFile);
	}

	@Override
	public int getBlockTextureFromSide(int i)
	{
		if (i == 3 || i == 2)
			return 2;

		return 3;
	}

	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		super.onBlockAdded(par1World, par2, par3, par4);
		unifyAdjacentChests(par1World, par2, par3, par4);
		int var5 = par1World.getBlockId(par2, par3, par4 - 1);
		int var6 = par1World.getBlockId(par2, par3, par4 + 1);
		int var7 = par1World.getBlockId(par2 - 1, par3, par4);
		int var8 = par1World.getBlockId(par2 + 1, par3, par4);

		if (var5 == blockID)
			unifyAdjacentChests(par1World, par2, par3, par4 - 1);

		if (var6 == blockID)
			unifyAdjacentChests(par1World, par2, par3, par4 + 1);

		if (var7 == blockID)
			unifyAdjacentChests(par1World, par2 - 1, par3, par4);

		if (var8 == blockID)
			unifyAdjacentChests(par1World, par2 + 1, par3, par4);
	}

	/**
	 * Turns the adjacent chests to a double chest.
	 */
	public void unifyAdjacentChests(World par1World, int par2, int par3, int par4)
	{
		if (!par1World.isRemote)
		{
			int var5 = par1World.getBlockId(par2, par3, par4 - 1);
			int var6 = par1World.getBlockId(par2, par3, par4 + 1);
			int var7 = par1World.getBlockId(par2 - 1, par3, par4);
			int var8 = par1World.getBlockId(par2 + 1, par3, par4);
			int var10;
			int var11;
			byte var13;
			int var14;

			if (var5 != blockID && var6 != blockID)
			{
				if (var7 != blockID && var8 != blockID)
				{
					var13 = 3;

					if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6])
						var13 = 3;

					if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5])
						var13 = 2;

					if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8])
						var13 = 5;

					if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7])
						var13 = 4;
				}
				else
				{
					var10 = par1World.getBlockId(var7 == blockID ? par2 - 1 : par2 + 1, par3, par4 - 1);
					var11 = par1World.getBlockId(var7 == blockID ? par2 - 1 : par2 + 1, par3, par4 + 1);
					var13 = 3;
					if (var7 == blockID)
						var14 = par1World.getBlockMetadata(par2 - 1, par3, par4);
					else
						var14 = par1World.getBlockMetadata(par2 + 1, par3, par4);

					if (var14 == 2)
						var13 = 2;

					if ((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11])
						var13 = 3;

					if ((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var10])
						var13 = 2;
				}
			}
			else
			{
				var10 = par1World.getBlockId(par2 - 1, par3, var5 == blockID ? par4 - 1 : par4 + 1);
				var11 = par1World.getBlockId(par2 + 1, par3, var5 == blockID ? par4 - 1 : par4 + 1);
				var13 = 5;
				if (var5 == blockID)
					var14 = par1World.getBlockMetadata(par2, par3, par4 - 1);
				else
					var14 = par1World.getBlockMetadata(par2, par3, par4 + 1);

				if (var14 == 4)
					var13 = 4;

				if ((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11])
					var13 = 5;

				if ((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var10])
					var13 = 4;
			}

			par1World.setBlockMetadataWithNotify(par2, par3, par4, var13);
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		int var5 = 0;

		if (par1World.getBlockId(par2 - 1, par3, par4) == blockID)
			++var5;

		if (par1World.getBlockId(par2 + 1, par3, par4) == blockID)
			++var5;

		if (par1World.getBlockId(par2, par3, par4 - 1) == blockID)
			++var5;

		if (par1World.getBlockId(par2, par3, par4 + 1) == blockID)
			++var5;

		return var5 > 1 ? false : isThereANeighborChest(par1World, par2 - 1, par3, par4) ? false : isThereANeighborChest(par1World, par2 + 1, par3, par4) ? false : isThereANeighborChest(par1World, par2, par3, par4 - 1) ? false : !isThereANeighborChest(par1World, par2, par3, par4 + 1);
	}

	/**
	 * Checks the neighbor blocks to see if there is a chest there. Args: world, x, y, z
	 */
	private boolean isThereANeighborChest(World par1World, int par2, int par3, int par4)
	{
		return par1World.getBlockId(par2, par3, par4) != blockID ? false : par1World.getBlockId(par2 - 1, par3, par4) == blockID ? true : par1World.getBlockId(par2 + 1, par3, par4) == blockID ? true : par1World.getBlockId(par2, par3, par4 - 1) == blockID ? true : par1World.getBlockId(par2, par3, par4 + 1) == blockID;
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
	 * their own) Args: x, y, z, neighbor blockID
	 */
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
		TileEntityCamoChest var6 = (TileEntityCamoChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var6 != null)
			var6.updateContainingBlockInfo();
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an update, as appropriate
	 */
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		TileEntityCamoChest var7 = (TileEntityCamoChest) par1World.getBlockTileEntity(par2, par3, par4);

		if (var7 != null)
			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
			{
				ItemStack var9 = var7.getStackInSlot(var8);

				if (var9 != null)
				{
					float var10 = random.nextFloat() * 0.8F + 0.1F;
					float var11 = random.nextFloat() * 0.8F + 0.1F;
					EntityItem var14;

					for (float var12 = random.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; par1World.spawnEntityInWorld(var14))
					{
						int var13 = random.nextInt(21) + 10;

						if (var13 > var9.stackSize)
							var13 = var9.stackSize;

						var9.stackSize -= var13;
						var14 = new EntityItem(par1World, par2 + var10, par3 + var11, par4 + var12, new ItemStack(var9.itemID, var13, var9.getItemDamage()));
						float var15 = 0.05F;
						var14.motionX = (float) random.nextGaussian() * var15;
						var14.motionY = (float) random.nextGaussian() * var15 + 0.2F;
						var14.motionZ = (float) random.nextGaussian() * var15;

						if (var9.hasTagCompound())
							var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
					}
				}
			}

		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		Object var10 = par1World.getBlockTileEntity(par2, par3, par4);

		if (var10 == null)
			return true;
		else if (par1World.isBlockSolidOnSide(par2, par3 + 1, par4, DOWN))
			return true;
		else if (isOcelotBlockingChest(par1World, par2, par3, par4))
			return true;
		else if (par1World.getBlockId(par2 - 1, par3, par4) == blockID && (par1World.isBlockSolidOnSide(par2 - 1, par3 + 1, par4, DOWN) || isOcelotBlockingChest(par1World, par2 - 1, par3, par4)))
			return true;
		else if (par1World.getBlockId(par2 + 1, par3, par4) == blockID && (par1World.isBlockSolidOnSide(par2 + 1, par3 + 1, par4, DOWN) || isOcelotBlockingChest(par1World, par2 + 1, par3, par4)))
			return true;
		else if (par1World.getBlockId(par2, par3, par4 - 1) == blockID && (par1World.isBlockSolidOnSide(par2, par3 + 1, par4 - 1, DOWN) || isOcelotBlockingChest(par1World, par2, par3, par4 - 1)))
			return true;
		else if (par1World.getBlockId(par2, par3, par4 + 1) == blockID && (par1World.isBlockSolidOnSide(par2, par3 + 1, par4 + 1, DOWN) || isOcelotBlockingChest(par1World, par2, par3, par4 + 1)))
			return true;
		else
		{
			if (par1World.getBlockId(par2 - 1, par3, par4) == blockID)
				var10 = new InventoryLargeChest("container.CamochestDouble", (TileEntityCamoChest) par1World.getBlockTileEntity(par2 - 1, par3, par4), (IInventory) var10);

			if (par1World.getBlockId(par2 + 1, par3, par4) == blockID)
				var10 = new InventoryLargeChest("container.CamochestDouble", (IInventory) var10, (TileEntityCamoChest) par1World.getBlockTileEntity(par2 + 1, par3, par4));

			if (par1World.getBlockId(par2, par3, par4 - 1) == blockID)
				var10 = new InventoryLargeChest("container.CamochestDouble", (TileEntityCamoChest) par1World.getBlockTileEntity(par2, par3, par4 - 1), (IInventory) var10);

			if (par1World.getBlockId(par2, par3, par4 + 1) == blockID)
				var10 = new InventoryLargeChest("container.CamochestDouble", (IInventory) var10, (TileEntityCamoChest) par1World.getBlockTileEntity(par2, par3, par4 + 1));

			if (par1World.isRemote)
				return true;
			else
			{
				par5EntityPlayer.displayGUIChest((IInventory) var10);
				return true;
			}
		}
	}

	/**
	 * each class overrdies this to return a new <className>
	 */
	@Override
	public TileEntity createNewTileEntity(World par1World)
	{
		return new TileEntityCamoChest();
	}

	/**
	 * Looks for a sitting ocelot within certain bounds. Such an ocelot is considered to be blocking access to the
	 * chest.
	 */
	public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3)
	{
		Iterator var4 = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(par1, par2 + 1, par3, par1 + 1, par2 + 2, par3 + 1)).iterator();
		EntityOcelot var6;

		do
		{
			if (!var4.hasNext())
				return false;

			EntityOcelot var5 = (EntityOcelot) var4.next();
			var6 = var5;
		} while (!var6.isSitting());

		return true;
	}

}
