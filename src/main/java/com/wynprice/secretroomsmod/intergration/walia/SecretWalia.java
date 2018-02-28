package com.wynprice.secretroomsmod.intergration.walia;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;

/**
 * Waila / Hwyla plugin handler
 * @author Wyn Price
 *
 */
@WailaPlugin
public class SecretWalia implements IWailaPlugin
{
	
	/**
	 * Register the Waila/Hwyla plugin
	 */
	@Override
	public void register(IWailaRegistrar registrar) 
	{
		registrar.registerStackProvider(SecretWaliaDataProvider.INSTANCE, Block.class);
	}

}
