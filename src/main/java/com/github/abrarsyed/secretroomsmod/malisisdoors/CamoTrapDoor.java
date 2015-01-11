/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ordinastie
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.abrarsyed.secretroomsmod.malisisdoors;

import net.malisis.doors.door.block.TrapDoor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Ordinastie
 *
 */
public class CamoTrapDoor extends TrapDoor
{
	private ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
	{
		switch (world.getBlockMetadata(x, y, z) & 3)
		{
			case DIR_NORTH:
				return ForgeDirection.NORTH;
			case DIR_SOUTH:
				return ForgeDirection.SOUTH;
			case DIR_EAST:
				return ForgeDirection.EAST;
			case DIR_WEST:
				return ForgeDirection.WEST;
			default:
				return ForgeDirection.UNKNOWN;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SecretRooms.TEXTURE_BLOCK_BASE);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		ForgeDirection dir = getDirection(world, x, y, z);
		return world.getBlock(x + dir.offsetX, y, z + dir.offsetZ).getIcon(world, x + dir.offsetX, y, z + dir.offsetZ, side);
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		ForgeDirection dir = getDirection(world, x, y, z);
		return world.getBlock(x + dir.offsetX, y, z + dir.offsetZ).colorMultiplier(world, x + dir.offsetX, y, z + dir.offsetZ);
	}
}
