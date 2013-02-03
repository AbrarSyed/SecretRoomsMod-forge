// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode

package com.github.AbrarSyed.SecretRooms.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

/**
 * @author AbrarSyed
 */
public class TileEntityCamoFull extends TileEntity
{
	public TileEntityCamoFull()
	{
		super();
		coordType = false;
	}

	@Override
	public boolean canUpdate()
	{
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		copyID = nbttagcompound.getInteger("copyID");
		coordType = nbttagcompound.getBoolean("hasCoords");
		setCopyCoordX(nbttagcompound.getInteger("copyCoordX"));
		setCopyCoordY(nbttagcompound.getInteger("copyCoordY"));
		setCopyCoordZ(nbttagcompound.getInteger("copyCoordZ"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("copyID", getCopyID());
		nbttagcompound.setBoolean("hasCoords", coordType);
		nbttagcompound.setInteger("copyCoordX", getCopyCoordX());
		nbttagcompound.setInteger("copyCoordY", getCopyCoordY());
		nbttagcompound.setInteger("copyCoordZ", getCopyCoordZ());
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
		DataOutputStream data = new DataOutputStream(bytes);
		try
		{
			int[] coords = { xCoord, yCoord, zCoord, copyID };
			for (int a = 0; a < coords.length; a++)
				data.writeInt(coords[a]);

			data.writeBoolean(hasCoords());

			if (hasCoords())
			{
				data.writeInt(copyCoordX);
				data.writeInt(copyCoordY);
				data.writeInt(copyCoordZ);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		packet.data = bytes.toByteArray();

		packet.length = packet.data.length;
		return packet;
	}

	public int getCopyCoordX()
	{
		return copyCoordX;
	}

	public void setCopyCoordX(int copyCoordX)
	{
		coordType = true;
		this.copyCoordX = copyCoordX;
	}

	public int getCopyCoordY()
	{
		return copyCoordY;
	}

	public void setCopyCoordY(int copyCoordY)
	{
		coordType = true;
		this.copyCoordY = copyCoordY;
	}

	public int getCopyCoordZ()
	{
		return copyCoordZ;
	}

	public void setCopyCoordZ(int copyCoordZ)
	{
		coordType = true;
		this.copyCoordZ = copyCoordZ;
	}

	public boolean hasCoords()
	{
		return coordType;
	}

	public int getCopyID()
	{
		return copyID;
	}

	public void setCopyID(int copyID)
	{
		this.copyID = copyID;
	}

	private int		copyID;
	private boolean	coordType;
	private int		copyCoordX;
	private int		copyCoordY;
	private int		copyCoordZ;
}
