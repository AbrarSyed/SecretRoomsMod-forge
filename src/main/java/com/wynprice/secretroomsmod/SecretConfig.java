package com.wynprice.secretroomsmod;

import java.io.File;

import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifineHelper;

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
		
		updateChecker = doUpdate.getBoolean();
		forceAO = doesForcedAO.getBoolean();
		accepted_optifine_versions = optifineVersions.getStringList();
		
		energized_blacklist_mirror = energized_paste_blacklist_pickup.getStringList();
		energized_non_replacable = energized_paste_nonreplaceable.getStringList();

		
		doUpdate.set(updateChecker);
		doesForcedAO.set(forceAO);
		optifineVersions.set(accepted_optifine_versions);
		
		energized_paste_blacklist_pickup.set(energized_blacklist_mirror);
		energized_paste_nonreplaceable.set(energized_non_replacable);

		
		if(CONFIG.hasChanged())
			CONFIG.save();
	}
}
