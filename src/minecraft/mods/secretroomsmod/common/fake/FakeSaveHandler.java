package mods.secretroomsmod.common.fake;

import java.io.File;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class FakeSaveHandler implements ISaveHandler
{
	private FakeWorldInfo	info;

	public FakeSaveHandler(WorldInfo info)
	{
		this.info = new FakeWorldInfo(info);
	}

	@Override
	public WorldInfo loadWorldInfo()
	{
		return info;
	}

	@Override
	public void checkSessionLock() throws MinecraftException
	{
		// nothing.
	}

	@Override
	public IChunkLoader getChunkLoader(WorldProvider worldprovider)
	{
		return null;
	}

	@Override
	public void saveWorldInfoWithPlayer(WorldInfo worldinfo, NBTTagCompound nbttagcompound)
	{
		// nothing
	}

	@Override
	public void saveWorldInfo(WorldInfo worldinfo)
	{
		// nothing.
	}

	@Override
	public IPlayerFileData getSaveHandler()
	{
		return null;
	}

	@Override
	public void flush()
	{
		// nothing
	}

	@Override
	public File getMapFileFromName(String s)
	{
		return new File(s);
	}

	@Override
	public String getWorldDirectoryName()
	{
		return "fake";
	}

}
