package com.wynprice.secretroomsmod.blocks;

import java.util.Random;

import com.wynprice.secretroomsmod.base.BaseFakeBlock;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class SecretGateEmptyBlock extends BaseFakeBlock
{

	public SecretGateEmptyBlock() {
		super("secret_gate_block", Material.ROCK);
		setHardness(0.1f);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.AIR;
	}

}
