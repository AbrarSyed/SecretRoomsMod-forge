package com.github.abrarsyed.secretroomsmod.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.api.BlockHolder;
import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.FakeWorld;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author AbrarSyed
 */
public class BlockOneWay extends BlockContainer
{
	public BlockOneWay()
	{
		super(Material.wood);
		setHardness(1.0F);
		setStepSound(soundTypeWood);
		setLightOpacity(0);
		setCreativeTab(SecretRooms.tab);
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
	public int getLightOpacity(IBlockAccess world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z) == 1 ? 0 : 255;
	}

	@Override
	public int getRenderType()
	{
		return SecretRooms.render3DId;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		if (!SecretRooms.displayCamo && SecretRooms.proxy.isOwner(world, x, y, z))
			return getBlockTextureFromSide(side);

		int metadata = world.getBlockMetadata(x, y, z);

		if (side != metadata)
			return Blocks.glass.getIcon(side, 0);

		try
		{
			TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);
			Block block = entity.getCopyBlock();

			if (block == null)
				return blockIcon;

			FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.getWorldObj());

			return block.getIcon(fake, x, y, z, side);
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
		Block var6 = par1IBlockAccess.getBlock(par2, par3, par4);
		return var6 == this ? false : super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public IIcon getIcon(int i, int meta)
	{
		if (i == 3)
			return blockIcon;
		return Blocks.glass.getIcon(i, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BASE);
	}
	
    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        try
        {
            if (OwnershipManager.isOwner(player.getPersistentID(), new BlockLocation(world, x, y, z)))
            {
                return super.getPickBlock(target, world, x, y, z, player);
            }
            
            TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);

            if (entity == null)
                return null;

            return new ItemStack(entity.getCopyBlock());
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        try
        {
            if (SecretRooms.proxy.isOwner(world, x, y, z))
            {
                return super.getPickBlock(target, world, x, y, z);
            }
            
            TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);

            if (entity == null)
                return null;

            return new ItemStack(entity.getCopyBlock());
        }
        catch (Exception e)
        {
            return null;
        }
    }

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack stack)
	{
		int metadata = 1;
		if (entityliving instanceof EntityPlayer)
		{
			metadata = determineOrientation(world, i, j, k, (EntityPlayer) entityliving);
		}

		world.setBlockMetadataWithNotify(i, j, k, metadata, 2);

		// CAMO STUFF
		BlockHolder holder = getIdCamoStyle(world, i, j, k);

		TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(i, j, k);

		if (holder == null)
		{
			holder = new BlockHolder(Blocks.stone, 0, null);
		}

		entity.setBlockHolder(holder);
        entity.setOwner(entityliving.getUniqueID());
        
        world.markBlockForUpdate(i, j, k);
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
		int meta = world.getBlockMetadata(x, y, z);
		BlockHolder[] holders = getHoldersFromFacing(world, x, y, z, meta);

		// if there is only 1 in the PLUS SIGN checked.
		if (BlockCamoFull.isOneLeft(holders))
		{
			holders = BlockCamoFull.truncateArray(holders);
			return holders[0];
		}

		BlockHolder end = null;

		// first line.
		if (holders[0] != null && holders[0].equals(holders[1]))
		{
			end = holders[0];
		}
		else if (holders[2] != null && holders[2].equals(holders[3]))
		{
			end = holders[0];
		}

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
		if (!SecretRooms.displayCamo && SecretRooms.proxy.isOwner(world, x, y, z))
			return super.colorMultiplier(world, x, y, z);

		TileEntityCamo entity = (TileEntityCamo) world.getTileEntity(x, y, z);

		if (entity == null)
			return super.colorMultiplier(world, x, y, z);

		FakeWorld fake = SecretRooms.proxy.getFakeWorld(entity.getWorldObj());
		Block fakeBlock = entity.getCopyBlock();

		if (fakeBlock == null)
			return super.colorMultiplier(world, x, y, z);

		return fakeBlock.colorMultiplier(fake, x, y, z);
	}

	public static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer)
	{
		int direction = BlockPistonBase.determineOrientation(world, i, j, k, entityplayer);

		ForgeDirection dir = ForgeDirection.getOrientation(direction);

		if (!SecretRooms.proxy.getFaceTowards(entityplayer.getUniqueID()))
		{
			dir = dir.getOpposite();
		}

		return dir.ordinal();
	}

	public BlockHolder[] getHoldersFromFacing(World world, int i, int j, int k, int direction)
	{
		BlockHolder[] holders = new BlockHolder[4];

		switch (direction)
			{
				case 0:
					holders[0] = getInfo(world, i + 1, j, k);
					holders[1] = getInfo(world, i - 1, j, k);
					holders[2] = getInfo(world, i, j, k + 1);
					holders[3] = getInfo(world, i, j, k - 1);
					break;

				case 1:
					holders[0] = getInfo(world, i + 1, j, k);
					holders[1] = getInfo(world, i - 1, j, k);
					holders[2] = getInfo(world, i, j, k + 1);
					holders[3] = getInfo(world, i, j, k - 1);
					break;

				case 2:
					holders[0] = getInfo(world, i, j + 1, k);
					holders[1] = getInfo(world, i, j - 1, k);
					holders[2] = getInfo(world, i + 1, j, k);
					holders[3] = getInfo(world, i - 1, j, k);
					break;

				case 3:
					holders[0] = getInfo(world, i, j + 1, k);
					holders[1] = getInfo(world, i, j - 1, k);
					holders[2] = getInfo(world, i + 1, j, k);
					holders[3] = getInfo(world, i - 1, j, k);
					break;

				case 4:
					holders[0] = getInfo(world, i, j + 1, k);
					holders[1] = getInfo(world, i, j - 1, k);
					holders[2] = getInfo(world, i, j, k + 1);
					holders[3] = getInfo(world, i, j, k - 1);
					break;

				case 5:
					holders[0] = getInfo(world, i, j + 1, k);
					holders[1] = getInfo(world, i, j - 1, k);
					holders[2] = getInfo(world, i, j, k + 1);
					holders[3] = getInfo(world, i, j, k - 1);
					break;
			}

		// return texture only
		return holders;
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
			Block block = world.getBlock(x, y, z);
			TileEntity entity = world.getTileEntity(x, y, z);

			if (entity != null && entity instanceof TileEntityCamo)
			{
				TileEntityCamo te = (TileEntityCamo) world.getTileEntity(x, y, z);
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

	@Override
	public TileEntity createNewTileEntity(World var1, int meta)
	{
		return new TileEntityCamo();
	}

	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		return 0;
	}
}
