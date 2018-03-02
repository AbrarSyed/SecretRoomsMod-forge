package com.wynprice.secretroomsmod.intergration.malisisdoors;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.base.interfaces.ISecretBlock;
import com.wynprice.secretroomsmod.base.interfaces.ISecretTileEntity;
import com.wynprice.secretroomsmod.handler.ParticleHandler;

import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;

public class SecretBlockIconProvider implements IBlockIconProvider
{

	@Override
	public Icon getIcon(IBlockState state, EnumFacing side) {
		if(state instanceof IExtendedBlockState && state.getBlock() instanceof ISecretBlock && ((IExtendedBlockState)state).getValue(ISecretBlock.RENDER_PROPERTY) != null) {
			return Icon.from(((IExtendedBlockState)state).getValue(ISecretBlock.RENDER_PROPERTY));
		}
		return Icon.from(SecretBlocks.GHOST_BLOCK);
		
	}
	
	@Override
	public Icon getIcon(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) 
	{
		System.out.println("ss");
		if(world.getBlockState(pos).getBlock() instanceof ISecretBlock) {
			return Icon.from(((ISecretTileEntity)world.getTileEntity(pos)).getMirrorState());
		}
		return IBlockIconProvider.super.getIcon(world, pos, state, side);
	}

}
