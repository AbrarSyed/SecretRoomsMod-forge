package com.github.AbrarSyed.SecretRooms.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockCamoFull extends BlockContainer
{

	protected BlockCamoFull(int par1)
	{
		super(par1, Material.wood);
		blockIndexInTexture = 0;
		setLightOpacity(255);
		setCreativeTab(SecretRooms.tab);
	}

	protected BlockCamoFull(int par1, Material material)
	{
		super(par1, material);
		blockIndexInTexture = 0;
		setLightOpacity(255);
		setCreativeTab(SecretRooms.tab);
	}

	@Override
	public void addCreativeItems(ArrayList itemList)
	{
		itemList.add(new ItemStack(this));
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityCamoFull();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public final int getLightOpacity(World world, int x, int y, int z)
	{
		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);
		FakeWorld fake = SecretRooms.proxy.getFakeWorld(world);

		if (entity == null)
			return 255;

		int id = entity.getCopyID();

		if (id == 0)
			return 255;

		return Block.blocksList[id].getLightOpacity(fake, x, y, z);
	}

	@Override
	public final boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public final int getRenderType()
	{
		return SecretRooms.camoRenderId;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public final int getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
	{
		if (!SecretRooms.displayCamo)
			return getBlockTextureFromSide(dir);

		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);
		int id;
		if (entity == null)
		{
			id = 1;
		}
		else if (entity.getCopyID() <= 0)
		{
			id = 1;
		}
		else
		{
			id = entity.getCopyID();
		}

		if (id == 1)
			return Block.stone.blockIndexInTexture;

		FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.worldObj);

		return Block.blocksList[id].getBlockTexture(fake, x, y, z, dir);
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return blockID;
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		super.onBlockAdded(world, i, j, k);
		// CAMO STUFF
		int[] IdAndCoords = getIdCamoStyle(world, i, j, k);
		BlockHolder holder;

		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(i, j, k);

		if (Arrays.equals(IdAndCoords, new int[] { 1, 0, 0, 0 }))
		{
			holder = new BlockHolder(1, 0, null);
		}

		TileEntity test = world.getBlockTileEntity(IdAndCoords[1], IdAndCoords[2], IdAndCoords[3]);

		if (test instanceof TileEntityCamoFull)
		{
			holder = ((TileEntityCamoFull) test).getBlockHolder();
		}
		else
		{
			holder = new BlockHolder(world, IdAndCoords[1], IdAndCoords[2], IdAndCoords[3]);
		}

		entity.setBlockHolder(holder);

		if (!world.isRemote)
		{
			FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(entity.getDescriptionPacket());
		}
	}

	@Override
	public final int colorMultiplier(IBlockAccess par1IBlockAccess, int x, int y, int z)
	{
		if (!SecretRooms.displayCamo)
			return super.colorMultiplier(par1IBlockAccess, x, y, z);

		TileEntityCamoFull entity = (TileEntityCamoFull) par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (entity == null)
			return super.colorMultiplier(par1IBlockAccess, x, y, z);

		FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.worldObj);
		int id = entity.getCopyID();

		if (id == 0)
			return super.colorMultiplier(par1IBlockAccess, x, y, z);

		Block fakeBlock = Block.blocksList[id];

		return fakeBlock.colorMultiplier(fake, x, y, z);
	}

	/**
	 * annalyses surrounding blocks and decides on a BlockID for the Camo Block to copy.
	 * 
	 * @param world
	 * @param x coord
	 * @param y coord
	 * @param z coord
	 * @return the ID of the block to be copied
	 */
	private int[] getIdCamoStyle(World world, int x, int y, int z)
	{
		int[] id = new int[] { 0, 0, 0, 0 };
		int[][] plusIds = new int[6][4];
		// Only PLUS sign id checks.
		plusIds[0] = getInfo(world, x, y - 1, z); // y-1
		plusIds[1] = getInfo(world, x, y + 1, z); // y+1
		plusIds[2] = getInfo(world, x - 1, y, z); // x-1
		plusIds[3] = getInfo(world, x + 1, y, z); // x+1
		plusIds[4] = getInfo(world, x, y, z - 1); // z-1
		plusIds[5] = getInfo(world, x, y, z + 1); // z+1

		// if there is only 1 in the PLUS SIGN checked.
		if (isOneLeft(truncateArrayINT(plusIds)))
		{
			plusIds = truncateArrayINT(plusIds);
			// System.out.println("IDs worked early:  " + Arrays.toString(plusIds[0]));
			return plusIds[0];
		}

		int[][] intChecks = new int[3][4];

		// checks Y's
		if (plusIds[0][0] == plusIds[1][0])
		{
			intChecks[1] = plusIds[0];
		}

		// checks X's
		if (plusIds[2][0] == plusIds[3][0])
		{
			intChecks[0] = plusIds[2];
		}

		// checks Z's
		if (plusIds[4][0] == plusIds[5][0])
		{
			intChecks[2] = plusIds[4];
		}

		// part of XY wall?
		if (intChecks[1][0] == intChecks[0][0] && intChecks[0][0] > 0)
		{
			id = intChecks[0];
		}
		else if (intChecks[1][0] == intChecks[2][0] && intChecks[2][0] > 0)
		{
			id = intChecks[1];
		}
		else if (intChecks[2][0] == intChecks[0][0] && intChecks[0][0] > 0)
		{
			id = intChecks[0];
		}
		else if (intChecks[1][0] != 0)
		{
			id = intChecks[1];
		}
		else if (intChecks[0][0] != 0)
		{
			id = intChecks[0];
		}
		else if (intChecks[2][0] != 0)
		{
			id = intChecks[2];
		}

		// System.out.println("IDs are fun:  " + Arrays.toString(id));

		if (id[0] != 0)
			return id;

		// GET MODE
		plusIds = truncateArrayINT(plusIds);

		try
		{
			id = tallyMode(plusIds);
		}
		catch (Exception e)
		{
			int[][] test = truncateArrayINT(plusIds);
			if (test.length >= 1)
			{
				id = test[0];
			}
			else
			{
				id = new int[] { 1, 0, 0, 0 };
			}
		}

		if (id[0] == 0)
			return new int[] { 1, 0, 0, 0 };

		return id;
	}

	private int[] tallyMode(int[][] nums2) throws Exception
	{
		int[] nums = new int[nums2.length];

		for (int i = 0; i < nums2.length; i++)
		{
			nums[i] = nums2[i][0];
		}

		// create array of tallies, all initialized to zero
		int[] tally = new int[256];

		// for each array entry, increment corresponding tally box
		for (int i = 0; i < nums.length; i++)
		{
			int value = nums[i];
			tally[value]++;
		}

		// now find the index of the largest tally - this is the mode
		int maxIndex = 0;

		for (int i = 1; i < tally.length; i++)
			if (tally[i] == tally[maxIndex] && tally[i] != 0)
				throw new Exception("NULL");
			else if (tally[i] > tally[maxIndex])
			{
				maxIndex = i;
			}

		return nums2[maxIndex];
	}

	/**
	 * Used to specially get Ids. It returns zero if the texture cannot be copied, or if it is air.
	 * 
	 * @param world The world
	 * @param x X coordinate
	 * @param y Y Coordinate
	 * @param z Z Coordinate
	 * @return
	 */
	private static int[] getInfo(World world, int x, int y, int z)
	{
		// if its an air block, return 0
		if (world.isAirBlock(x, y, z))
			return new int[] { 0, 0, 0, 0 };
		else
		{
			int id = world.getBlockId(x, y, z);
			Block block = Block.blocksList[id];

			if (block instanceof BlockOneWay)
				return new int[] { id, x, y, z };
			else if (block.isOpaqueCube())
				return new int[] { id, x, y, z };
			else
			{

				if (block.getBlockBoundsMinX() == 0 &&
						block.getBlockBoundsMinY() == 0 &&
						block.getBlockBoundsMinZ() == 0 &&
						block.getBlockBoundsMaxX() == 1 &&
						block.getBlockBoundsMaxY() == 1 &&
						block.getBlockBoundsMaxZ() == 1)
					return new int[] { id, x, y, z };
				else
					return new int[] { 0, 0, 0, 0 };
			}
		}
	}

	/**
	 * This truncates an int Array so that it contains only values above zero. The size of the array is also cut down so that all of them are full.
	 * 
	 * @param array
	 * The array to be truncated
	 * @return the truncated array.
	 */
	private static int[][] truncateArrayINT(int[][] array)
	{
		int num = 0;

		for (int[] obj : array)
			if (obj[0] > 0)
			{
				num++;
			}

		int[][] truncated = new int[num][4];
		num = 0;

		for (int[] obj : array)
			if (obj[0] > 0)
			{
				truncated[num] = obj;
				num++;
			}

		return truncated;
	}

	private boolean isOneLeft(int[][] textures)
	{
		if (!checkAllNull(textures))
			if (truncateArrayINT(textures).length == 1)
				return true;

		return false;
	}

	private boolean checkAllNull(int[][] textures)
	{
		boolean flag = true;

		for (int[] num : textures)
			if (num[0] > 0)
			{
				flag = false;
			}

		return flag;
	}

	@Override
	public int getFlammability(IBlockAccess iba, int x, int y, int z, int metadata, ForgeDirection face)
	{
		TileEntityCamoFull entity = (TileEntityCamoFull) iba.getBlockTileEntity(x, y, z);

		if (entity != null)
		{
			World world = entity.worldObj;
			int ID = entity.getCopyID();

			if (ID != 0)
				return blocksList[ID].getFlammability(SecretRooms.proxy.getFakeWorld(world), x, y, z, metadata, face);
		}

		return blockFlammability[blockID];
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		return true;
	}

}
