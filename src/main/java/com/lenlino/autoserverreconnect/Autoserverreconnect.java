package com.lenlino.autoserverreconnect;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Autoserverreconnect.MODID)
public class Autoserverreconnect {
    public static final String MODID = "autoserverreconnect";

    public Autoserverreconnect(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }
}
