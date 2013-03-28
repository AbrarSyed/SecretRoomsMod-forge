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
import net.minecraft.util.Icon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockOneWay extends BlockContainer
{
	public BlockOneWay(int i)
	{
		super(i, Material.wood);
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
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		if (!SecretRooms.displayCamo)
			return getBlockTextureFromSide(side);
		
		int metadata = world.getBlockMetadata(x, y, z);
		
		if (side != metadata)
			return Block.glass.getBlockTextureFromSide(side);

		try
		{
			TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);
			int id = entity.getCopyID();

			FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.worldObj);

			return Block.blocksList[id].getBlockTexture(fake, x, y, z, side);
		}
		catch (Throwable t)
		{
			return blockIcon;
		}
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		int var6 = par1IBlockAccess.getBlockId(par2, par3, par4);
		return var6 == blockID ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public Icon getBlockTextureFromSideAndMetadata(int i, int meta)
	{
		if (i == 5)
			return blockIcon;
		return Block.glass.getBlockTextureFromSide(i);
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack stack)
	{
		int metadata = 1;
		if (entityliving instanceof EntityPlayer)
		{
			metadata = BlockOneWay.determineOrientation(world, i, j, k, (EntityPlayer) entityliving);
		}

		world.setBlockMetadataWithNotify(i, j, k, metadata, 2);
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		super.onBlockAdded(world, i, j, k);

		if (world.isRemote)
			return;

		// CAMO STUFF
		BlockHolder holder = getIdCamoStyle(world, i, j, k);

		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(i, j, k);

		if (holder == null)
		{
			holder = new BlockHolder(1, 0, null);
		}

		entity.setBlockHolder(holder);
		FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(entity.getDescriptionPacket());
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
		holders[0] = BlockCamoFull.getInfo(world, x, y - 1, z); // y-1
		holders[1] = BlockCamoFull.getInfo(world, x, y + 1, z); // y+1
		holders[2] = BlockCamoFull.getInfo(world, x - 1, y, z); // x-1
		holders[3] = BlockCamoFull.getInfo(world, x + 1, y, z); // x+1
		holders[4] = BlockCamoFull.getInfo(world, x, y, z - 1); // z-1
		holders[5] = BlockCamoFull.getInfo(world, x, y, z + 1); // z+1

		// if there is only 1 in the PLUS SIGN checked.
		if (BlockCamoFull.isOneLeft(holders))
		{
			holders = BlockCamoFull.truncateArray(holders);
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
		holders = BlockCamoFull.truncateArray(holders);
		return BlockCamoFull.tallyMode(holders);
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		if (!SecretRooms.displayCamo)
			return super.colorMultiplier(world, x, y, z);

		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(x, y, z);

		if (entity == null)
			return super.colorMultiplier(world, x, y, z);

		FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.worldObj);
		int id = entity.getCopyID();

		if (id == 0)
			return super.colorMultiplier(world, x, y, z);

		Block fakeBlock = Block.blocksList[id];

		return fakeBlock.colorMultiplier(fake, x, y, z);
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

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityCamoFull();
	}
}
