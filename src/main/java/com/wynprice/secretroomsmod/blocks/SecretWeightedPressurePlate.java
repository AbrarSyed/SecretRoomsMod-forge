package com.wynprice.secretroomsmod.blocks;

import java.util.List;

import com.wynprice.secretroomsmod.base.BaseFakePressurePlate;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SecretWeightedPressurePlate extends BaseFakePressurePlate
{
	private final int maxWeight;
	public SecretWeightedPressurePlate(String name, int maxWeight) 
	{
		super(name, Material.IRON);
		this.maxWeight = maxWeight;
	}

	@Override
	protected int getLevel(World world, BlockPos pos, List<Entity> entityList) 
	{
		int i = Math.min(entityList.size(), this.maxWeight);

        if (i > 0)
        {
            float f = (float)Math.min(this.maxWeight, i) / (float)this.maxWeight;
            return MathHelper.ceil(f * 15.0F);
        }
        else
        {
            return 0;
        }
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
