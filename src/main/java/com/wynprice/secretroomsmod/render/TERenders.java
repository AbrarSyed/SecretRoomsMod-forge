package com.wynprice.secretroomsmod.render;

import com.wynprice.secretroomsmod.base.BaseTERender;
import com.wynprice.secretroomsmod.tileentity.TileEntityInfomationHolder;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretDispenser;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretPressurePlate;

public class TERenders 
{
	public static class TileEntityInfomationHolderRenderer extends BaseTERender<TileEntityInfomationHolder>{}

	public static class TileEntityInfomationHolderRendererDispenser extends BaseTERender<TileEntitySecretDispenser>{}
	
	public static class TileEntityInfomationHolderRenderPlate extends BaseTERender<TileEntitySecretPressurePlate>{}
}
