package mods.SecretRoomsMod.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mods.SecretRoomsMod.common.BlockHolder;
import mods.SecretRoomsMod.common.TileEntityCamoFull;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketSRM0UpdateCamo extends PacketSRMBase
{
	public final int			x, y, z;
	public final BlockHolder	holder;

	public PacketSRM0UpdateCamo(TileEntityCamoFull entity)
	{
		holder = entity.getBlockHolder();
		x = entity.xCoord;
		y = entity.yCoord;
		z = entity.zCoord;
	}

	public PacketSRM0UpdateCamo(ObjectInputStream stream) throws IOException
	{
		x = stream.readInt();
		y = stream.readInt();
		z = stream.readInt();
		
		NBTTagCompound nbt = (NBTTagCompound) NBTBase.readNamedTag(stream);
		holder = BlockHolder.buildFromNBT(nbt);
	}

	@Override
	public void writeToStream(ObjectOutputStream stream) throws IOException
	{
		stream.writeInt(x);
		stream.writeInt(y);
		stream.writeInt(z);
		
		NBTTagCompound nbt = new NBTTagCompound();
		holder.writeToNBT(nbt);
		NBTBase.writeNamedTag(nbt, stream);
	}

	@Override
	public int getID()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayerMP player)
	{
		// nothing...
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player)
	{
		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);

		if (entity == null || holder == null)
			return;

		entity.setBlockHolder(holder);

		world.markBlockForRenderUpdate(x, y, z);
	}

}
