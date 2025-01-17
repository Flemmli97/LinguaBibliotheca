package io.github.flemmli97.linguabib.fabric;

import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class PlatformImpl implements Platform {

    @Override
    public boolean hasRemote(ServerPlayer player) {
        return ServerPlayNetworking.canSend(player, S2CLangData.TYPE);
    }

    @Override
    public void sendTo(CustomPacketPayload pkt, ServerPlayer player) {
        if (!ServerPlayNetworking.canSend(player, pkt.type()))
            return;
        ServerPlayNetworking.send(player, pkt);
    }
}
