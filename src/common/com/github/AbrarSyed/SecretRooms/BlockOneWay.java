package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 * @author AbrarSyed
 */
public class BlockOneWay extends BlockContainer
{
	public BlockOneWay(int i, int j)
	{
		super(i, j, Material.wood);
		this.setHardness(1.0F);
		this.setStepSound(Block.soundWoodFootstep);
		this.setLightOpacity(15);
		this.setCreativeTab(SecretRooms.tab);
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
		return world.getBlockMetadata(x, y, z) == 1 ? 0 : 15;
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
		{
			if (entity == null)
			{
				return glass.blockIndexInTexture;
			}
			else
			{
				if (!SecretRooms.displayCamo)
					return 0;
				return entity.getTexture();
			}
		}

		return blockIndexInTexture;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
		return var6 == this.blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
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
		int metadata = ((BlockOneWay) SecretRooms.oneWay).determineOrientation(world, i, j, k, (EntityPlayer) entityliving);
		world.setBlockMetadata(i, j, k, metadata);

		SecretRooms.proxy.doOneWayStuff(world, i, j, k, entityliving);
	}

	/*
	 * public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) { TileEntityCamo entity = (TileEntityCamo) par1World.getBlockTileEntity(par2, par3, par4); par5EntityPlayer.addChatMessage("texture: "+entity.getTexture()); return true; }
	 */

	@SideOnly(value = Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z)
	{

		if (!SecretRooms.displayCamo)
			return 0xffffff;

		TileEntityCamo entityHere = (TileEntityCamo) iblockaccess.getBlockTileEntity(x, y, z);
		int metadata = iblockaccess.getBlockMetadata(x, y, z);

		if (entityHere == null)
			return 0xffffff;

		int texture = entityHere.getTexture();

		if ((texture == 0 && metadata == 1) || texture == 3 || texture == 38)
		{
			int redColor = 0;
			int greenColor = 0;
			int blueColork = 0;

			for (int l = -1; l <= 1; l++)
			{
				for (int i1 = -1; i1 <= 1; i1++)
				{
					int grassColor = iblockaccess.getBiomeGenForCoords(x + i1, z + l).getBiomeGrassColor();
					redColor += (grassColor & 16711680) >> 16;
					greenColor += (grassColor & 65280) >> 8;
					blueColork += grassColor & 255;
				}
			}

			return (redColor / 9 & 255) << 16 | (greenColor / 9 & 255) << 8 | blueColork / 9 & 255;
		}

		return 0xffffff;
	}

	public static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int direction = BlockPistonBase.determineOrientation(world, i, j, k, entityplayer);

		if (!SecretRooms.OneWayFaceTowards)
		{
			switch(direction)
			{
				case 0: return 1;
				case 1: return 0;
				case 2: return 3;
				case 3: return 2;
				case 4: return 5;
				case 5: return 4;
				default: return 0;
			}
		}
		else
			return direction;
	}

	@SideOnly(value = Side.CLIENT)
	public Object[] getOtherProperties(World world, int i, int j, int k, int direction)
	{
		int l = direction;
		Object[] tempTexture = null;
		Object[][] textures = new Object[4][2];
		Block block;

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
		{
			// System.out.println("All Null, returning glass.");
			return new Object[] { Block.glass.blockIndexInTexture, null };
		}
		else if (isOneLeft(textures))
		{
			// System.out.println("All Null but one, returning that.");
			textures = truncateArrayOBJ(textures);
			if ((Integer) textures[0][0] >= 0)
			{
				return textures[0];
			}

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
			{
				if (textureCalc[tally - 1] >= 0)
				{
					icon = (byte) (tally);
					// System.out.println("tally is "+tally+" and icon is "+icon);
				}
			}
		}

		if (icon >= 0)
		{
			// textures = truncateArrayINT(textures);
			return getFromList(pathList, textureCalc[icon]);
		}
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
			return (index * 1000) + (Integer) array[0];
		}

		list.add((String) array[1]);
		index = list.indexOf(array[1]) + 1;
		return (index * 1000) + (Integer) array[0];
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
		{
			if ((Integer) num[0] >= 0)
			{
				flag = false;
			}
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
			{
				return true;
			}
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
			array[1] = (Block.blocksList[entity.getCopyID()]).getTextureFile();
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
		{
			if ((Integer) obj[0] >= 0)
			{
				num++;
			}
		}

		Object[][] truncated = new Object[num][2];
		num = 0;

		for (Object[] obj : array)
		{
			if ((Integer) obj[0] >= 0)
			{
				truncated[num] = obj;
				num++;
			}
		}

		return truncated;
	}

	@SideOnly(value = Side.CLIENT)
	private int[] truncateArrayINT(int[] array)
	{
		int num = 0;

		for (int obj : array)
		{
			if (obj >= 0)
			{
				num++;
			}
		}

		int[] truncated = new int[num];
		num = 0;

		for (int obj : array)
		{
			if (obj >= 0)
			{
				truncated[num] = obj;
				num++;
			}
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
		{
			if (tally[i] == tally[maxIndex] && maxIndex != 0)
			{
				throw new Throwable("NULL");
			}
			else if (tally[i] > tally[maxIndex])
			{
				maxIndex = i;
			}
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