package com.wynprice.secretroomsmod;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Config;

public class SecretConfig {
	@Config(modid=SecretRooms5.MODID, category="general")
	public static class General {
		@Config.Comment("Should the Update Checker be run on startup")
		public static boolean update_checker = true;
		
		@Config.Comment("Should the helmet be available to players in singleplayer?")
		public static boolean single_player_helmet = false;
		
	}
	
	@Config(modid=SecretRooms5.MODID, category="energized_paste")
	public static class EnergizedPaste {
		
		@Config.Comment("Blocks in this list wont be able to be picked up be the Energized Camouflage Paste, so no other block can mirror this")
		public static String[] blacklist_mirror = new String[0];
		
		@Config.Comment("Blocks in this list wont be able to be replaced by the Energized Camouflage Paste, so the rendering for this block cant be changed")
		public static String[] non_replaceable = new String[0];
		
		@Config.Comment("A list of Blocks which have TileEntities, and whose model is allowed to be copied by Energized paste.\nTo apply to a whole mod, do 'modid:*', replacing modid with the Mod ID")
		public static String[] whitelisted_tileentities = {"minecraft:*"};
		
		@Config.Comment("The Sound, Volume and Pitch to play when a block is set to the Energized Paste")
		public static String sound_set_name = "minecraft:block.sand.place";
		public static double sound_set_volume = 0.5d;
		public static double sound_set_pitch = 3d;
		
		@Config.Comment("The Sound, Volume and Pitch to play when Energized Paste is used on another block, changing the appereance of that block.")
		public static String sound_use_name = "minecraft:block.slime.break";
		public static double sound_use_volume = 0.2d;
		public static double sound_use_pitch = 3d;

	}
}
