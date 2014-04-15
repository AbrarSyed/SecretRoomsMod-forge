package com.github.abrarsyed.secretroomsmod.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoLightDetector extends BlockCamoFull
{
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_DETECTOR);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return world.getBlockMetadata(x, y, z);
	}
	
	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	public void updateLightLevel(World world, int x, int y, int z)
	{
		if (!world.provider.hasNoSky)
		{
			int meta = world.getBlockMetadata(x, y, z);
			int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y+1, z) - world.skylightSubtracted;
			float angle = world.getCelestialAngleRadians(1.0F);

			if (angle < (float) Math.PI)
			{
				angle += (0.0F - angle) * 0.2F;
			}
			else
			{
				angle += (((float) Math.PI * 2F) - angle) * 0.2F;
			}

			lightValue = Math.round((float) lightValue * MathHelper.cos(angle));

			if (lightValue < 0)
			{
				lightValue = 0;
			}

			if (lightValue > 15)
			{
				lightValue = 15;
			}

			if (meta != lightValue)
			{
				world.setBlockMetadataWithNotify(x, y, z, lightValue, 3);
				world.notifyBlockChange(x - 1, y - 1, z+1, this);
				world.notifyBlockChange(x + 1, y - 1, z+1, this);
				world.notifyBlockChange(x - 1, y + 1, z+1, this);
				world.notifyBlockChange(x + 1, y + 1, z+1, this);
				world.notifyBlockChange(x - 1, y - 1, z+1, this);
				world.notifyBlockChange(x + 1, y - 1, z-1, this);
				world.notifyBlockChange(x - 1, y + 1, z-1, this);
				world.notifyBlockChange(x + 1, y + 1, z-1, this);
			}
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World par1World, int meta)
	{
		return new TileEntityCamoDetector();
	}
}
