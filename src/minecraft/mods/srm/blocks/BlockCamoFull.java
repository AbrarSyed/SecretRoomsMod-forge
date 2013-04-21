package mods.SecretRoomsMod.blocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import mods.SecretRoomsMod.SecretRooms;
import mods.SecretRoomsMod.common.BlockHolder;
import mods.SecretRoomsMod.common.fake.FakeWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
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

	public BlockCamoFull(int par1)
	{
		super(par1, Material.wood);
		setLightOpacity(255);
		setCreativeTab(SecretRooms.tab);
	}

	public BlockCamoFull(int par1, Material material)
	{
		super(par1, material);
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
		return new TileEntityCamo();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BASE);
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public final int idPicked(World world, int x, int y, int z)
	{
		try
		{
			TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(x, y, z);
			FakeWorld fake = SecretRooms.proxy.getFakeWorld(world);

			if (entity == null)
				return 0;

			return entity.getCopyID();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	@Override
	public final int getLightOpacity(World world, int x, int y, int z)
	{
		try
		{
			TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(x, y, z);
			FakeWorld fake = SecretRooms.proxy.getFakeWorld(world);

			if (entity == null)
				return 255;

			int id = entity.getCopyID();

			if (id == 0)
				return 255;

			return Block.blocksList[id].getLightOpacity(fake, x, y, z);
		}
		catch (Exception e)
		{
			return 255;
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public final int getRenderType()
	{
		return SecretRooms.camoRenderId;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
	{
		if (!SecretRooms.displayCamo)
			return getBlockTextureFromSide(dir);

		try
		{
			TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(x, y, z);
			int id = entity.getCopyID();

			if (id == 0)
				return blockIcon;

			FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.worldObj);

			return Block.blocksList[id].getBlockTexture(fake, x, y, z, dir);
		}
		catch (Throwable t)
		{
			return blockIcon;
		}
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

		if (world.isRemote)
			return;

		// CAMO STUFF
		BlockHolder holder = getIdCamoStyle(world, i, j, k);

		TileEntityCamo entity = (TileEntityCamo) world.getBlockTileEntity(i, j, k);

		if (holder == null)
		{
			holder = new BlockHolder(1, 0, null);
		}

		entity.setBlockHolder(holder);
		FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(entity.getDescriptionPacket());
	}

	@Override
	public final int colorMultiplier(IBlockAccess par1IBlockAccess, int x, int y, int z)
	{
		if (!SecretRooms.displayCamo)
			return super.colorMultiplier(par1IBlockAccess, x, y, z);

		TileEntityCamo entity = (TileEntityCamo) par1IBlockAccess.getBlockTileEntity(x, y, z);

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
	 * @param world
	 * @param x coord
	 * @param y coord
	 * @param z coord
	 * @return the ID of the block to be copied
	 */
	private BlockHolder getIdCamoStyle(World world, int x, int y, int z)
	{
		BlockHolder[] holders = new BlockHolder[6];
		// Only PLUS sign id checks.
		holders[0] = getInfo(world, x, y - 1, z); // y-1
		holders[1] = getInfo(world, x, y + 1, z); // y+1
		holders[2] = getInfo(world, x - 1, y, z); // x-1
		holders[3] = getInfo(world, x + 1, y, z); // x+1
		holders[4] = getInfo(world, x, y, z - 1); // z-1
		holders[5] = getInfo(world, x, y, z + 1); // z+1

		// if there is only 1 in the PLUS SIGN checked.
		if (isOneLeft(holders))
		{
			holders = truncateArray(holders);
			// System.out.println("IDs worked early:  " + Arrays.toString(plusIds[0]));
			return holders[0];
		}

		BlockHolder[] planeChecks = new BlockHolder[3];

		// checks X's
		if (holders[2] != null && holders[2].equals(holders[3]))
		{
			planeChecks[0] = holders[2];
		}

		// checks Y's
		if (holders[0] != null && holders[0].equals(holders[1]))
		{
			planeChecks[1] = holders[0];
		}

		// checks Z's
		if (holders[4] != null && holders[4].equals(holders[5]))
		{
			planeChecks[2] = holders[4];
		}

		BlockHolder end = null;

		// part of XZ wall?
		if (planeChecks[0] != null && planeChecks[0].equals(planeChecks[2]))
		{
			end = planeChecks[0];
		}
		// part of XY wall?
		else if (planeChecks[0] != null && planeChecks[0].equals(planeChecks[1]))
		{
			end = planeChecks[0];
		}
		// part of YZ wall?
		else if (planeChecks[1] != null && planeChecks[1].equals(planeChecks[2]))
		{
			end = planeChecks[1];
		}

		// no entire planes? lets check single lines.

		// check y
		else if (planeChecks[1] != null)
		{
			end = planeChecks[1];
		}
		// check x
		else if (planeChecks[0] != null)
		{
			end = planeChecks[0];
		}
		// check z
		else if (planeChecks[2] != null)
		{
			end = planeChecks[2];
		}

		// System.out.println("IDs are fun:  " + Arrays.toString(id));

		if (end != null)
			return end;

		// GET MODE
		holders = truncateArray(holders);
		return tallyMode(holders);
	}

	protected static BlockHolder tallyMode(BlockHolder[] holders)
	{
		HashMap<BlockHolder, Integer> map = new HashMap<BlockHolder, Integer>();

		Integer num;
		for (BlockHolder holder : holders)
		{
			num = map.get(holder);
			if (num == null)
			{
				map.put(holder, 1);
			}
			else
			{
				map.put(holder, num++);
			}
		}

		BlockHolder highestHolder = null;
		int highestNum = 0;
		for (Entry<BlockHolder, Integer> entry : map.entrySet())
		{
			if (entry.getValue() > highestNum)
			{
				highestNum = entry.getValue();
				highestHolder = entry.getKey();
			}
		}

		return highestHolder;
	}

	/**
	 * Used to specially get Ids. It returns zero if the texture cannot be copied, or if it is air.
	 * @param world The world
	 * @param x X coordinate
	 * @param y Y Coordinate
	 * @param z Z Coordinate
	 * @return
	 */
	protected static BlockHolder getInfo(World world, int x, int y, int z)
	{
		// if its an air block, return 0
		if (world.isAirBlock(x, y, z))
			return null;
		else
		{
			int id = world.getBlockId(x, y, z);
			Block block = Block.blocksList[id];

			if (block instanceof BlockCamoFull || block instanceof BlockOneWay)
			{
				TileEntityCamo te = (TileEntityCamo) world.getBlockTileEntity(x, y, z);
				return te.getBlockHolder();
			}
			else if (block.isOpaqueCube())
				return new BlockHolder(world, x, y, z);
			else
			{

				if (block.getBlockBoundsMinX() == 0 &&
						block.getBlockBoundsMinY() == 0 &&
						block.getBlockBoundsMinZ() == 0 &&
						block.getBlockBoundsMaxX() == 1 &&
						block.getBlockBoundsMaxY() == 1 &&
						block.getBlockBoundsMaxZ() == 1)
					return new BlockHolder(world, x, y, z);
				else
					return null;
			}
		}
	}

	/**
	 * This truncates an int Array so that it contains only values above zero. The size of the array is also cut down so that all of them are full.
	 * @param array The array to be truncated
	 * @return the truncated array.
	 */
	protected static BlockHolder[] truncateArray(BlockHolder[] array)
	{
		int num = 0;

		for (BlockHolder obj : array)
			if (obj != null)
			{
				num++;
			}

		BlockHolder[] truncated = new BlockHolder[num];

		num = 0;
		for (BlockHolder obj : array)
			if (obj != null)
			{
				truncated[num] = obj;
				num++;
			}

		return truncated;
	}

	protected static boolean isOneLeft(BlockHolder[] holders)
	{
		return !checkAllNull(holders) && truncateArray(holders).length == 1;
	}

	private static boolean checkAllNull(BlockHolder[] holders)
	{
		boolean flag = true;

		for (BlockHolder obj : holders)
			if (obj != null)
				return false;

		return flag;
	}

	@Override
	public int getFlammability(IBlockAccess iba, int x, int y, int z, int metadata, ForgeDirection face)
	{
		TileEntityCamo entity = (TileEntityCamo) iba.getBlockTileEntity(x, y, z);

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
