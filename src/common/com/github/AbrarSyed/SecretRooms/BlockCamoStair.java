package com.github.AbrarSyed.SecretRooms;

import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

/**
 * @author alexbegt
 */
public class BlockCamoStair extends BlockCamoFull
{

	protected BlockCamoStair(int par1)
	{
		super(par1);
		setHardness(1.5F);
		setLightOpacity(15);
		setTextureFile(SecretRooms.textureFile);
		blockIndexInTexture = 4;
	}

	private static final int[][]	stairDirections	= new int[][] { { 2, 6 }, { 3, 7 }, { 2, 3 }, { 6, 7 }, { 0, 4 }, { 1, 5 }, { 0, 1 }, { 4, 5 } };
	private boolean					field_72156_cr	= false;

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Sets teh special colliding bounding box necessary for stairs
	 */
	@Override
	public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity)
	{
		int var8 = world.getBlockMetadata(x, y, z);
		int var9 = var8 & 3;
		float var10 = 0.0F;
		float var11 = 0.5F;
		float var12 = 0.5F;
		float var13 = 1.0F;

		if ((var8 & 4) != 0)
		{
			var10 = 0.5F;
			var11 = 1.0F;
			var12 = 0.0F;
			var13 = 0.5F;
		}

		setBlockBounds(0.0F, var10, 0.0F, 1.0F, var11, 1.0F);
		super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);

		if (var9 == 0)
		{
			setBlockBounds(0.5F, var12, 0.0F, 1.0F, var13, 1.0F);
			super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
		else if (var9 == 1)
		{
			setBlockBounds(0.0F, var12, 0.0F, 0.5F, var13, 1.0F);
			super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
		else if (var9 == 2)
		{
			setBlockBounds(0.0F, var12, 0.5F, 1.0F, var13, 1.0F);
			super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
		else if (var9 == 3)
		{
			setBlockBounds(0.0F, var12, 0.0F, 1.0F, var13, 0.5F);
			super.addCollidingBlockToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Sets the metadata of the stairs when they are placed
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
	{
		int var6 = MathHelper.floor_double(par5EntityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		switch (var6)
			{
				case 0:
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
			}

		if (var6 == 0)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 2);

		if (var6 == 1)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 1);

		if (var6 == 2)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 3);

		if (var6 == 3)
			par1World.setBlockMetadataWithNotify(par2, par3, par4, 0);
	}

	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
	 * x, y, z, startVec, endVec
	 */
	@Override
	public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
	{
		MovingObjectPosition[] var7 = new MovingObjectPosition[8];
		int var8 = par1World.getBlockMetadata(par2, par3, par4);
		int var9 = var8 & 3;
		boolean var10 = (var8 & 4) == 4;
		int[] var11 = stairDirections[var9 + (var10 ? 4 : 0)];
		field_72156_cr = true;
		int var14;
		int var15;
		int var16;

		for (int var12 = 0; var12 < 8; ++var12)
		{
			int[] var13 = var11;
			var14 = var11.length;

			for (var15 = 0; var15 < var14; ++var15)
			{
				var16 = var13[var15];

				if (var16 == var12)
					;
			}

			var7[var12] = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
		}

		int[] var21 = var11;
		int var24 = var11.length;

		for (var14 = 0; var14 < var24; ++var14)
		{
			var15 = var21[var14];
			var7[var15] = null;
		}

		MovingObjectPosition var23 = null;
		double var22 = 0.0D;
		MovingObjectPosition[] var25 = var7;
		var16 = var7.length;

		for (int var17 = 0; var17 < var16; ++var17)
		{
			MovingObjectPosition var18 = var25[var17];

			if (var18 != null)
			{
				double var19 = var18.hitVec.squareDistanceTo(par6Vec3);

				if (var19 > var22)
				{
					var23 = var18;
					var22 = var19;
				}
			}
		}

		return var23;
	}
}
