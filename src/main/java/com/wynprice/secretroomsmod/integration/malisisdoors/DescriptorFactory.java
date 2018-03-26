package com.wynprice.secretroomsmod.integration.malisisdoors;

import net.malisis.doors.DoorDescriptor;

public interface DescriptorFactory<T extends DoorDescriptor> 
{
	public T getDescriptor();
}
