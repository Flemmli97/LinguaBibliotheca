package io.github.flemmli97.linguabib;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface Platform {

    Platform INSTANCE = LinguaBib.getPlatformInstance(Platform.class,
            "io.github.flemmli97.linguabib.fabric.PlatformImpl",
            "io.github.flemmli97.linguabib.forge.PlatformImpl");

    boolean hasRemote(ServerPlayer player);

    void sendTo(CustomPacketPayload pkt, ServerPlayer player);
}
