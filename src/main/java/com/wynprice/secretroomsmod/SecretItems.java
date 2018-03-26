package com.wynprice.secretroomsmod;

import com.wynprice.secretroomsmod.base.BaseItemDoor;
import com.wynprice.secretroomsmod.integration.malisisdoors.SecretCompactMalisisDoors;
import com.wynprice.secretroomsmod.items.CamouflagePaste;
import com.wynprice.secretroomsmod.items.ProgrammableSwitchProbe;
import com.wynprice.secretroomsmod.items.SwitchProbe;
import com.wynprice.secretroomsmod.items.TrueSightHelmet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=SecretRooms5.MODID)
public class SecretItems 
{
	public static final Item CAMOUFLAGE_PASTE = new CamouflagePaste();
	public static final Item SECRET_WOODEN_DOOR = SecretCompatibility.MALISISDOORS ? SecretCompactMalisisDoors.WOODEN_DOOR_ITEM : new BaseItemDoor(SecretBlocks.SECRET_WOODEN_DOOR, "secret_wooden_door");
	public static final Item SECRET_IRON_DOOR = SecretCompatibility.MALISISDOORS ? SecretCompactMalisisDoors.IRON_DOOR_ITEM : new BaseItemDoor(SecretBlocks.SECRET_IRON_DOOR, "secret_iron_door");
	public static final Item SWITCH_PROBE = new SwitchProbe();
	public static final Item PROGRAMMABLE_SWITCH_PROBE = new ProgrammableSwitchProbe();
	public static final Item IRON_EXPOSING_HELMET = new TrueSightHelmet("iron_exposing_helmet", ItemArmor.ArmorMaterial.IRON, 2, EntityEquipmentSlot.HEAD);
	public static final Item DIAMOND_EXPOSING_HELMET = new TrueSightHelmet("diamond_exposing_helmet", ItemArmor.ArmorMaterial.DIAMOND, 3, EntityEquipmentSlot.HEAD);

	@SubscribeEvent
	public static void registryEvent(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
				CAMOUFLAGE_PASTE,
				SECRET_WOODEN_DOOR,
				SECRET_IRON_DOOR,
				SWITCH_PROBE,
				PROGRAMMABLE_SWITCH_PROBE,
				IRON_EXPOSING_HELMET,
				DIAMOND_EXPOSING_HELMET);
	}
		
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		regRender(CAMOUFLAGE_PASTE);
		regRender(CAMOUFLAGE_PASTE, 1, new ResourceLocation(SecretRooms5.MODID, "energized_camouflage_paste"));
		regRender(SECRET_WOODEN_DOOR);
		regRender(SECRET_IRON_DOOR);
		regRender(SWITCH_PROBE);
		regRender(PROGRAMMABLE_SWITCH_PROBE);
		regRender(IRON_EXPOSING_HELMET);
		regRender(DIAMOND_EXPOSING_HELMET);
	}
	
	private static void regRender(Item item)
	{
		regRender(item, 0, item.getRegistryName());
	}
	
	private static void regRender(Item item, int meta, ResourceLocation fileName)
	{
		item.setCreativeTab(SecretRooms5.TAB);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(fileName, "inventory"));
	}
	
}
