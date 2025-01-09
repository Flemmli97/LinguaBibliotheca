package io.github.flemmli97.linguabib.fabric;

import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.network.Packet;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PlatformImpl implements Platform {

    @Override
    public boolean hasRemote(ServerPlayer player) {
        return ServerPlayNetworking.canSend(player, S2CLangData.ID);
    }

    @Override
    public void sendTo(Packet pkt, ServerPlayer player) {
        if (!ServerPlayNetworking.canSend(player, pkt.getID()))
            return;
        FriendlyByteBuf buf = PacketByteBufs.create();
        pkt.write(buf);
        ServerPlayNetworking.send(player, pkt.getID(), buf);
    }
}
