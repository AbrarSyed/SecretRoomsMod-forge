package com.wynprice.secretroomsmod.blocks;

import java.util.List;

import com.wynprice.secretroomsmod.base.BaseFakePressurePlate;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecretPressurePlate extends BaseFakePressurePlate
{

	public SecretPressurePlate() {
		super("secret_pressure_plate", Material.WOOD);
	}

	@Override
	protected int getLevel(World world, BlockPos pos, List<Entity> entityList) 
	{
		return entityList.size() > 0 ? 15 : 0;
	}

	@Override
	protected SoundEvent soundOn() {
		return SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON;
	}

	@Override
	protected SoundEvent soundOff() {
		return SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF;
	}

}
