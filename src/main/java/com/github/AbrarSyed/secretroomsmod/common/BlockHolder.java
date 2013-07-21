package com.github.AbrarSyed.secretroomsmod.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Tiny class that is used to contain the information of a block
 * @author AbrarSyed
 */
public class BlockHolder
{
	private final NBTTagCompound	nbt;
	public int						blockID;
	public final int				metadata;

	public BlockHolder(IBlockAccess world, int x, int y, int z)
	{
		blockID = world.getBlockId(x, y, z);
		metadata = world.getBlockMetadata(x, y, z);

		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te != null)
		{
			nbt = new NBTTagCompound();
			te.writeToNBT(nbt);

			// strip location
			nbt.setInteger("x", 0);
			nbt.setInteger("y", 0);
			nbt.setInteger("z", 0);

		}
		else
		{
			nbt = null;
		}
	}

	public BlockHolder(int ID, int meta, NBTTagCompound nbt)
	{
		this.nbt = nbt;
		blockID = ID;
		metadata = meta;
	}

	/**
	 * Constructs a TileEntity from this block and loads it from the NBT data.
	 * @return Tile Entity for this block
	 */
	public TileEntity getTileEntity(World world, int x, int y, int z)
	{
		if (blockID == 0 || nbt == null)
			return null;

		TileEntity te = TileEntity.createAndLoadEntity(nbt);

		te.worldObj = world;
		te.xCoord = x;
		te.yCoord = y;
		te.zCoord = z;
		return te;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("copyID", blockID);
		compound.setInteger("copyMeta", metadata);

		compound.setBoolean("hasCopyTE", nbt != null);

		if (nbt != null)
		{
			compound.setCompoundTag("copyTE", nbt);
		}
	}

	@Override
	public boolean equals(Object equals)
	{
		return false;
	}

	public boolean equals(BlockHolder holder)
	{
		if (holder == null)
			return false;

		boolean compound = false;
		if (nbt == null || holder.nbt == null)
		{
			compound = nbt == holder.nbt;
		}
		else
		{
			compound = nbt.equals(holder.nbt);
		}

		if (blockID == holder.blockID &&
				metadata == holder.metadata &&
				compound)
			return true;

		return false;
	}

	public static BlockHolder buildFromNBT(NBTTagCompound nbt)
	{
		int ID = nbt.getInteger("copyID");
		int meta = nbt.getInteger("copyMeta");

		NBTTagCompound nbtNew = null;
		boolean hasNBT = nbt.getBoolean("hasCopyTE");

		if (hasNBT)
		{
			nbtNew = nbt.getCompoundTag("copyTE");
		}

		return new BlockHolder(ID, meta, nbtNew);
	}
}
