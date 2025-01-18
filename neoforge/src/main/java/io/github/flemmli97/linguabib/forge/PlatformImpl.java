package io.github.flemmli97.linguabib.forge;

import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class PlatformImpl implements Platform {

    @Override
    public boolean hasRemote(ServerPlayer player) {
        return player.connection.hasChannel(S2CLangData.TYPE);
    }

    @Override
    public void sendTo(CustomPacketPayload pkt, ServerPlayer player) {
        if (!player.connection.hasChannel(pkt.type()))
            return;
        player.connection.send(pkt);
    }
}
