package com.github.AbrarSyed.secretroomsmod.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ProxyCommon
{
	private HashMap<Integer, FakeWorld>	fakes;
	private HashSet<UUID>				awaySet;

	public ProxyCommon()
	{
		fakes = new HashMap<Integer, FakeWorld>();
		awaySet = new HashSet<UUID>();
	}

	public void loadRenderStuff()
	{
		// client only
	}

	public void loadKeyStuff()
	{
		// client only...
	}

	public void onServerStop(FMLServerStoppingEvent e)
	{
		fakes.clear();
		awaySet.clear();
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(Load event)
	{
		int dim = event.world.provider.dimensionId;
		fakes.put(dim, new FakeWorld(event.world));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldUnLoad(Unload event)
	{
		int dim = event.world.provider.dimensionId;
		fakes.remove(dim);
	}

	public FakeWorld getFakeWorld(World world)
	{
		int dim = world.provider.dimensionId;
		return fakes.get(dim);
	}

	public void onKeyPress(UUID uuid)
	{
		if (awaySet.contains(uuid))
		{
			awaySet.remove(uuid);
		}
		else
		{
			awaySet.add(uuid);
		}
	}

	public boolean getFaceTowards(UUID uuid)
	{
		return !awaySet.contains(uuid);
	}
}
