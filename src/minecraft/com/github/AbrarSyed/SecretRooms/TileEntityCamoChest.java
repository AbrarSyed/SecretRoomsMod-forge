package com.github.AbrarSyed.SecretRooms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * @author Alexbegt, AbrarSyed
 */
public class TileEntityCamoChest extends TileEntityCamoFull implements IInventory
{
	private ItemStack[]			chestContents			= new ItemStack[36];

	/** Determines if the check for adjacent chests has taken place. */
	public boolean				adjacentChestChecked	= false;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityCamoChest	adjacentChestZNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityCamoChest	adjacentChestXPos;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityCamoChest	adjacentChestXNeg;

	/** Contains the chest tile located adjacent to this one (if any) */
	public TileEntityCamoChest	adjacentChestZPosition;

	/** The current angle of the lid (between 0 and 1) */
	public float				lidAngle;

	/** The angle of the lid last tick */
	public float				prevLidAngle;

	/** The number of players currently using this chest */
	public int					numUsingPlayers;

	/** Server sync counter (once per 20 ticks) */
	private int					ticksSinceSync;

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return 27;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return chestContents[par1];
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (chestContents[par1] != null)
		{
			ItemStack var3;

			if (chestContents[par1].stackSize <= par2)
			{
				var3 = chestContents[par1];
				chestContents[par1] = null;
				onInventoryChanged();
				return var3;
			}
			else
			{
				var3 = chestContents[par1].splitStack(par2);

				if (chestContents[par1].stackSize == 0)
					chestContents[par1] = null;

				onInventoryChanged();
				return var3;
			}
		}
		else
			return null;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (chestContents[par1] != null)
		{
			ItemStack var2 = chestContents[par1];
			chestContents[par1] = null;
			return var2;
		}
		else
			return null;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		chestContents[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
			par2ItemStack.stackSize = getInventoryStackLimit();

		onInventoryChanged();
	}

	/**
	 * Returns the name of the inventory.
	 */
	@Override
	public String getInvName()
	{
		return "container.Camochest";
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
		chestContents = new ItemStack[getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound) var2.tagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < chestContents.length)
				chestContents[var5] = ItemStack.loadItemStackFromNBT(var4);
		}
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < chestContents.length; ++var3)
			if (chestContents[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				chestContents[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}

		par1NBTTagCompound.setTag("Items", var2);
	}

	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
	{
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this ? false : par1EntityPlayer.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
	}

	/**
	 * Causes the TileEntity to reset all it's cached values for it's container
	 * block, blockID, metaData and in the case of chests, the adjcacent chest
	 * check
	 */
	@Override
	public void updateContainingBlockInfo()
	{
		super.updateContainingBlockInfo();
		adjacentChestChecked = false;
	}

	/**
	 * Performs the check for adjacent chests to determine if this chest is
	 * double or not.
	 */
	public void checkForAdjacentChests()
	{
		if (!adjacentChestChecked)
		{
			adjacentChestChecked = true;
			adjacentChestZNeg = null;
			adjacentChestXPos = null;
			adjacentChestXNeg = null;
			adjacentChestZPosition = null;

			if (worldObj.getBlockId(xCoord - 1, yCoord, zCoord) == SecretRooms.camoChest.blockID)
				adjacentChestXNeg = (TileEntityCamoChest) worldObj.getBlockTileEntity(xCoord - 1, yCoord, zCoord);

			if (worldObj.getBlockId(xCoord + 1, yCoord, zCoord) == SecretRooms.camoChest.blockID)
				adjacentChestXPos = (TileEntityCamoChest) worldObj.getBlockTileEntity(xCoord + 1, yCoord, zCoord);

			if (worldObj.getBlockId(xCoord, yCoord, zCoord - 1) == SecretRooms.camoChest.blockID)
				adjacentChestZNeg = (TileEntityCamoChest) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord - 1);

			if (worldObj.getBlockId(xCoord, yCoord, zCoord + 1) == SecretRooms.camoChest.blockID)
				adjacentChestZPosition = (TileEntityCamoChest) worldObj.getBlockTileEntity(xCoord, yCoord, zCoord + 1);

			if (adjacentChestZNeg != null)
				adjacentChestZNeg.updateContainingBlockInfo();

			if (adjacentChestZPosition != null)
				adjacentChestZPosition.updateContainingBlockInfo();

			if (adjacentChestXPos != null)
				adjacentChestXPos.updateContainingBlockInfo();

			if (adjacentChestXNeg != null)
				adjacentChestXNeg.updateContainingBlockInfo();
		}
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses,
	 * e.g. the mob spawner uses this to count ticks and creates a new spawn
	 * inside its implementation.
	 */
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		checkForAdjacentChests();

		if (++ticksSinceSync % 20 * 4 == 0)
			;

		prevLidAngle = lidAngle;
		float var1 = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null)
		{
			if (adjacentChestZPosition != null)
			{
			}

			if (adjacentChestXPos != null)
			{
			}
		}

		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F)
		{
			float var8 = lidAngle;

			if (numUsingPlayers > 0)
				lidAngle += var1;
			else
				lidAngle -= var1;

			if (lidAngle > 1.0F)
				lidAngle = 1.0F;

			float var3 = 0.5F;

			if (lidAngle < var3 && var8 >= var3 && adjacentChestZNeg == null && adjacentChestXNeg == null)
			{
				if (adjacentChestZPosition != null)
				{
				}

				if (adjacentChestXPos != null)
				{
				}
			}

			if (lidAngle < 0.0F)
				lidAngle = 0.0F;
		}
	}

	/**
	 * Called when a client event is received with the event number and
	 * argument, see World.sendClientEvent
	 */
	@Override
	public void receiveClientEvent(int par1, int par2)
	{
		if (par1 == 1)
			numUsingPlayers = par2;
	}

	@Override
	public void openChest()
	{
		++numUsingPlayers;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SecretRooms.camoChest.blockID, 1, numUsingPlayers);
	}

	@Override
	public void closeChest()
	{
		--numUsingPlayers;
		worldObj.addBlockEvent(xCoord, yCoord, zCoord, SecretRooms.camoChest.blockID, 1, numUsingPlayers);
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate()
	{
		updateContainingBlockInfo();
		checkForAdjacentChests();
		super.invalidate();
	}
}
