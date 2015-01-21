package com.github.abrarsyed.secretroomsmod.api;

import java.util.UUID;

public interface ITileEntityCamo
{
    /** gets an array of 6 booleans each represneting if a given side of a block is camoflaged */
    boolean[] getIsCamo();
    void setIsCamo(boolean[] camo);
    
    /** Get the owner of the block */
    UUID getOwner();
    void setOwner(UUID id);
    
    int getXCoord();
    int getYCoord();
    int getZCoord();
    
    /** the information about the copied block */
    BlockHolder getBlockHolder();
    void setBlockHolder(BlockHolder holder);
}
