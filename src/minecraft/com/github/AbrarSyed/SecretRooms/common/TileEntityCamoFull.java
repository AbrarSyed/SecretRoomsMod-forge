package com.github.AbrarSyed.SecretRooms.common;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
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
		Packet250CustomPayload packet = new Packet250CustomPayload();

		packet.isChunkDataPacket = true;

		packet.channel = "SRM-TE-CamoFull";

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try
		{
			ObjectOutputStream data = new ObjectOutputStream(bytes);
			int[] coords = { xCoord, yCoord, zCoord };
			for (int a = 0; a < coords.length; a++)
			{
				data.writeInt(coords[a]);
			}
			NBTTagCompound nbt = new NBTTagCompound();
			holder.writeToNBT(nbt);
			NBTBase.writeNamedTag(nbt, data);
			data.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		packet.data = bytes.toByteArray();

		packet.length = packet.data.length;
		return packet;
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
