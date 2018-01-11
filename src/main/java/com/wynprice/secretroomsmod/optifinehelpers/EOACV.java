package com.wynprice.secretroomsmod.optifinehelpers;

public enum EOACV//Shorter than EnumOptifineArrayCacheVersion
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