package com.github.abrarsyed.secretroomsmod.api;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

/**
 * Tiny class that is used to contain the information of a block
 * @author AbrarSyed
 */
public class BlockHolder
{
	private final NBTTagCompound	nbt;
	public Block		    block;
	public final int				metadata;

	public BlockHolder(IBlockAccess world, int x, int y, int z)
	{
		block = world.getBlock(x, y, z);
		metadata = world.getBlockMetadata(x, y, z);

		TileEntity te = world.getTileEntity(x, y, z);
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

	public BlockHolder(Block block, int meta, NBTTagCompound nbt)
	{
		this.nbt = nbt;
		this.block = block;
		metadata = meta;
	}

	/**
	 * Constructs a TileEntity from this block and loads it from the NBT data.
	 * @return Tile Entity for this block
	 */
	public TileEntity getTileEntity(World world, int x, int y, int z)
	{
		if (block == null || nbt == null)
			return null;

		TileEntity te = TileEntity.createAndLoadEntity(nbt);

		te.setWorldObj(world);
		te.xCoord = x;
		te.yCoord = y;
		te.zCoord = z;
		return te;
	}

	public void writeToNBT(NBTTagCompound compound)
	{
	    UniqueIdentifier ident = GameRegistry.findUniqueIdentifierFor(block);
		compound.setString("copyMod", ident.modId);
		compound.setString("copyBlock", ident.name);
		compound.setInteger("copyMeta", metadata);

		compound.setBoolean("hasCopyTE", nbt != null);

		if (nbt != null)
		{
			compound.setTag("copyTE", nbt);
		}
	}

	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((block == null) ? 0 : block.hashCode());
        result = prime * result + metadata;
        result = prime * result + ((nbt == null) ? 0 : nbt.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlockHolder other = (BlockHolder) obj;
        if (block == null)
        {
            if (other.block != null)
                return false;
        }
        else if (!block.equals(other.block))
            return false;
        if (metadata != other.metadata)
            return false;
        if (nbt == null)
        {
            if (other.nbt != null)
                return false;
        }
        else if (!nbt.equals(other.nbt))
            return false;
        return true;
    }

	public static BlockHolder buildFromNBT(NBTTagCompound nbt)
	{
	    Block block = null;
	    if (nbt.hasKey("copyID"))
	    {
	        block = Block.getBlockById(nbt.getInteger("copyID"));
	    }
	    else
	    {
	        block = GameRegistry.findBlock(nbt.getString("copyMod"), nbt.getString("copyBlock"));
	    }
	    
		int meta = nbt.getInteger("copyMeta");

		NBTTagCompound nbtNew = null;
		boolean hasNBT = nbt.getBoolean("hasCopyTE");

		if (hasNBT)
		{
			nbtNew = nbt.getCompoundTag("copyTE");
		}

		return new BlockHolder(block, meta, nbtNew);
	}
	
	public Block getBlock()
	{
	    return block;
	}
}
