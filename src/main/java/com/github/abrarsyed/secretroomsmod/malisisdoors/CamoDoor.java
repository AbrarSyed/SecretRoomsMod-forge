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

import net.malisis.doors.door.DoorDescriptor;
import net.malisis.doors.door.block.Door;
import net.malisis.doors.door.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
import com.github.abrarsyed.secretroomsmod.common.SecretRooms;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Ordinastie
 *
 */
public class CamoDoor extends Door
{
	public CamoDoor(DoorDescriptor desc)
	{
		super(desc);
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
		Block block = world.getBlock(x, y - 1, z);
		
		if (block == null || block.isAir(world, x, y-1, z) || block == this)
		    return blockIcon;
		
		if (block == Blocks.grass)
			block = Blocks.dirt;
		
		return block.getIcon(world, x, y - 1, z, side);
	}

	@Override
	public IIcon getIcon(int side, int metadata)
	{
		return blockIcon;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, stack);

		if (entity instanceof EntityPlayer)
		{
			OwnershipManager.setOwnership(entity.getUniqueID(), new BlockLocation(world, x, y, z));
			OwnershipManager.setOwnership(entity.getUniqueID(), new BlockLocation(world, x, y + 1, z));
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		super.breakBlock(world, x, y, z, block, metadata);

		// remove ownership
		OwnershipManager.removeBlock(new BlockLocation(world, x, y, z));
		OwnershipManager.removeBlock(new BlockLocation(world, x, y + 1, z));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		if ((metadata & FLAG_TOPBLOCK) != 0)
			return null;

		DoorTileEntity te = new CamoDoorTileEntity();
		te.setDescriptor(getDescriptor());
		return te;
	}

}
