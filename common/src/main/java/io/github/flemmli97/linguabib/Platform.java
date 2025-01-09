package io.github.flemmli97.linguabib;

import io.github.flemmli97.linguabib.network.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface Platform {

    Platform INSTANCE = LinguaBib.getPlatformInstance(Platform.class,
            "io.github.flemmli97.linguabib.fabric.PlatformImpl",
            "io.github.flemmli97.linguabib.forge.PlatformImpl");

    boolean hasRemote(ServerPlayer player);

    void sendTo(Packet pkt, ServerPlayer player);
}
