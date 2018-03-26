package com.wynprice.secretroomsmod.integration.malisisdoors;

import java.util.List;

import com.google.common.collect.Lists;
import com.wynprice.secretroomsmod.render.fakemodels.FakeBlockModel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import scala.reflect.internal.Trees.Super;

public class MalisisItemTrapDoorModel extends FakeBlockModel {

	public MalisisItemTrapDoorModel() {
		super(getModel(new ResourceLocation("item/arrow")));
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		System.out.println(state);
		return super.getQuads(state, side, rand);
	}
	
	@Override
	public ItemOverrideList getOverrides() {
		return super.getOverrides();
	}
	
}
