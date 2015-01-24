package com.github.abrarsyed.secretroomsmod.api;

import java.util.UUID;

public interface ITileEntityCamo
{
    /** 
     * @return an array of 6 booleans each representing if a given side of a block is camouflaged 
     */
    boolean[] getIsCamo();
    void setIsCamo(boolean[] camo);
    
    /**
     * @return the owner of the block
     */
    UUID getOwner();
    void setOwner(UUID id);
    
    int getXCoord();
    int getYCoord();
    int getZCoord();
    
    /**
     * @return the information about the copied block
     */
    BlockHolder getBlockHolder();
    void setBlockHolder(BlockHolder holder);
}
