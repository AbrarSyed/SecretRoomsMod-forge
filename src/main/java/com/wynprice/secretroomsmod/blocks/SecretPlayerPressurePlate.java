package com.wynprice.secretroomsmod.blocks;

import java.util.List;

import com.wynprice.secretroomsmod.base.BaseFakePressurePlate;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SecretPlayerPressurePlate extends BaseFakePressurePlate {

	public SecretPlayerPressurePlate() 
	{
		super("secret_player_pressure_plate", Material.IRON);
	}

	@Override
	protected int getLevel(World world, BlockPos pos, List<Entity> entityList)
	{
		for(Entity e : entityList) 
			if(e instanceof EntityPlayer)
				return 15;
		return 0;
	}

	@Override
	protected SoundEvent soundOn() {
		return SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON;
	}

	@Override
	protected SoundEvent soundOff() {
		return SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF;
	}

}
