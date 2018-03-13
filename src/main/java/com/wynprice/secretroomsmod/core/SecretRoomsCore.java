package com.wynprice.secretroomsmod.core;

import java.util.Map;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * CoreMod for SecretRoomsMod
 * @author Wyn Price
 *
 */
@IFMLLoadingPlugin.Name(value = "SecretRoomsMod-Core")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"com.wynprice.secretroomsmod.core"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class SecretRoomsCore implements IFMLLoadingPlugin {

    public static boolean isDebofEnabled = false;

    public SecretRoomsCore() {
        FMLLog.info("[SecretRoomsMod-Core] Core loaded");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"com.wynprice.secretroomsmod.core.SecretRoomsTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isDebofEnabled = (boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}