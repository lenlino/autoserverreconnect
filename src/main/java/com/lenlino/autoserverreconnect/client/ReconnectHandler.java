package com.lenlino.autoserverreconnect.client;

import com.lenlino.autoserverreconnect.Autoserverreconnect;
import com.lenlino.autoserverreconnect.Config;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.net.SocketAddress;

@Mod.EventBusSubscriber(modid = Autoserverreconnect.MODID, value = Dist.CLIENT)
public class ReconnectHandler {

    public static ServerData lastServer;
    private static int countdownTicks = -1;

    private static final Logger LOGGER = LogUtils.getLogger();

    private static Button reconnectBtn;

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (!(event.getScreen() instanceof DisconnectedScreen screen)) return;

        if (lastServer == null) return;

        reconnectBtn = Button.builder(Component.literal("Reconnect Now"), b -> reconnectNow())
                .bounds(screen.width / 2 - 100, screen.height - 60, 200, 20)
                .build();
        event.addListener(reconnectBtn);

        if (Config.enabled) {
            countdownTicks = Math.max(1, Config.reconnectDelaySeconds) * 20;
        }
    }

    private static void reconnectNow() {
        countdownTicks = -1;
        ServerData server = lastServer;
        if (server == null) return;
        Minecraft mc = Minecraft.getInstance();
        ConnectScreen.startConnecting(
                new TitleScreen(),
                mc,
                ServerAddress.parseString(server.ip),
                server,
                false
        );
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (countdownTicks < 0) return;

        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof DisconnectedScreen)) {
            countdownTicks = -1;
            return;
        }
        if (Config.onlyWhenWindowActive && !mc.isWindowActive()) return;

        countdownTicks--;
        if (countdownTicks <= 0) {
            countdownTicks = -1;
            ServerData server = lastServer;
            if (server != null) {
                ConnectScreen.startConnecting(
                        new TitleScreen(),
                        mc,
                        ServerAddress.parseString(server.ip),
                        server,
                        false
                );
            }
        }
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render.Post event) {
        if (countdownTicks < 0) return;
        if (!(event.getScreen() instanceof DisconnectedScreen disconnectedScreen)) return;

        Minecraft mc = Minecraft.getInstance();
        int seconds = (countdownTicks + 19) / 20;
        Component text;
        if (Config.onlyWhenWindowActive && !mc.isWindowActive()) {
            text = Component.literal("Reconnect paused (window not focused)");
        } else {
            text = Component.literal("Reconnecting in " + seconds + "s...");
        }

        reconnectBtn.setMessage(text);
    }
}
