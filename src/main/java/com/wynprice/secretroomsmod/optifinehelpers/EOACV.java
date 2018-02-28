package com.wynprice.secretroomsmod.optifinehelpers;

/**
 * Shorter than EnumOptifineArrayCacheVersion. Used to get the right ArrayCache for the right version
 * @author Wyn Price
 *
 */
public enum EOACV
{
	C6, C7;
	
	public Object arrayCache;
	
	public Class arrayClass()
	{
		return arrayCache.getClass();
	}
	
	public Object newArray()
	{
		try {
			return arrayClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}