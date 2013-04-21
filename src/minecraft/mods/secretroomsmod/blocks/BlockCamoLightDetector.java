package mods.secretroomsmod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCamoLightDetector extends BlockCamoFull
{

	public BlockCamoLightDetector(int par1)
	{
		super(par1);
	}

	public BlockCamoLightDetector(int par1, Material material)
	{
		super(par1, material);
	}

	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return world.getBlockMetadata(x, y, z);
	}

	public void updateLightLevel(World world, int x, int y, int z)
	{
		if (!world.provider.hasNoSky)
		{
			int l = world.getBlockMetadata(x, y, z);
			int i1 = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
			float f = world.getCelestialAngleRadians(1.0F);

			if (f < (float) Math.PI)
			{
				f += (0.0F - f) * 0.2F;
			}
			else
			{
				f += (((float) Math.PI * 2F) - f) * 0.2F;
			}

			i1 = Math.round((float) i1 * MathHelper.cos(f));

			if (i1 < 0)
			{
				i1 = 0;
			}

			if (i1 > 15)
			{
				i1 = 15;
			}

			if (l != i1)
			{
				world.setBlockMetadataWithNotify(x, y, z, i1, 3);
			}
		}
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the block.
	 */
	@Override
	public TileEntity createNewTileEntity(World par1World)
	{
		return new TileEntityCamoDetector();
	}
}
