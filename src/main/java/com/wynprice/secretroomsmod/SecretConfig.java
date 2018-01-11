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
	
	public static boolean updateChecker;
	public static boolean forceAO;
	public static String[] accepted_optifine_versions;
	
	{
		SecretConfig.syncConfig();
	}
	
	public static void syncConfig()
	{
		CONFIG.load(); 
		Property doUpdate = CONFIG.get(CATEGORYGENRAL, "update_checker", true, new TextComponentTranslation("config.do_update.comment").getUnformattedText());
		Property doesForcedAO = CONFIG.get(CATEGORYGENRAL, "force_ambient_occlusion", false, new TextComponentTranslation("config.ao.comment").getUnformattedText());
		Property optifineVersions = CONFIG.get(CATEGORYGENRAL, "accepted_optifine_versions", new String[]{"OptiFine_1.12.2_HD_U_C6","OptiFine_1.12.2_HD_U_C7","OptiFine_1.12.2_HD_U_C8"}, new TextComponentTranslation("config.oflist").getUnformattedComponentText().replace("<newline>", "\n"));
		
		updateChecker = doUpdate.getBoolean();
		forceAO = doesForcedAO.getBoolean();
		accepted_optifine_versions = optifineVersions.getStringList();
		
		doUpdate.set(updateChecker);
		doesForcedAO.set(forceAO);
		optifineVersions.set(accepted_optifine_versions);
		if(CONFIG.hasChanged())
			CONFIG.save();
	}
}
