package com.lenlino.autoserverreconnect.mixin;

import com.lenlino.autoserverreconnect.client.ReconnectHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.slf4j.Logger;

@Mixin(ConnectScreen.class)
public class ConnectMixin {
    @Shadow @Final static Logger LOGGER;

    @Inject(method = "startConnecting", at = @At(value = "HEAD"))
    private static void startConnectiongMixin(Screen p_279473_, Minecraft p_279200_, ServerAddress p_279150_, ServerData p_279481_, boolean p_279117_, CallbackInfo ci) {
        LOGGER.info(p_279481_.name);
        ReconnectHandler.lastServer = p_279481_;
    }
}
