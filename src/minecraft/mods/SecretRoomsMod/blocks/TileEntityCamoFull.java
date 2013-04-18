package mods.SecretRoomsMod.blocks;

import mods.SecretRoomsMod.SecretRooms;
import mods.SecretRoomsMod.common.BlockHolder;
import mods.SecretRoomsMod.common.FakeWorld;
import mods.SecretRoomsMod.network.PacketSRM0UpdateCamo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCamoFull extends TileEntity
{
	private BlockHolder	holder;

	public TileEntityCamoFull()
	{
		super();
		holder = null;
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		// backwards compat...
		holder = BlockHolder.buildFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		if (holder != null)
		{
			holder.writeToNBT(nbttagcompound);
		}
	}

	/**
	 * signs and mobSpawners use this to send text and meta-data
	 */
	@Override
	public Packet getDescriptionPacket()
	{
		return new PacketSRM0UpdateCamo(this).getPacket250();
	}

	public void setBlockHolder(BlockHolder holder)
	{
		if (holder == null)
			return;
		FakeWorld fake = SecretRooms.proxy.getFakeWorld(worldObj);
		if (fake != null)
		{
			fake.addOverrideBlock(xCoord, yCoord, zCoord, holder);
		}

		this.holder = holder;
		worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
	}

	/**
	 * invalidates a tile entity
	 */
	@Override
	public void invalidate()
	{
		FakeWorld fake = SecretRooms.proxy.getFakeWorld(worldObj);
		if (fake != null)
		{
			fake.removeOverrideBlock(xCoord, yCoord, zCoord);
		}
		super.invalidate();
	}

	@Override
	public void validate()
	{
		SecretRooms.proxy.getFakeWorld(worldObj).addOverrideBlock(xCoord, yCoord, zCoord, holder);
	}

	public BlockHolder getBlockHolder()
	{
		return holder;
	}

	public int getCopyID()
	{
		return holder == null ? 0 : holder.blockID;
	}
}
