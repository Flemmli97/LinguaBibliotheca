package io.github.flemmli97.linguabib.fabric;

import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PacketRegister {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(S2CLangData.TYPE, S2CLangData.STREAM_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(S2CLangData.TYPE, (pkt, ctx) -> S2CLangData.handle(pkt));
    }
}
