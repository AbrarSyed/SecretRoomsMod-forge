package com.wynprice.secretroomsmod;

import java.io.File;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

public class SecretConfig 
{		
	public static final Configuration CONFIG = new Configuration(new File(Loader.instance().getConfigDir(), SecretRooms5.MODID + ".cfg"));
	public static final String CATEGORYGENRAL = "general";
	public static final String CATEGORYENERGIZEDPASTE = "energized_paste";
	
	public static boolean updateChecker;
	public static boolean forceAO;
	public static String[] accepted_optifine_versions;
	
	public static String[] energized_blacklist_mirror;
	public static String[] energized_non_replacable;
	
	public static String sound_set_name;
	public static double sound_set_vol, sound_set_pitch;
	
	public static String sound_use_name;
	public static double sound_use_vol, sound_use_pitch;
	
	{
		SecretConfig.syncConfig();
	}
	
	public static void syncConfig()
	{
		CONFIG.load(); 
		Property doUpdate = CONFIG.get(CATEGORYGENRAL, "update_checker", true, new TextComponentTranslation("config.do_update.comment").getUnformattedText());
		Property doesForcedAO = CONFIG.get(CATEGORYGENRAL, "force_ambient_occlusion", false, new TextComponentTranslation("config.ao.comment").getUnformattedText());
		Property optifineVersions = CONFIG.get(CATEGORYGENRAL, "accepted_optifine_versions", new String[]{"OptiFine_1.12.2_HD_U_C6","OptiFine_1.12.2_HD_U_C7","OptiFine_1.12.2_HD_U_C8"}, new TextComponentTranslation("config.oflist").getUnformattedComponentText().replace("<newline>", "\n"));
		
		Property energized_paste_blacklist_pickup = CONFIG.get(CATEGORYENERGIZEDPASTE, "blacklist_mirror", new String[0],  new TextComponentTranslation("config.energizedpaste.blacklistpickup").getUnformattedComponentText().replace("<newline>", "\n"));
		Property energized_paste_nonreplaceable = CONFIG.get(CATEGORYENERGIZEDPASTE, "non_replaceable", new String[0],  new TextComponentTranslation("config.energizedpaste.nonreplaceable").getUnformattedComponentText().replace("<newline>", "\n"));
		
		Property p_sound_set_name = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_set_name", "minecraft:block.sand.place",  new TextComponentTranslation("config.energizedpaste.sound.set").getUnformattedComponentText());
		Property p_sound_set_vol = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_set_volume", 0.5d);
		Property p_sound_set_pitch = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_set_pitch", 3d);
		
		Property p_sound_use_name = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_use_name", "minecraft:block.slime.break",  new TextComponentTranslation("config.energizedpaste.sound.use").getUnformattedComponentText());
		Property p_sound_use_vol = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_use_volume", 0.2d);
		Property p_sound_use_pitch = CONFIG.get(CATEGORYENERGIZEDPASTE, "sound_use_pitch", 3d);

		
		updateChecker = doUpdate.getBoolean();
		forceAO = doesForcedAO.getBoolean();
		accepted_optifine_versions = optifineVersions.getStringList();
		
		energized_blacklist_mirror = energized_paste_blacklist_pickup.getStringList();
		energized_non_replacable = energized_paste_nonreplaceable.getStringList();
		
		sound_set_name = p_sound_set_name.getString();
		sound_set_vol = p_sound_set_vol.getDouble();
		sound_set_pitch = p_sound_set_pitch.getDouble();
		
		sound_use_name = p_sound_use_name.getString();
		sound_use_vol = p_sound_use_vol.getDouble();
		sound_use_pitch = p_sound_use_pitch.getDouble();

		
		doUpdate.set(updateChecker);
		doesForcedAO.set(forceAO);
		optifineVersions.set(accepted_optifine_versions);
		
		energized_paste_blacklist_pickup.set(energized_blacklist_mirror);
		energized_paste_nonreplaceable.set(energized_non_replacable);

		p_sound_set_name.set(sound_set_name);
		p_sound_set_vol.set(sound_set_vol);
		p_sound_set_pitch.set(sound_set_pitch);

		p_sound_use_name.set(sound_use_name);
		p_sound_use_vol.set(sound_use_vol);
		p_sound_use_pitch.set(sound_use_pitch);

		if(CONFIG.hasChanged())
			CONFIG.save();
	}
}
