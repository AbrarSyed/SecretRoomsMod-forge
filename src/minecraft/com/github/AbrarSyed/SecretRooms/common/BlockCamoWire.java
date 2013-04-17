package com.github.AbrarSyed.SecretRooms.common;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCamoWire extends Block
{
	@SideOnly(Side.CLIENT)
	private static Icon	cross;
	@SideOnly(Side.CLIENT)
	private static Icon	line;
	@SideOnly(Side.CLIENT)
	private static Icon	cross_overlay;
	@SideOnly(Side.CLIENT)
	private static Icon	line_overlay;

	public BlockCamoWire(int id)
	{
		super(id, Material.circuits);
		this.setCreativeTab(SecretRooms.tab);
	}

	/**
	 * make it walk-through.
	 */
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
	{
		return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)
				|| par1World.getBlockId(par2, par3 - 1, par4) == Block.glowStone.blockID;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone
	 * wire, etc to this block.
	 */
	public boolean isOpaqueCube()
	{
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return 5;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
	 * when first determining what to render.
	 */
	public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
	{
		return 8388608;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister par1IconRegister)
	{
		BlockCamoWire.cross = par1IconRegister.registerIcon("redstoneDust_cross");
		BlockCamoWire.line = par1IconRegister.registerIcon("redstoneDust_line");
		BlockCamoWire.cross_overlay = par1IconRegister.registerIcon("redstoneDust_cross_overlay");
		BlockCamoWire.line_overlay = par1IconRegister.registerIcon("redstoneDust_line_overlay");
		this.blockIcon = BlockCamoWire.cross;
	}

	@SideOnly(Side.CLIENT)
	public static Icon func_94409_b(String par0Str)
	{
		return par0Str == "redstoneDust_cross" ? cross : (par0Str == "redstoneDust_line" ? line
				: (par0Str == "redstoneDust_cross_overlay" ? cross_overlay
						: (par0Str == "redstoneDust_line_overlay" ? line_overlay : null)));
	}

	/**
	 * Determine if this block can make a redstone connection on the side provided,
	 * Useful to control which sides are inputs and outputs for redstone wires.
	 * Side:
	 * -1: UP
	 * 0: NORTH
	 * 1: EAST
	 * 2: SOUTH
	 * 3: WEST
	 * @param world The current world
	 * @param x X Position
	 * @param y Y Position
	 * @param z Z Position
	 * @param side The side that is trying to make the connection
	 * @return True to make the connection
	 */
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if (world.isRemote)
			return;

		calcPower(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
	{
		if (world.isRemote)
			return;

		calcPower(world, x, y, z);
		//world.notifyBlocksOfNeighborChange(x, y, z, blockID);
	}

	private void calcPower(World world, int x, int y, int z)
	{
		setRedstoneProvidePower(false);
		int nonWirePower = world.getStrongestIndirectPower(x, y, z);
		setRedstoneProvidePower(true);
		int wirePower = world.getStrongestIndirectPower(x, y, z);

		int power;
		if (nonWirePower >= wirePower)
			power = nonWirePower;
		else
			power = wirePower - 1;

		if (wirePower < 0)
			wirePower = 0;

		world.setBlockMetadataWithNotify(x, y, z, wirePower, 0x02);
		notifyArround(world, x, y, z);
	}

	public static void setRedstoneProvidePower(boolean bool)
	{
		ObfuscationReflectionHelper.setPrivateValue(BlockRedstoneWire.class, Block.redstoneWire, bool, 0);
	}

	public void notifyArround(World world, int x, int y, int z)
	{
		for (int pX = x - 1; pX <= x + 1; pX++)
			for (int pY = y - 1; pY <= y + 1; pY++)
				for (int pZ = z - 1; pZ <= z + 1; pZ++)
					if (x == pX && y == pY && z == pZ)
						continue;
					else
						world.notifyBlockOfNeighborChange(pX, pY, pZ, blockID);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		return isProvidingStrongPower(world, x, y, z, side);
	}

	@Override
	public boolean canProvidePower()
	{
		return Block.redstoneWire.canProvidePower();
	}

	// ------------------------------------------------------------------------------------------------------------
	// OLD REDSTONE CODE
	//------------------------------------------------------------------------------------------------------------
	//
	//	/**
	//	 * Sets the strength of the wire current (0-15) for this block based on
	//	 * neighboring blocks and propagates to
	//	 * neighboring redstone wires
	//	 */
	//	private void updateAndPropagateCurrentStrength(World par1World, int par2, int par3, int par4)
	//	{
	//		this.calculateCurrentChanges(par1World, par2, par3, par4, par2, par3, par4);
	//		ArrayList arraylist = this.getRedstoneUpdateList();
	//
	//		for (int l = 0; l < arraylist.size(); ++l)
	//		{
	//			ChunkPosition chunkposition = (ChunkPosition) arraylist.get(l);
	//			par1World.notifyBlocksOfNeighborChange(chunkposition.x, chunkposition.y, chunkposition.z, this.blockID);
	//		}
	//	}
	//
	//	private void calculateCurrentChanges(World par1World, int par2, int par3, int par4, int par5, int par6, int par7)
	//	{
	//		int k1 = par1World.getBlockMetadata(par2, par3, par4);
	//		byte b0 = 0;
	//		int l1 = this.getMaxCurrentStrength(par1World, par5, par6, par7, b0);
	//		//this.setRedstoneProvidePower(false);
	//		int i2 = par1World.getStrongestIndirectPower(par2, par3, par4);
	//		//this.setRedstoneProvidePower(true);
	//
	//		if (i2 > 0 && i2 > l1 - 1)
	//		{
	//			l1 = i2;
	//		}
	//
	//		int j2 = 0;
	//
	//		for (int k2 = 0; k2 < 4; ++k2)
	//		{
	//			int l2 = par2;
	//			int i3 = par4;
	//
	//			if (k2 == 0)
	//			{
	//				l2 = par2 - 1;
	//			}
	//
	//			if (k2 == 1)
	//			{
	//				++l2;
	//			}
	//
	//			if (k2 == 2)
	//			{
	//				i3 = par4 - 1;
	//			}
	//
	//			if (k2 == 3)
	//			{
	//				++i3;
	//			}
	//
	//			if (l2 != par5 || i3 != par7)
	//			{
	//				j2 = this.getMaxCurrentStrength(par1World, l2, par3, i3, j2);
	//			}
	//
	//			if (par1World.isBlockNormalCube(l2, par3, i3)
	//					&& !par1World.isBlockNormalCube(par2, par3 + 1, par4))
	//			{
	//				if ((l2 != par5 || i3 != par7) && par3 >= par6)
	//				{
	//					j2 = this.getMaxCurrentStrength(par1World, l2, par3 + 1, i3, j2);
	//				}
	//			}
	//			else if (!par1World.isBlockNormalCube(l2, par3, i3) && (l2 != par5 || i3 != par7)
	//					&& par3 <= par6)
	//			{
	//				j2 = this.getMaxCurrentStrength(par1World, l2, par3 - 1, i3, j2);
	//			}
	//		}
	//
	//		if (j2 > l1)
	//		{
	//			l1 = j2 - 1;
	//		}
	//		else if (l1 > 0)
	//		{
	//			--l1;
	//		}
	//		else
	//		{
	//			l1 = 0;
	//		}
	//
	//		if (i2 > l1 - 1)
	//		{
	//			l1 = i2;
	//		}
	//
	//		if (k1 != l1)
	//		{
	//			par1World.setBlockMetadataWithNotify(par2, par3, par4, l1, 2);
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2, par3, par4));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2 - 1, par3, par4));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2 + 1, par3, par4));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2, par3 - 1, par4));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2, par3 + 1, par4));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2, par3, par4 - 1));
	//			this.addToRedstoneUpdateList(new ChunkPosition(par2, par3, par4 + 1));
	//		}
	//	}
	//
	//	/**
	//	 * Calls World.notifyBlocksOfNeighborChange() for all neighboring blocks,
	//	 * but only if the given block is a redstone
	//	 * wire.
	//	 */
	//	private void notifyWireNeighborsOfNeighborChange(World par1World, int par2, int par3, int par4)
	//	{
	//		if (par1World.getBlockId(par2, par3, par4) == this.blockID || par1World.getBlockId(par2, par3, par4) == Block.redstoneWire.blockID)
	//		{
	//			par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
	//		}
	//	}
	//
	//	/**
	//	 * Called whenever the block is added into the world. Args: world, x, y, z
	//	 */
	//	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	//	{
	//		super.onBlockAdded(par1World, par2, par3, par4);
	//
	//		if (!par1World.isRemote)
	//		{
	//			this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3, par4);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3, par4);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 - 1);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 + 1);
	//
	//			if (par1World.isBlockNormalCube(par2 - 1, par3, par4))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 + 1, par4);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 - 1, par4);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2 + 1, par3, par4))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 + 1, par4);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 - 1, par4);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2, par3, par4 - 1))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 - 1);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 - 1);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2, par3, par4 + 1))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 + 1);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 + 1);
	//			}
	//		}
	//	}
	//
	//	/**
	//	 * ejects contained items into the world, and notifies neighbors of an
	//	 * update, as appropriate
	//	 */
	//	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	//	{
	//		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	//
	//		if (!par1World.isRemote)
	//		{
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
	//			par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
	//			this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3, par4);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3, par4);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 - 1);
	//			this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3, par4 + 1);
	//
	//			if (par1World.isBlockNormalCube(par2 - 1, par3, par4))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 + 1, par4);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 - 1, par3 - 1, par4);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2 + 1, par3, par4))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 + 1, par4);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2 + 1, par3 - 1, par4);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2, par3, par4 - 1))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 - 1);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 - 1);
	//			}
	//
	//			if (par1World.isBlockNormalCube(par2, par3, par4 + 1))
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 + 1, par4 + 1);
	//			}
	//			else
	//			{
	//				this.notifyWireNeighborsOfNeighborChange(par1World, par2, par3 - 1, par4 + 1);
	//			}
	//		}
	//	}
	//
	//	/**
	//	 * Returns the current strength at the specified block if it is greater than
	//	 * the passed value, or the passed value
	//	 * otherwise. Signature: (world, x, y, z, strength)
	//	 */
	//	private int getMaxCurrentStrength(World par1World, int par2, int par3, int par4, int par5)
	//	{
	//		if (par1World.getBlockId(par2, par3, par4) != this.blockID && par1World.getBlockId(par2, par3, par4) != Block.redstoneWire.blockID)
	//		{
	//			return par5;
	//		}
	//		else
	//		{
	//			int i1 = par1World.getBlockMetadata(par2, par3, par4);
	//			return i1 > par5 ? i1 : par5;
	//		}
	//	}
	//
	//	/**
	//	 * Lets the block know when one of its neighbor changes. Doesn't know which
	//	 * neighbor changed (coordinates passed are
	//	 * their own) Args: x, y, z, neighbor blockID
	//	 */
	//	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	//	{
	//		if (!par1World.isRemote)
	//		{
	//			boolean flag = this.canPlaceBlockAt(par1World, par2, par3, par4);
	//
	//			if (flag)
	//			{
	//				this.updateAndPropagateCurrentStrength(par1World, par2, par3, par4);
	//			}
	//			else
	//			{
	//				this.dropBlockAsItem(par1World, par2, par3, par4, 0, 0);
	//				par1World.setBlockToAir(par2, par3, par4);
	//			}
	//
	//			super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
	//		}
	//	}
	//
	//	/**
	//	 * Returns true if the block is emitting direct/strong redstone power on the
	//	 * specified side. Args: World, X, Y, Z,
	//	 * side. Note that the side is reversed - eg it is 1 (up) when checking the
	//	 * bottom of the block.
	//	 */
	//	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
	//	{
	//		return !this.canProvidePower() ? 0 : this.isProvidingWeakPower(world, x, y, z, side);
	//	}
	//
	//	/**
	//	 * Returns true if the block is emitting indirect/weak redstone power on the
	//	 * specified side. If isBlockNormalCube
	//	 * returns true, standard redstone propagation rules will apply instead and
	//	 * this will not be called. Args: World, X,
	//	 * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when
	//	 * checking the bottom of the block.
	//	 */
	//	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z,
	//			int par5)
	//	{
	//		if (!this.canProvidePower())
	//		{
	//			return 0;
	//		}
	//		else
	//		{
	//			int i1 = world.getBlockMetadata(x, y, z);
	//
	//			if (i1 == 0)
	//			{
	//				return 0;
	//			}
	//			else if (par5 == 1)
	//			{
	//				return i1;
	//			}
	//			else
	//			{
	//				boolean flag = Block.redstoneWire.isPoweredOrRepeater(world, x - 1, y, z, 1)
	//						|| !world.isBlockNormalCube(x - 1, y, z)
	//						&& Block.redstoneWire.isPoweredOrRepeater(world, x - 1, y - 1, z, -1);
	//				boolean flag1 = Block.redstoneWire.isPoweredOrRepeater(world, x + 1, y, z, 3)
	//						|| !world.isBlockNormalCube(x + 1, y, z)
	//						&& Block.redstoneWire.isPoweredOrRepeater(world, x + 1, y - 1, z, -1);
	//				boolean flag2 = Block.redstoneWire.isPoweredOrRepeater(world, x, y, z - 1, 2)
	//						|| !world.isBlockNormalCube(x, y, z - 1)
	//						&& Block.redstoneWire.isPoweredOrRepeater(world, x, y - 1, z - 1, -1);
	//				boolean flag3 = Block.redstoneWire.isPoweredOrRepeater(world, x, y, z + 1, 0)
	//						|| !world.isBlockNormalCube(x, y, z + 1)
	//						&& Block.redstoneWire.isPoweredOrRepeater(world, x, y - 1, z + 1, -1);
	//
	//				if (!world.isBlockNormalCube(x, y + 1, z))
	//				{
	//					if (world.isBlockNormalCube(x - 1, y, z)
	//							&& Block.redstoneWire.isPoweredOrRepeater(world, x - 1, y + 1, z, -1))
	//					{
	//						flag = true;
	//					}
	//
	//					if (world.isBlockNormalCube(x + 1, y, z)
	//							&& Block.redstoneWire.isPoweredOrRepeater(world, x + 1, y + 1, z, -1))
	//					{
	//						flag1 = true;
	//					}
	//
	//					if (world.isBlockNormalCube(x, y, z - 1)
	//							&& Block.redstoneWire.isPoweredOrRepeater(world, x, y + 1, z - 1, -1))
	//					{
	//						flag2 = true;
	//					}
	//
	//					if (world.isBlockNormalCube(x, y, z + 1)
	//							&& Block.redstoneWire.isPoweredOrRepeater(world, x, y + 1, z + 1, -1))
	//					{
	//						flag3 = true;
	//					}
	//				}
	//
	//				return !flag2 && !flag1 && !flag && !flag3 && par5 >= 2 && par5 <= 5 ? i1
	//						: (par5 == 2 && flag2 && !flag && !flag1 ? i1 : (par5 == 3 && flag3
	//								&& !flag && !flag1 ? i1
	//								: (par5 == 4 && flag && !flag2 && !flag3 ? i1 : (par5 == 5 && flag1
	//										&& !flag2 && !flag3 ? i1 : 0))));
	//			}
	//		}
	//	}
	//
	//	
	//	public static ArrayList<ChunkPosition> getRedstoneUpdateList()
	//	{
	//		Set set = ObfuscationReflectionHelper.getPrivateValue(BlockRedstoneWire.class, Block.redstoneWire, 1);
	//		ArrayList<ChunkPosition> list = new ArrayList(set);
	//		set.clear();
	//		return list;
	//	}
	//	
	//	public static void addToRedstoneUpdateList(ChunkPosition pos)
	//	{
	//		Set set = ObfuscationReflectionHelper.getPrivateValue(BlockRedstoneWire.class, Block.redstoneWire, 1);
	//		set.add(pos);
	//	}
	//
	@SideOnly(Side.CLIENT)
	/**
	 * A randomly called display update to be able to add particles or other items for display
	 */
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		int l = par1World.getBlockMetadata(par2, par3, par4);

		if (l > 0)
		{
			double d0 = (double) par2 + 0.5D + ((double) par5Random.nextFloat() - 0.5D) * 0.2D;
			double d1 = (double) ((float) par3 + 0.0625F);
			double d2 = (double) par4 + 0.5D + ((double) par5Random.nextFloat() - 0.5D) * 0.2D;
			float f = (float) l / 15.0F;
			float f1 = f * 0.6F + 0.4F;

			if (l == 0)
			{
				f1 = 0.0F;
			}

			float f2 = f * f * 0.7F - 0.5F;
			float f3 = f * f * 0.6F - 0.7F;

			if (f2 < 0.0F)
			{
				f2 = 0.0F;
			}

			if (f3 < 0.0F)
			{
				f3 = 0.0F;
			}

			par1World.spawnParticle("reddust", d0, d1, d2, (double) f1, (double) f2, (double) f3);
		}
	}

}

