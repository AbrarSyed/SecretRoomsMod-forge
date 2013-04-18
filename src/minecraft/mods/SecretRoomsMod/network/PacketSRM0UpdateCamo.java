package mods.SecretRoomsMod.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mods.SecretRoomsMod.blocks.TileEntityCamoFull;
import mods.SecretRoomsMod.common.BlockHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSRM0UpdateCamo extends PacketSRMBase
{
	public final int			x, y, z;
	public final BlockHolder	holder;
	public final boolean		hasHolder;

	public PacketSRM0UpdateCamo(TileEntityCamoFull entity)
	{
		holder = entity.getBlockHolder();
		hasHolder = holder != null;
		x = entity.xCoord;
		y = entity.yCoord;
		z = entity.zCoord;
	}

	public PacketSRM0UpdateCamo(ObjectInputStream stream) throws IOException
	{
		x = stream.readInt();
		y = stream.readInt();
		z = stream.readInt();

		hasHolder = stream.readBoolean();

		if (hasHolder)
		{
			NBTTagCompound nbt = (NBTTagCompound) NBTBase.readNamedTag(stream);
			holder = BlockHolder.buildFromNBT(nbt);
		}
		else
			holder = null;
	}

	@Override
	public void writeToStream(ObjectOutputStream stream) throws IOException
	{
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);

		stream.writeBoolean(hasHolder);

		if (hasHolder)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			holder.writeToNBT(nbt);
			NBTBase.writeNamedTag(nbt, stream);
		}
	}

	@Override
	public int getID()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player)
	{
		// nothing...
	}

	@Override
	public void actionServer(World world, EntityPlayer player)
	{
		if (world == null)
			return;

		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);

		if (entity == null || holder == null)
			return;

		entity.setBlockHolder(holder);

		world.markBlockForRenderUpdate(x, y, z);
	}

}
