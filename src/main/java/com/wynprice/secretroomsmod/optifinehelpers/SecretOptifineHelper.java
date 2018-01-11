package com.wynprice.secretroomsmod.optifinehelpers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.ArrayList;

import org.apache.logging.log4j.core.util.Loader;

import com.wynprice.secretroomsmod.SecretConfig;
import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SecretOptifineHelper 
{	
	public static String version = "null";
	public static String actualVersion = "null";
	
	public static final boolean IS_OPTIFINE = getIsOptifine();
	
	public static final ArrayList<IBlockState[]> CURRENT_C7_LIST = new ArrayList<>();
		
	private static boolean getIsOptifine()
	{
		try
		{
			
		if(!Loader.isClassAvailable("ChunkCacheOF"))
			return false;
		try 
		{
			for(String line : Files.readAllLines(new File(Minecraft.getMinecraft().mcDataDir, "logs/fml-client-latest.log").toPath()))
				for(String accVersion : SecretConfig.accepted_optifine_versions)
					if(line.contains(accVersion))
					{
						actualVersion = accVersion;
						version = accVersion.replace("OptiFine_1.12.2_HD_U_", "");							
						SecretRooms5.LOGGER.info("Using Optifine " + version);
						resetCached();
						return true;
					}
		} catch (IOException e) 
		{
			return false;
		}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static int getPositionIndex(BlockPos posFromIn, BlockPos posToIn, int subIn, BlockPos position)
	{
		return new PositionIndex(posFromIn, posToIn, subIn).getPositionIndex(position);
	}
	
	private static class PositionIndex
	{
		public final int posX;
		public final int posY;
		public final int posZ;
		public final int sizeX;
		public final int sizeY;
		public final int sizeZ;
		public final int sizeXY;
		  
		public PositionIndex(BlockPos posFromIn, BlockPos posToIn, int subIn)
		{
		    
		    int minChunkX = posFromIn.getX() - subIn >> 4;
		    int minChunkY = posFromIn.getY() - subIn >> 4;
		    int minChunkZ = posFromIn.getZ() - subIn >> 4;
		    int maxChunkX = posToIn.getX() + subIn >> 4;
		    int maxChunkY = posToIn.getY() + subIn >> 4;
		    int maxChunkZ = posToIn.getZ() + subIn >> 4;
		    
		    this.sizeX = (maxChunkX - minChunkX + 1 << 4);
		    this.sizeY = (maxChunkY - minChunkY + 1 << 4);
		    this.sizeZ = (maxChunkZ - minChunkZ + 1 << 4);
		    this.sizeXY = (this.sizeX * this.sizeY);
		    
		    this.posX = (minChunkX << 4);
		    this.posY = (minChunkY << 4);
		    this.posZ = (minChunkZ << 4);
		}
		  
		public int getPositionIndex(BlockPos pos)
		{
		    int dx = pos.getX() - this.posX;
		    if ((dx < 0) || (dx >= this.sizeX)) {
		      return -1;
		    }
		    int dy = pos.getY() - this.posY;
		    if ((dy < 0) || (dy >= this.sizeY)) {
		      return -1;
		    }
		    int dz = pos.getZ() - this.posZ;
		    if ((dz < 0) || (dz >= this.sizeZ)) {
		      return -1;
		    }
		    return dz * this.sizeXY + dy * this.sizeX + dx;
		}
	}

	
	public static boolean resetCached()
	{
		if(version.equals("C6")) return resetCached_C6();
		if(version.equals("C7")) return resetCached_C7();
		if(version.equals("C8")) return resetCached_C7();//No need to change, code stayed the same
		throw new RuntimeException("SecretRoomsMod: There is somthing wrong with the config. " + actualVersion + " is not a valid Optifine Version. The Reduced Optifine version Applied Optifine Version: " + version);
	}
	
	private static boolean resetCached_C6()
	{
		try
		{
			Field field = Class.forName("ChunkCacheOF").getDeclaredField("cacheBlockStates");
			field.setAccessible(true);
			if(field.get(null).getClass() != EOACV.C6.arrayClass())
				try {
					field.set(null, EOACV.C6.newArray());
				}
				catch (IllegalAccessException e) {
					return false;
				}

			field.setAccessible(false);
			return true;
		}
		catch (Throwable e) 
		{
			return false;
		}
	}
		
	private static boolean setC7Field(String fieldName)
	{
		try
		{
			Field field = Class.forName("ChunkCacheOF").getDeclaredField(fieldName);
			field.setAccessible(true);
			if(field.get(null).getClass() != EOACV.C7.arrayClass())
				try 
				{
					Field overrideFieldAccessorField = Field.class.getDeclaredField("overrideFieldAccessor");
					overrideFieldAccessorField.setAccessible(true);
					Object overrideFieldAccessorValue = overrideFieldAccessorField.get(field);
					Class<?> unsafeFieldAccessorImplClass = Class.forName("sun.reflect.UnsafeFieldAccessorImpl");
					Field isFinalField = unsafeFieldAccessorImplClass.getDeclaredField("isFinal");
					isFinalField.setAccessible(true);
					isFinalField.set(overrideFieldAccessorValue, false);
					Class<?> unsafeQualifiedStaticFieldAccessorImplClass = Class.forName("sun.reflect.UnsafeQualifiedStaticFieldAccessorImpl");
					Field isReadOnlyField = unsafeQualifiedStaticFieldAccessorImplClass.getDeclaredField("isReadOnly");
					isReadOnlyField.setAccessible(true);
					isReadOnlyField.set(overrideFieldAccessorValue, false);
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				    field.set(null, EOACV.C7.newArray());
				}
				catch (Throwable e) 
				{
					e.printStackTrace();
					return false;
				}

			field.setAccessible(false);
			return true;
		}
		catch (Throwable e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean resetCached_C7()
	{
		setC7Field("combinedLights");
		return setC7Field("cacheBlockStates");
	}
}
