package com.github.AbrarSyed.SecretRooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.network.PacketDispatcher;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 * @author AbrarSyed
 */
public class BlockCamoFull extends BlockContainer
{

	protected BlockCamoFull(int par1)
	{
		super(par1, Material.wood);
		blockIndexInTexture = 0;
		this.setLightOpacity(15);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	protected BlockCamoFull(int par1, Material material)
	{
		super(par1, material);
		blockIndexInTexture = 0;
		this.setLightOpacity(15);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityCamoFull();
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
	public int getRenderType()
	{
		return SecretRooms.camoRenderId;
	}
    
    @Override
    @SideOnly(value=Side.CLIENT)
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int dir)
    {
    	if (!SecretRooms.displayCamo)
    		return getBlockTextureFromSide(dir);
    	
    	TileEntityCamoFull entity = ((TileEntityCamoFull)world.getBlockTileEntity(x, y, z));
    	int id;
    	if (entity == null)
    		id = 1;
    	else if (entity.getCopyID() <= 0)
    		id = 1;
    	else
    	{
        	id = entity.getCopyID();
        	x = entity.getCopyCoordX();
        	y = entity.getCopyCoordY();
        	z = entity.getCopyCoordZ();
    	}
    	
    	if (id == 1)
    		return Block.stone.blockIndexInTexture;
    	
    	return Block.blocksList[id].getBlockTexture(world, x, y, z, dir);
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
    public void updateBlockMetadata(World world, int i, int j, int k, int side, float something1, float something2, float something3)
    {
    	if (alreadyExists(world, i, j, k))
    		return;
    	
    	if (!world.isRemote)
    	{
    		// CAMO STUFF
    		int[] IdAndCoords = getIdCamoStyle(world, i, j , k);

    		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(i, j, k);

    		if (IdAndCoords.length <= 4)
    		{
    			entity.setCopyCoordX(IdAndCoords[1]);
    			entity.setCopyCoordY(IdAndCoords[2]);
    			entity.setCopyCoordZ(IdAndCoords[3]);
    		}

    		entity.setCopyID(IdAndCoords[0]);
    		FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().sendPacketToAllPlayers(entity.getDescriptionPacket());
    	}
    	else
    	{
    		// CAMO STUFF
    		int[] IdAndCoords = getIdCamoStyle(world, i, j , k);

    		TileEntityCamoFull entity = (TileEntityCamoFull) world.getBlockTileEntity(i, j, k);

    		if (IdAndCoords.length <= 4)
    		{
    			entity.setCopyCoordX(IdAndCoords[1]);
    			entity.setCopyCoordY(IdAndCoords[2]);
    			entity.setCopyCoordZ(IdAndCoords[3]);
    		}

    		entity.setCopyID(IdAndCoords[0]);
    	}
    }
    
    private boolean alreadyExists(World world, int i, int j, int k)
    {
    	TileEntity entityU = world.getBlockTileEntity(i, j, k);

    	if (entityU != null && entityU instanceof TileEntityCamoFull)
    	{
    		TileEntityCamoFull entity = (TileEntityCamoFull)entityU;
    		
    		if (entity.getCopyID() != 0)
    		{
    			if (entity.hasCoords())
    			{
    				int ID = world.getBlockId(entity.getCopyCoordX(), entity.getCopyCoordY(), entity.getCopyCoordZ());
    				
    				if (ID == 0)
    					return false;
    			}
    			return true;
    		}
    	}

    	return false;
    }
    
    
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	if (!SecretRooms.displayCamo)
    		return 0xffffff;
    	
        TileEntityCamoFull entity = (TileEntityCamoFull)par1IBlockAccess.getBlockTileEntity(par2, par3, par4);
        
        if (entity == null)
        	return 0xffffff;
        
        int id = entity.getCopyID();
        
        
        if (id == Block.grass.blockID)
        {
            int i = 0;
            int j = 0;
            int k = 0;

            for (int l = -1; l <= 1; l++)
            {
                for (int i1 = -1; i1 <= 1; i1++)
                {
                    int j1 = par1IBlockAccess.getBiomeGenForCoords(par2 + i1, par4 + l).getBiomeGrassColor();
                    i += (j1 & 0xff0000) >> 16;
                    j += (j1 & 0xff00) >> 8;
                    k += j1 & 0xff;
                }
            }
            return (i / 9 & 0xff) << 16 | (j / 9 & 0xff) << 8 | k / 9 & 0xff;
        }
        
        
        return 0xffffff;
    }
    
    /**
     * annalyses surrounding blocks and decides on a BlockID for the Camo Block to copy.
     * @param world
     * @param x coord
     * @param y coord
     * @param z coord
     * @return the ID of the block to be copied
     */
    private int[] getIdCamoStyle(World world, int x, int y, int z)
    {
        int[] id = new int[]{0, 0, 0 , 0};
        int[][] plusIds = new int[6][4];
        Block block;
        
        
        // Only PLUS sign id checks.
        plusIds[0] = getInfo(world, x,   y-1, z); // y-1
        plusIds[1] = getInfo(world, x,   y+1, z); // y+1
        plusIds[2] = getInfo(world, x-1, y,   z); // x-1
        plusIds[3] = getInfo(world, x+1, y,   z); // x+1
        plusIds[4] = getInfo(world, x,   y,   z-1); // z-1
        plusIds[5] = getInfo(world, x,   y,   z+1); // z+1
         
        
        // if there is only 1 in the PLUS SIGN checked.
        if (isOneLeft(truncateArrayINT(plusIds)))
        {
        	plusIds = truncateArrayINT(plusIds);
        	System.out.println("IDs worked early:  "+Arrays.toString(plusIds[0]));
        	return plusIds[0];
        }
        
        int[][] intChecks = new int[3][4];
        
        // checks Y's
        if (plusIds[0][0] == plusIds[1][0])
        	intChecks[1] = plusIds[0];
        
        //checks X's
        if (plusIds[2][0] == plusIds[3][0])
        	intChecks[0] = plusIds[2];
    	
        // checks Z's
        if (plusIds[4][0] == plusIds[5][0])
        	intChecks[2] = plusIds[4];
        
        // part of XY wall?
        if (intChecks[1][0] == intChecks[0][0] && intChecks[0][0] > 0)
        	id = intChecks[0];
        
        // part of YZ wall?
        else if (intChecks[1][0] == intChecks[2][0] && intChecks[2][0] > 0)
        	id = intChecks[1];
        
        // part of XZ floor or ceiling?
        else if (intChecks[2][0] == intChecks[0][0] && intChecks[0][0] > 0)
        	id = intChecks[0];
        
        // check Y column
        else if (intChecks[1][0] != 0)
        	id = intChecks[1];
        
        // check X row
        else if (intChecks[0][0] != 0)
        	id = intChecks[0];
        
        // check Z row
        else if (intChecks[2][0] != 0)
        	id = intChecks[2];
        
        System.out.println("IDs are fun:  "+Arrays.toString(id));
        
        if (id[0] != 0) return id;
        
        // GET MODE
        plusIds = this.truncateArrayINT(plusIds);
        
        try
        {
        	id = tallyMode(plusIds);
        }
        catch(Exception e)
        {
        	int[][] test = truncateArrayINT(plusIds);
        	if (test.length >= 1)
        		id = test[0];
        	else
        		id = new int[] {1, 0, 0, 0, 0};
        }
        
        if (id[0] == 0)
        	return new int[] {1, 0, 0, 0, 0};
        
        return id;
    }
    
    private int[] tallyMode(int[][] nums2) throws Exception
    {
    	int[] nums = new int[nums2.length];
    	
    	for (int i = 0; i < nums2.length; i++)
    	{
    		nums[i] = nums2[i][0];
    	}
    	
        // create array of tallies, all initialized to zero
        int[] tally = new int[256];

        // for each array entry, increment corresponding tally box
        for (int i = 0; i < nums.length; i++)
        {
            int value = nums[i];
            tally[value]++;
        }

        // now find the index of the largest tally - this is the mode
        int maxIndex = 0;

        for (int i = 1; i < tally.length; i++)
        {
            if (tally[i] == tally[maxIndex] && tally[i] != 0)
            {
                throw new Exception("NULL");
            }
            else if (tally[i] > tally[maxIndex])
            {
                maxIndex = i;
            }
        }

        return nums2[maxIndex];
    }
    
    /**
     * Used to specially get Ids. It returns zero if the texture cannot be copied, or if it is air.
     * @param world The world
     * @param x X coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     * @return
     */
    private static int[] getInfo(World world, int x, int y, int z)
    {
    	// if its an air block, return 0
    	if (world.isAirBlock(x, y, z))
    		return new int[] {0, 0, 0, 0};
    	else
    	{
    		int id = world.getBlockId(x, y, z);
    		Block block = Block.blocksList[id];
    		
    		if (block instanceof BlockCamoFull)
    		{
    			TileEntityCamoFull entity = (TileEntityCamoFull)world.getBlockTileEntity(x, y, z);
    			if (entity != null)
    				return new int[] {entity.getCopyID(), entity.getCopyCoordX(), entity.getCopyCoordY(), entity.getCopyCoordZ()};
    			else
    				return new int[] {0, 0, 0, 0};
    		}
    		else if (block instanceof BlockOneWay)
    			return new int[] {id, x, y, z};
    		else if (block.isOpaqueCube())
    			return new int[] {id, x, y, z};
    		else
    		{
    			block.setBlockBoundsBasedOnState(world, x, y, z);
    			double[] bounds = new double[]
    					{
    					block.minX,
    					block.minY,
    					block.minZ,
    					block.maxX,
    					block.maxY,
    					block.maxZ
    					};

    			if (block.minX == 0
    					&& block.minY == 0
    					&& block.minZ == 0
    					&& block.maxX == 1
    					&& block.maxY == 1
    					&& block.maxZ == 1
    					)
    				return new int[] {id, x, y, z};
    			else
    				return new int[] {0, 0, 0, 0};
    		}
    	}
    }
    
    /**
     * This truncates an int Array so that it contains only values above zero. The size of the array is also cut down so that all of them are full.
     * @param array The array to be truncated
     * @return the truncated array.
     */
    private static int[][] truncateArrayINT(int[][] array)
    {
        int num = 0;

        for (int[] obj: array)
        {
            if (obj[0] > 0)
            {
                num++;
            }
        }

        int[][] truncated = new int[num][4];
        num = 0;

        for (int[] obj: array)
        {
            if (obj[0] > 0)
            {
                truncated[num] = obj;
                num++;
            }
        }

        return truncated;
    }
    
    private boolean isOneLeft(int[][] textures)
    {
        if (!checkAllNull(textures))
        {
            if (truncateArrayINT(textures).length == 1)
            {
                return true;
            }
        }

        return false;
    }
    
    private boolean checkAllNull(int[][] textures)
    {
        boolean flag = true;

        for (int[] num: textures)
        {
            if (num[0] > 0)
            {
                flag = false;
            }
        }

        return flag;
    }
	
}
