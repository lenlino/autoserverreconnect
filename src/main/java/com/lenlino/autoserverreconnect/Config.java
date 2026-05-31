package com.lenlino.autoserverreconnect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Autoserverreconnect.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue ENABLED = BUILDER
            .comment("Enable auto reconnect on disconnect")
            .define("enabled", true);

    private static final ForgeConfigSpec.IntValue RECONNECT_DELAY_SECONDS = BUILDER
            .comment("Seconds to wait between reconnect attempts")
            .defineInRange("reconnectDelaySeconds", 5, 1, 3600);

    private static final ForgeConfigSpec.BooleanValue ONLY_WHEN_WINDOW_ACTIVE = BUILDER
            .comment("Only attempt to reconnect while the game window is focused")
            .define("onlyWhenWindowActive", false);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;
    public static int reconnectDelaySeconds;
    public static boolean onlyWhenWindowActive;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enabled = ENABLED.get();
        reconnectDelaySeconds = RECONNECT_DELAY_SECONDS.get();
        onlyWhenWindowActive = ONLY_WHEN_WINDOW_ACTIVE.get();
    }
}
