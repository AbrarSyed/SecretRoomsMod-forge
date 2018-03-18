package com.wynprice.secretroomsmod.items;

import com.wynprice.secretroomsmod.SecretConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TrueSightHelmet extends ItemArmor
{

	public TrueSightHelmet(String name, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(name);
		setRegistryName(name);
	}
	
	public static boolean isHelmet(EntityPlayer player) {
		return player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() instanceof TrueSightHelmet && (player.isCreative() || player.isSpectator() || SecretConfig.General.single_player_helmet);
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isHelmet() {
		return isHelmet(Minecraft.getMinecraft().player);
	}

}
