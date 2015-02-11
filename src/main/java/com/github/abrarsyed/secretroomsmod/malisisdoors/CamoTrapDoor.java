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
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;
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
        if (!SecretRooms.displayCamo && SecretRooms.proxy.isOwner(world, x, y, z))
        {
            return blockIcon;
        }
	    
		ForgeDirection dir = getDirection(world, x, y, z);

        Block block = world.getBlock(x + dir.offsetX, y, z + dir.offsetZ);

        if (block == null || block.isAir(world, x, y - 1, z) || block == this)
            return blockIcon;

        return block.getIcon(world, x + dir.offsetX, y, z + dir.offsetZ, side);
    }

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		ForgeDirection dir = getDirection(world, x, y, z);
		return world.getBlock(x + dir.offsetX, y, z + dir.offsetZ).colorMultiplier(world, x + dir.offsetX, y, z + dir.offsetZ);
	}
	
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, stack);
        
        if (entity instanceof EntityPlayer)
        {
            OwnershipManager.setOwnership(entity.getUniqueID(), new BlockLocation(world, x, y, z));
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
        super.breakBlock(world, x, y, z, block, metadata);
        
        // remove ownership
        OwnershipManager.removeBlock(new BlockLocation(world, x, y, z));
    }
	
    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player)
    {
        if (SecretRooms.proxy.isOwner(world, x, y, z))
        {
            return new ItemStack(this);
        }
        
        // modify coordinates to get hinge Block.
        ForgeDirection dir = getDirection(world, x, y, z);
        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;
        
        Block block = world.getBlock(x, y, z);
        
        if (block == null)
            return null;
        
        return block.getPickBlock(target, world, x, y, z, player);
    }
    
    @Override
    public final ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        if (SecretRooms.proxy.isOwner(world, x, y, z))
        {
            return new ItemStack(this);
        }
        
        // modify coordinates to get hinge Block.
        ForgeDirection dir = getDirection(world, x, y, z);
        x += dir.offsetX;
        y += dir.offsetY;
        z += dir.offsetZ;
        
        Block block = world.getBlock(x, y, z);
        
        if (block == null)
            return null;
        
        return block.getPickBlock(target, world, x, y, z);
    }
}
