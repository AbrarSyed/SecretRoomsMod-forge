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
public class TileEntityCamo extends TileEntity
{
	public TileEntityCamo()
	{
		texturePath = "/terrain.png";
	}

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		setTexture(nbttagcompound.getInteger("Texture"));
		forged = nbttagcompound.getBoolean("Forged");

		if (forged)
		{
			setTexturePath(nbttagcompound.getString("TexturePath"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setInteger("Texture", getTexture());
		nbttagcompound.setBoolean("Forged", forged);
		if (forged)
		{
			nbttagcompound.setString("TexturePath", getTexturePath());
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "SRM-TE-Camo";
		packet.isChunkDataPacket = true;

		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(bytes);
		try
		{
			int[] coords = { xCoord, yCoord, zCoord, texture };
			for (int a = 0; a < coords.length; a++)
			{
				data.writeInt(coords[a]);
			}

			data.writeBoolean(forged);

			if (forged)
			{
				data.write(getTexturePath().length());
				data.writeChars(getTexturePath());
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

	public int getTexture()
	{
		return texture;
	}

	public void setTexture(int texture)
	{
		this.texture = texture;
	}

	public String getTexturePath()
	{
		if (forged)
			return texturePath;
		else
			return "/terrain.png";
	}

	public void setTexturePath(String texture)
	{
		if (texture != null && !texture.equals("/terrain.png"))
		{
			forged = true;
			texturePath = texture;
		}
	}

	private int		texture;
	private boolean	forged;
	private String	texturePath;
}
