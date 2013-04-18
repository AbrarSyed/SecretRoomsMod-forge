package mods.SecretRoomsMod.common;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;

public class ProxyCommon
{
	private HashMap<Integer, FakeWorld>	fakes;
	private HashSet<String>				awaySet;

	public ProxyCommon()
	{
		fakes = new HashMap<Integer, FakeWorld>();
		awaySet = new HashSet<String>();
	}

	public void loadRenderStuff()
	{
		// client only
	}

	public void loadKeyStuff()
	{
		// client only...
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
	public void onWorldLoad(Unload event)
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
		if (awaySet.contains(username))
		{
			awaySet.remove(username);
		}
		else
		{
			awaySet.add(username);
		}
	}

	public boolean getFaceAway(String username)
	{
		return awaySet.contains(username);
	}
}
