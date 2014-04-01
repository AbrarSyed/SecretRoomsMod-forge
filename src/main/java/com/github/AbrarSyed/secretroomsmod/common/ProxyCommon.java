package com.github.AbrarSyed.secretroomsmod.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

import com.github.AbrarSyed.secretroomsmod.common.fake.FakeWorld;

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
		if (event.world instanceof FakeWorld)
			return;
		int dim = event.world.provider.dimensionId;
		fakes.put(dim, FakeWorld.getFakeWorldFor(event.world));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onWorldUnLoad(Unload event)
	{
		if (event.world instanceof FakeWorld)
			return;

		int dim = event.world.provider.dimensionId;
		fakes.remove(dim);
	}

	public FakeWorld getFakeWorld(World world)
	{
		int dim = world.provider.dimensionId;
		return fakes.get(dim);
	}

	public void onKeyPress(UUID uid)
	{
		if (awaySet.contains(uid))
		{
			awaySet.remove(uid);
		}
		else
		{
			awaySet.add(uid);
		}
	}

	public boolean getFaceTowards(String username)
	{
		return !awaySet.contains(username);
	}
}
