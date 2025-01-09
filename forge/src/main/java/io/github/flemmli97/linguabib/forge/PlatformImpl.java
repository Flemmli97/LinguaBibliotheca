package io.github.flemmli97.linguabib.forge;

import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.network.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class PlatformImpl implements Platform {

    @Override
    public boolean hasRemote(ServerPlayer player) {
        return LinguaBibForge.DISPATCHER.isRemotePresent(player.connection.getConnection());
    }

    @Override
    public void sendTo(Packet pkt, ServerPlayer player) {
        if (!this.hasRemote(player))
            return;
        LinguaBibForge.DISPATCHER.send(PacketDistributor.PLAYER
                .with(() -> player), pkt);
    }
}
