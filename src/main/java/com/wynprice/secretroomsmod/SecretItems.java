package com.wynprice.secretroomsmod;

import java.util.ArrayList;

import com.wynprice.secretroomsmod.base.BaseExposingHelmet;
import com.wynprice.secretroomsmod.base.BaseItemDoor;
import com.wynprice.secretroomsmod.items.SwitchProbe;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SecretItems 
{
	public static final Item CAMOUFLAGE_PASTE = new Item().setRegistryName(SecretRooms2.MODID, "camouflage_paste").setUnlocalizedName("camouflage_paste");
	public static final Item SECRET_WOODEN_DOOR = new BaseItemDoor(SecretBlocks.SECRET_WOODEN_DOOR, "secret_wooden_door");
	public static final Item SECRET_IRON_DOOR = new BaseItemDoor(SecretBlocks.SECRET_IRON_DOOR, "secret_iron_door");
	public static final Item SWITCH_PROBE = new SwitchProbe();
	public static final Item IRON_EXPOSING_HELMET = new BaseExposingHelmet("iron_exposing_helmet", ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD);
	public static final Item DIAMOND_EXPOSING_HELMET = new BaseExposingHelmet("diamond_exposing_helmet", ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD);

	
	public static void preInit()
	{
		regItem(CAMOUFLAGE_PASTE);
		regItem(SECRET_WOODEN_DOOR);
		regItem(SECRET_IRON_DOOR);
		regItem(SWITCH_PROBE, 1);
		regItem(IRON_EXPOSING_HELMET, 1);
		regItem(DIAMOND_EXPOSING_HELMET, 1);

	}
	
		
	public static void regRenders()
	{
		for(Item item : ALL_ITEMS)
			regRender(item);
	}
	
	public final static ArrayList<Item> ALL_ITEMS= new ArrayList<Item>();

	
	private static String[] emptyList(int size)
	{
		String[] s = new String[size];
		for(int i = 0; i < size; i++)
			s[i] = "";
		return s;
	}
	
	private static Item getItem(Item item)
	{
		return item;
	}
	
	private static void regItem(Item item)
	{
		regItem(item, 64);
	}
	
	private static void regItem(Item item, int stackSize)
	{
		ALL_ITEMS.add(item);
		item.setMaxStackSize(stackSize);
		ForgeRegistries.ITEMS.register(item);
	}
	
	
	
	private static void regRender(Item item)
	{
		item.setCreativeTab(SecretRooms2.TAB);
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
}