// ------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------
// OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- OLD CODE -- 
// ------------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------------
//
///**
// * @author AbrarSyed
// */
//public class BlockCamoWire extends BlockCamoFull
//{
//	public BlockCamoWire(int i)
//	{
//		super(i, Material.circuits);
//		setHardness(1.5F);
//		setStepSound(Block.soundWoodFootstep);
//	}
//
//	@Override
//	public void addCreativeItems(ArrayList itemList)
//	{
//		itemList.add(new ItemStack(this));
//	}
//
//	@Override
//	public Icon getBlockTextureFromSideAndMetadata(int i, int j)
//	{
//		if (i == 3)
//			return Block.redstoneWire.getBlockTextureFromSide(j);
//		else
//			return this.blockIcon;
//	}
//
//	@Override
//	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
//	{
//		if (l == 0)
//			return;
//
//		if (Block.blocksList[l].canProvidePower() || l == blockID)
//		{
//			// System.out.println(i+" "+j+" "+k+"  notified");
//			boolean isPoweredOld = getPoweredState(world, i, j, k) > 0;
//			boolean isPowered = world.isBlockGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j, k);
//
//			// old and new different?
//			if (isPowered != isPoweredOld)
//			{
//				// if new is powerred.. turn on.
//				if (isPowered)
//				{
//					turnOn(world, i, j, k);
//					// else turnoff
//				}
//				else
//				{
//					turnOff(world, i, j, k);
//				}
//			}
//			else
//			{
//				// old and new are the same.
//
//				// if its off, leave it off.
//				if (!isPowered)
//					return;
//
//				// otherwise.. lets see if we have a new power-er.
//				if (!isPoweredFromAllowedDir(world, i, j, k))
//				{
//					turnOff(world, i, j, k);
//				}
//			}
//		}
//	}
//
//	@Override
//	public boolean isProvidingWeakPower(IBlockAccess world, int i, int j, int k, int l)
//	{
//		if (getPoweredState(world, i, j, k) == 0)
//			return false;
//		else
//			return isProvidingStrongPower(world, i, j, k, l);
//	}
//
//	@Override
//	public boolean isProvidingStrongPower(IBlockAccess iblockaccess, int i, int j, int k, int l)
//	{
//		if (iblockaccess.getBlockMetadata(i, j, k) == 0)
//			return false;
//
//		boolean worked = (iblockaccess.getBlockMetadata(i, j, k) & 7) != getOppositeSide(l);
//		// System.out.println("check power to "+l+"  is "+worked);
//		return worked;
//	}
//
//	@Override
//	public boolean canProvidePower()
//	{
//		return true;
//	}
//
//	@Override
//	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
//	{
//		return Block.blocksList[blockID].canProvidePower() && side != -1;
//	}
//
//	private void turnOff(World world, int i, int j, int k)
//	{
//		world.setBlockMetadataWithNotify(i, j, k, 0);
//		notifyWireNeighborsOfNeighborChange(world, i, j, k);
//		world.markBlockForUpdate(i, j, k);
//		// System.out.println("Turned off");
//	}
//
//	private void turnOn(World world, int i, int j, int k)
//	{
//		boolean[] flags = new boolean[6];
//
//		for (int x = 0; x < 6; x++)
//		{
//			flags[x] = isBlockPoweredBy(world, i, j, k, x);
//		}
//
//		byte poweredSide = -1;
//
//		for (byte x = 0; x < 6; x++)
//			if (flags[x])
//			{
//				poweredSide = x;
//			}
//
//		if (poweredSide >= 0)
//		{
//			world.setBlockMetadataWithNotify(i, j, k, 8 + poweredSide);
//			notifyWireNeighborsOfNeighborChange(world, i, j, k);
//			world.markBlockForUpdate(i, j, k);
//			// System.out.println(i+" "+j+" "+k+" Turned on: direction is "+poweredSide);
//		}
//	}
//
//	private boolean isPoweredFromAllowedDir(World world, int i, int j, int k)
//	{
//		byte dir = (byte) getPoweredDirectionFromMetadata(world, i, j, k);
//		boolean[] flags = new boolean[6];
//
//		for (int x = 0; x < 6; x++)
//		{
//			flags[x] = isBlockPoweredBy(world, i, j, k, x);
//		}
//
//		if (flags[dir])
//			// System.out.println("It is bieng powered by "+dir);
//			return true;
//
//		// System.out.println("It is NOT bieng powered by "+dir);
//		return false;
//	}
//
//	private boolean isBlockPoweredBy(World world, int i, int j, int k, int l)
//	{
//		int newX, newY, newZ;
//
//		ForgeDirection dir = ForgeDirection.getOrientation(l);
//		int opposite = dir.getOpposite().ordinal();
//		newX = i + dir.offsetX;
//		newY = j + dir.offsetY;
//		newZ = k + dir.offsetZ;
//
//		int id = world.getBlockId(newX, newY, newZ);
//
//		if (id == 0)
//			return false;
//
//		if (id == Block.redstoneWire.blockID && world.getBlockMetadata(newX, newY, newZ) > 0)
//			return true;
//		else if (id == blockID && opposite != (world.getBlockMetadata(newX, newY, newZ) & 7) && (world.getBlockMetadata(newX, newY, newZ) & 8) > 0)
//			// System.out.println("Checked as Block");
//			return true;
//
//		Block block = Block.blocksList[id];
//
//		if (block.canProvidePower() && (block.isProvidingStrongPower(world, newX, newY, newZ, opposite) || block.isProvidingWeakPower(world, newX, newY, newZ, opposite)))
//			return true;
//
//		return false;
//	}
//
//	private void notifyWireNeighborsOfNeighborChange(World world, int i, int j, int k)
//	{
//		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
//		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
//		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
//		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
//		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
//		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
//		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
//		return;
//	}
//
//	private int getPoweredDirectionFromMetadata(World world, int i, int j, int k)
//	{
//		return world.getBlockMetadata(i, j, k) & 7;
//	}
//
//	private int getPoweredState(IBlockAccess world, int i, int j, int k)
//	{
//		return world.getBlockMetadata(i, j, k) & 8;
//	}
//
//	private int getOppositeSide(int i)
//	{
//		switch (i)
//			{
//				case 0:
//					return 1;
//
//				case 1:
//					return 0;
//
//				case 2:
//					return 3;
//
//				case 3:
//					return 2;
//
//				case 4:
//					return 5;
//
//				default:
//					return 4;
//			}
//	}
//}
