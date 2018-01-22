package com.wynprice.secretroomsmod.handler;

import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ReloadTrueSightModelsHandler 
{
	
	boolean lastTickHelmet;
	
	int updated = 0;
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.player instanceof EntityPlayerSP && (updated++ == 20 || event.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet != lastTickHelmet))
		{
			lastTickHelmet = event.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet;
			for(TileEntity holder : ISecretBlock.ALL_SECRET_TILE_ENTITIES)
				if(holder instanceof ISecretTileEntity)
					event.player.world.markBlockRangeForRenderUpdate(holder.getPos().add(-1, -1, -1), holder.getPos().add(1, 1, 1));
			if(EnergizedPasteHandler.getEnergized_map().get(event.player.world.provider.getDimension()) != null)
				for(BlockPos pos : EnergizedPasteHandler.getEnergized_map().get(event.player.world.provider.getDimension()).keySet())
					event.player.world.markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));

		}
	}
}
