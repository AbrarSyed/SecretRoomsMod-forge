package com.wynprice.secretroomsmod.handler;

import java.util.HashMap;

import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Used to add walking particles
 * @author Wyn Price
 *
 */
@Deprecated
public class ParticleHandler 
{
	/**
	 * Used to keep info of where blocks are being broken, as the tile-entity will no longer exist
	 */
	public static final HashMap<BlockPos, IBlockState> BLOCKBRAKERENDERMAP = new HashMap<>();
}
