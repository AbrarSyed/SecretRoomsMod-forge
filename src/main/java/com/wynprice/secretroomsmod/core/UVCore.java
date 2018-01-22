package com.wynprice.secretroomsmod.core;

import java.util.Map;

import com.wynprice.secretroomsmod.SecretRooms5;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(value = "SRM UV-Core")
@IFMLLoadingPlugin.MCVersion(value = "1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({"com.wynprice.secretroomsmod.core"})
@IFMLLoadingPlugin.SortingIndex(1001)
public class UVCore implements IFMLLoadingPlugin {

    public static boolean isDebofEnabled = false;

    public UVCore() {
        FMLLog.info("[UV-Core] Initialized.");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {"com.wynprice.secretroomsmod.core.UVTransformer"};
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