package com.wynprice.secretroomsmod.intergration;

import com.wynprice.secretroomsmod.base.BaseExposingHelmet;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;

import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.ProbeConfig;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FakeInfoProvider implements IBlockDisplayOverride
{

	@Override
	public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) 
	{
		if(!(blockState.getBlock() instanceof ISecretBlock))
			return false;
		DefaultProbeInfoProvider.showStandardBlockInfo(new ProbeConfig(), mode, probeInfo, player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof BaseExposingHelmet ? 
				blockState : ((ISecretBlock)blockState.getBlock()).getState(world, data.getPos()), player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof BaseExposingHelmet ? 
				blockState.getBlock() : ((ISecretBlock)blockState.getBlock()).getState(world, data.getPos()).getBlock(), world, data.getPos(), player, new IProbeHitData() {
					
					@Override
					public EnumFacing getSideHit() {
						return data.getSideHit();
					}
					
					@Override
					public BlockPos getPos() {
						return data.getPos();
					}
					
					@Override
					public ItemStack getPickBlock() {
						return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof BaseExposingHelmet ? data.getPickBlock() : 
							((ISecretBlock)blockState.getBlock()).getState(world, data.getPos()).getBlock()
								.getPickBlock(((ISecretBlock)blockState.getBlock()).getState(world, data.getPos()), 
										world.rayTraceBlocks(player.getPositionVector(), player.getPositionVector()
												.addVector(player.getLookVec().x * 5, player.getLookVec().y * 5, player.getLookVec().z * 5)), world, data.getPos(), player);
					}
					
					@Override
					public Vec3d getHitVec() {
						return data.getHitVec();
					}
				});
		return true;
	}

}
