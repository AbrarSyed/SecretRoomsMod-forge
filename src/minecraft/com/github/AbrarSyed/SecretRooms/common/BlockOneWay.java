package com.github.AbrarSyed.SecretRooms.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockOneWay extends BlockContainer
{
	public BlockOneWay(int i, int j)
	{
		super(i, j, Material.wood);
		setHardness(1.0F);
		setStepSound(Block.soundWoodFootstep);
		setLightOpacity(0);
		setCreativeTab(SecretRooms.tab);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 1 ? 0 : 255;
	}

	@Override
	public int getRenderType()
	{
		return SecretRooms.camoRenderId;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
	{
		TileEntityCamo entity = (TileEntityCamo) iblockaccess.getBlockTileEntity(i, j, k);
		int metadata = iblockaccess.getBlockMetadata(i, j, k);

		if (l == metadata)
			if (entity == null)
				return glass.blockIndexInTexture;
			else
			{
				if (!SecretRooms.displayCamo)
					return 0;
				return entity.getTexture();
			}

		return blockIndexInTexture;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
		return var6 == blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public int getBlockTextureFromSide(int i)
	{
		if (i == 5)
			return 0;
		return blockIndexInTexture;
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
	{
		int metadata = 1;
		if (entityliving instanceof EntityPlayer)
		{
			metadata = BlockOneWay.determineOrientation(world, i, j, k, (EntityPlayer) entityliving);
		}

		world.setBlockMetadata(i, j, k, metadata);

		SecretRooms.proxy.handleOneWayPlace(world, i, j, k, entityliving);
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z)
	{

		if (!SecretRooms.displayCamo)
			return 0xffffff;

		TileEntityCamo entityHere = (TileEntityCamo) iblockaccess.getBlockTileEntity(x, y, z);
		iblockaccess.getBlockMetadata(x, y, z);

		if (entityHere == null)
			return 0xffffff;

		int texture = entityHere.getTexture();

		if (entityHere.getTexturePath().equals("/terrain.png"))
		{
			// grassed
			if (texture == 0 || texture == 3 || texture == 38)
				return Block.grass.colorMultiplier(iblockaccess, x, y, z);

			// normal leaves
			if (texture == 52 || texture == 53)
			{
				int var6 = 0;
				int var7 = 0;
				int var8 = 0;

				for (int var9 = -1; var9 <= 1; ++var9)
				{
					for (int var10 = -1; var10 <= 1; ++var10)
					{
						int var11 = iblockaccess.getBiomeGenForCoords(x + var10, z + var9).getBiomeFoliageColor();
						var6 += (var11 & 16711680) >> 16;
						var7 += (var11 & 65280) >> 8;
						var8 += var11 & 255;
					}
				}

				return (var6 / 9 & 255) << 16 | (var7 / 9 & 255) << 8 | var8 / 9 & 255;
			}

			// pine leaves
			if (texture == 132 || texture == 133)
				return ColorizerFoliage.getFoliageColorPine();

			// birch leaves
			if (texture == 196 || texture == 197)
				return ColorizerFoliage.getFoliageColorBirch();

		}

		return 0xffffff;
	}

	public static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int direction = BlockPistonBase.determineOrientation(world, i, j, k, entityplayer);

		ForgeDirection dir = ForgeDirection.getOrientation(direction);

		if (!SecretRooms.proxy.getFaceAway(entityplayer.username))
		{
			dir = dir.getOpposite();
		}

		return dir.ordinal();
	}

	@SideOnly(value = Side.CLIENT)
	public Object[] getOtherProperties(World world, int i, int j, int k, int direction)
	{
		int l = direction;
		Object[] tempTexture = null;
		Object[][] textures = new Object[4][2];
		switch (l)
			{
				case 0:
					textures[0] = getTexture(world, i + 1, j, k, l);
					textures[1] = getTexture(world, i - 1, j, k, l);
					textures[2] = getTexture(world, i, j, k + 1, l);
					textures[3] = getTexture(world, i, j, k - 1, l);
					tempTexture = getDuplicateBlock(textures);
					break;

				case 1:
					textures[0] = getTexture(world, i + 1, j, k, l);
					textures[1] = getTexture(world, i - 1, j, k, l);
					textures[2] = getTexture(world, i, j, k + 1, l);
					textures[3] = getTexture(world, i, j, k - 1, l);
					tempTexture = getDuplicateBlock(textures);
					break;

				case 2:
					textures[0] = getTexture(world, i, j + 1, k, l);
					textures[1] = getTexture(world, i, j - 1, k, l);
					textures[2] = getTexture(world, i + 1, j, k, l);
					textures[3] = getTexture(world, i - 1, j, k, l);
					tempTexture = getDuplicateBlock(textures);
					break;

				case 3:
					textures[0] = getTexture(world, i, j + 1, k, l);
					textures[1] = getTexture(world, i, j - 1, k, l);
					textures[2] = getTexture(world, i + 1, j, k, l);
					textures[3] = getTexture(world, i - 1, j, k, l);
					tempTexture = getDuplicateBlock(textures);
					break;

				case 4:
					textures[0] = getTexture(world, i, j + 1, k, l);
					textures[1] = getTexture(world, i, j - 1, k, l);
					textures[2] = getTexture(world, i, j, k + 1, l);
					textures[3] = getTexture(world, i, j, k - 1, l);
					tempTexture = getDuplicateBlock(textures);
					break;

				case 5:
					textures[0] = getTexture(world, i, j + 1, k, l);
					textures[1] = getTexture(world, i, j - 1, k, l);
					textures[2] = getTexture(world, i, j, k + 1, l);
					textures[3] = getTexture(world, i, j, k - 1, l);
					tempTexture = getDuplicateBlock(textures);
					break;
			}

		// return texture only
		return tempTexture;
	}

	@SideOnly(value = Side.CLIENT)
	private Object[] getDuplicateBlock(Object[][] textures)
	{
		if (checkAllNull(textures))
			// System.out.println("All Null, returning glass.");
			return new Object[] { Block.glass.blockIndexInTexture, null };
		else if (isOneLeft(textures))
		{
			// System.out.println("All Null but one, returning that.");
			textures = truncateArrayOBJ(textures);
			if ((Integer) textures[0][0] >= 0)
				return textures[0];

			// System.out.println("But it is null.");
		}

		int[] textureCalc = new int[4];
		ArrayList<String> pathList = new ArrayList<String>();
		for (int i = 0; i < 4; i++)
		{
			textureCalc[i] = addToList(pathList, textures[i]);
		}

		byte icon = -1;

		// System.out.println("Texture side1 = "+textureCalc[2]);
		// System.out.println("Texture side2 = "+textureCalc[3]);
		// check sides
		if (textureCalc[2] >= 0 && textureCalc[3] >= 0 && textureCalc[2] == textureCalc[3] && icon < 0)
		{
			// System.out.println("Sides Have Same texture, using that");
			icon = 2;
		}

		// System.out.println("Texture top = "+textureCalc[0]);
		// System.out.println("Texture bot = "+textureCalc[1]);
		// check top and bottom
		if (textureCalc[0] >= 0 && textureCalc[1] >= 0 && textureCalc[0] == textureCalc[1] && icon < 0)
		{
			// System.out.println("Top And Bottom Have Same texture, using that");
			icon = 0;
		}

		// if all else fails, get mode.
		if (icon < 0)
		{
			// System.out.println("Trying Tally");
			textureCalc = truncateArrayINT(textureCalc);
			int tally = -1;

			try
			{
				// System.out.println("Trying Tally");
				textureCalc = truncateArrayINT(textureCalc);
				tally = tallyMode(textureCalc);
				// System.out.println("There are "+textureCalc.length+" texures.");
				// System.out.println("Tally is "+tally);
			}
			catch (Throwable throwable)
			{
				// System.out.println("Tally failed.");
			}

			if (tally <= textureCalc.length && tally >= 0)
				if (textureCalc[tally - 1] >= 0)
				{
					icon = (byte) tally;
					// System.out.println("tally is "+tally+" and icon is "+icon);
				}
		}

		if (icon >= 0)
			// textures = truncateArrayINT(textures);
			return getFromList(pathList, textureCalc[icon]);
		else
		{
			textureCalc = truncateArrayINT(textureCalc);
			// System.out.println("Got first texture" + textures[0]);
			return getFromList(pathList, textureCalc[0]);
		}
	}

	@SideOnly(value = Side.CLIENT)
	private int addToList(ArrayList<String> list, Object[] array)
	{
		if (array[1] == null || array[1].equals("/terrain.png"))
			return ((Integer) array[0]).intValue();

		int index;

		if (list.contains(array[1]))
		{
			index = list.indexOf(array[1]) + 1;
			return index * 1000 + (Integer) array[0];
		}

		list.add((String) array[1]);
		index = list.indexOf(array[1]) + 1;
		return index * 1000 + (Integer) array[0];
	}

	@SideOnly(value = Side.CLIENT)
	private Object[] getFromList(ArrayList<String> list, int texture)
	{
		if (texture <= 1000)
			return new Object[] { texture, null };

		int index = texture % 1000;
		texture = (texture - index) / 1000;
		String path = list.get(texture - 1);

		return new Object[] { index, path };
	}

	@SideOnly(value = Side.CLIENT)
	private boolean checkAllNull(Object[][] textures)
	{
		boolean flag = true;

		for (Object[] num : textures)
			if ((Integer) num[0] >= 0)
			{
				flag = false;
			}

		return flag;
	}

	@SideOnly(value = Side.CLIENT)
	private boolean isOneLeft(Object[][] textures)
	{
		if (!checkAllNull(textures))
		{
			textures = truncateArrayOBJ(textures);

			if (textures.length == 1)
				return true;
		}

		return false;
	}

	@SideOnly(value = Side.CLIENT)
	public Object[] getTexture(World world, int x, int y, int z, int side)
	{
		Object[] array = new Object[] { -1, null };
		if (world.isAirBlock(x, y, z))
			return array;

		Block block = blocksList[world.getBlockId(x, y, z)];

		if (block instanceof BlockCamoFull)
		{
			TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);
			array[0] = block.getBlockTexture(world, x, y, z, side);
			array[1] = Block.blocksList[entity.getCopyID()].getTextureFile();
			return array;
		}
		else if (block instanceof BlockOneWay)
		{
			TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(x, y, z);
			if (side == entity.getBlockMetadata())
			{
				array[0] = entity.getTexture();
				array[1] = entity.getTexturePath();
				return array;
			}
		}

		if (!block.isDefaultTexture)
		{
			array[1] = block.getTextureFile();
		}

		if (block.isOpaqueCube() && block.getRenderType() == 0)
		{
			array[0] = block.getBlockTexture(world, x, y, z, side);
			array[1] = block.getTextureFile();
			return array;
		}
		else
		{
			block.setBlockBoundsBasedOnState(world, x, y, z);
			double[] bounds = new double[] { block.getBlockBoundsMinX(), block.getBlockBoundsMinY(), block.getBlockBoundsMinZ(), block.getBlockBoundsMaxX(), block.getBlockBoundsMaxY(), block.getBlockBoundsMaxZ() };

			if (bounds[0] == 0 && bounds[1] == 0 && bounds[2] == 0 && bounds[3] == 1 && bounds[4] == 1 && bounds[5] == 1)
			{
				array[0] = block.getBlockTexture(world, x, y, z, side);
				return array;
			}
		}

		return array;
	}

	@SideOnly(value = Side.CLIENT)
	private Object[][] truncateArrayOBJ(Object[][] array)
	{
		int num = 0;

		for (Object[] obj : array)
			if ((Integer) obj[0] >= 0)
			{
				num++;
			}

		Object[][] truncated = new Object[num][2];
		num = 0;

		for (Object[] obj : array)
			if ((Integer) obj[0] >= 0)
			{
				truncated[num] = obj;
				num++;
			}

		return truncated;
	}

	@SideOnly(value = Side.CLIENT)
	private int[] truncateArrayINT(int[] array)
	{
		int num = 0;

		for (int obj : array)
			if (obj >= 0)
			{
				num++;
			}

		int[] truncated = new int[num];
		num = 0;

		for (int obj : array)
			if (obj >= 0)
			{
				truncated[num] = obj;
				num++;
			}

		return truncated;
	}

	@SideOnly(value = Side.CLIENT)
	private int tallyMode(int[] nums) throws Throwable
	{
		// create array of tallies, all initialized to zero
		int[] tally = new int[256];

		for (int i = 0; i < tally.length; i++)
		{
			tally[i] = 0;
		}

		// for each array entry, increment corresponding tally box
		for (int i = 0; i < nums.length; i++)
		{
			int value = nums[i];
			tally[value]++;
		}

		// now find the index of the largest tally - this is the mode
		int maxIndex = -1;

		for (int i = 1; i < tally.length; i++)
			if (tally[i] == tally[maxIndex] && maxIndex != 0)
				throw new Throwable("NULL");
			else if (tally[i] > tally[maxIndex])
			{
				maxIndex = i;
			}

		return maxIndex;
	}

	@SideOnly(value = Side.CLIENT)
	private int getMode(int[] nums) throws Throwable
	{
		int modeIndex = tallyMode(nums);
		return nums[modeIndex];
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityCamo();
	}
}
