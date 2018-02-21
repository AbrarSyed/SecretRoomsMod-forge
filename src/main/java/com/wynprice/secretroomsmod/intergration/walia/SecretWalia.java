package com.wynprice.secretroomsmod.intergration.walia;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;

@WailaPlugin
public class SecretWalia implements IWailaPlugin
{

	@Override
	public void register(IWailaRegistrar registrar) 
	{
		registrar.registerStackProvider(SecretWaliaDataProvider.INSTANCE, Block.class);
	}

}
