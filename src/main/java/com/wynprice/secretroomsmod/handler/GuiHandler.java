package com.wynprice.secretroomsmod.handler;

import com.wynprice.secretroomsmod.SecretBlocks;
import com.wynprice.secretroomsmod.render.ContainerSecretChest;
import com.wynprice.secretroomsmod.render.GuiSecretChest;
import com.wynprice.secretroomsmod.tileentity.TileEntitySecretChest;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	
	public static final int SECRET_CHEST = 0;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == SECRET_CHEST)
			return new ContainerSecretChest(new BlockPos(x, y, z), ((TileEntitySecretChest) world.getTileEntity(new BlockPos(x, y, z))).getHandler(), player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == SECRET_CHEST)
			return new GuiSecretChest(new BlockPos(x, y, z), ((TileEntitySecretChest) world.getTileEntity(new BlockPos(x, y, z))).getHandler(), player);
		return null;
	}

}
