package mods.SecretRoomsMod.common;

import java.util.HashMap;
import java.util.HashSet;

import cpw.mods.fml.common.event.FMLServerStoppingEvent;

import mods.SecretRoomsMod.common.fake.FakeWorld;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class ProxyCommon
{
	private HashMap<Integer, FakeWorld>	fakes;
	private HashSet<String>				towardSet;

	public ProxyCommon()
	{
		fakes = new HashMap<Integer, FakeWorld>();
		towardSet = new HashSet<String>();
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
		towardSet.clear();
	}

	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void onWorldLoad(Load event)
	{
		if (event.world instanceof FakeWorld)
			return;
		int dim = event.world.provider.dimensionId;
		fakes.put(dim, FakeWorld.getFakeWorldFor(event.world));
	}

	@ForgeSubscribe(priority = EventPriority.LOWEST)
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

	public void onKeyPress(String username)
	{
		if (towardSet.contains(username))
		{
			towardSet.remove(username);
		}
		else
		{
			towardSet.add(username);
		}
	}

	public boolean getFaceAway(String username)
	{
		return towardSet.contains(username);
	}
}
