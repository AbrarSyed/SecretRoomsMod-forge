package com.wynprice.secretroomsmod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = SecretRooms5.MODID, category = "")
@EventBusSubscriber(modid=SecretRooms5.MODID)
public final class SecretConfig {

    public static final General GENERAL = new General();
    public static final EnergizedPaste ENERGIZED_PASTE = new EnergizedPaste();
    public static final SRMBlockFunctionality BLOCK_FUNCTIONALITY = new SRMBlockFunctionality();
    
    public static final class General {
        @Config.Name("update_checker")
        @Config.Comment("Check for mod updates on startup")
        public boolean updateChecker = true;

        @Config.Name("survival_mode_helmet")
        @Config.Comment("Allow the helmet to be used in survival mode")
        public boolean survivalModeHelmet = true;
    }

    public static final class EnergizedPaste {
        @Config.Name("mirror_blacklist")
        @Config.Comment("Blacklist of blocks that should not be mirrored by energized paste")
        public String[] blacklistMirror = {};

        @Config.Name("replacement_blacklist")
        @Config.Comment("Blacklist of blocks that should not be replaced by energized paste")
        public String[] replacementBlacklist = {};

        @Config.Name("tile_entity_whitelist")
        @Config.Comment("Whitelist of blocks with tile entities that can be copied by energized paste\nTo apply to a whole mod, do 'modid:*'")
        public String[] tileEntityWhitelist = {};

        @Config.Comment("The Sound, Volume and Pitch to play when a block is set to the Energized Paste")
        @Config.Name("sound_set_name")
        public String soundSetName = "minecraft:block.sand.place";

        @Config.Name("sound_set_volume")
        public double soundSetVolume = 0.5D;

        @Config.Name("sound_set_pitch")
        public double soundSetPitch = 3.0D;

		@Config.Comment("The Sound, Volume and Pitch to play when Energized Paste is used on another block, changing the appereance of that block.")
        @Config.Name("sound_use_name")
        public String soundUseName = "minecraft:block.slime.break";

        @Config.Name("sound_use_volume")
        public double soundUseVolume = 0.2D;

        @Config.Name("sound_use_pitch")
        public double soundUsePitch = 3.0D;
    }
 
    public static final class SRMBlockFunctionality {
    	
    	@Config.Comment("Should SRM attempt to copy the light value of the block its mirroring")
    	@Config.Name("copy_light")
    	public boolean copyLight = true;
    	
    	@Config.Comment("Should SRM be limited to only full blocks (Like the Classic SecretRoomsMod)")
    	@Config.Name("only_full_blocks")
    	public boolean onlyFullBlocks = false;
    	
    	
    }
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(event.getConfigID() != null && event.getConfigID().equals(SecretRooms5.MODID)) {
            ConfigManager.sync(SecretRooms5.MODID, Config.Type.INSTANCE);
        }
    }
    
}