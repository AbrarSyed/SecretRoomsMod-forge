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

import java.util.UUID;

import com.github.abrarsyed.secretroomsmod.api.BlockHolder;
import com.github.abrarsyed.secretroomsmod.api.ITileEntityCamo;
import com.github.abrarsyed.secretroomsmod.common.BlockLocation;
import com.github.abrarsyed.secretroomsmod.common.OwnershipManager;

import net.malisis.doors.door.tileentity.DoorTileEntity;

/**
 * @author Ordinastie
 *
 */
public class CamoDoorTileEntity extends DoorTileEntity implements ITileEntityCamo
{

    @Override
    public boolean[] getIsCamo()
    {
        return new boolean[] {true, true, true, true, true, true};
    }

    @Override
    public void setIsCamo(boolean[] camo) { }

    @Override
    public UUID getOwner()
    {
        return OwnershipManager.getOwner(new BlockLocation(worldObj, xCoord, yCoord + 1, zCoord));
    }

    @Override
    public void setOwner(UUID id)
    {
        OwnershipManager.setOwnership(id, new BlockLocation(worldObj, xCoord, yCoord, zCoord));
        OwnershipManager.setOwnership(id, new BlockLocation(worldObj, xCoord, yCoord + 1, zCoord));
    }

    @Override
    public int getXCoord()
    {
        // TODO Auto-generated method stub
        return xCoord;
    }

    @Override
    public int getYCoord()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getZCoord()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public BlockHolder getBlockHolder()
    {
        return new BlockHolder(worldObj, xCoord, yCoord - 1, zCoord);
    }

    @Override
    public void setBlockHolder(BlockHolder holder)
    {
        // nothing
    }

}
