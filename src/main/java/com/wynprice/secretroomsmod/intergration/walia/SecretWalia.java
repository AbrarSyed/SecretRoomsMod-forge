package com.wynprice.secretroomsmod.intergration.walia;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@WailaPlugin
public class SecretWalia implements IWailaPlugin
{

	@Override
	public void register(IWailaRegistrar registrar) 
	{
		for(Block block : ForgeRegistries.BLOCKS.getValues())
		{
			if(!(block instanceof ISecretBlock))
				continue;
			registrar.registerStackProvider(SecretWaliaDataProvider.INSTANCE, block.getClass());
			registrar.registerHeadProvider(SecretWaliaDataProvider.INSTANCE, block.getClass());
			registrar.registerBodyProvider(SecretWaliaDataProvider.INSTANCE, block.getClass());
			registrar.registerTailProvider(SecretWaliaDataProvider.INSTANCE, block.getClass());
		}

	}

}
