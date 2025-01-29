package io.github.flemmli97.linguabib.fabric;

import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PacketRegister {

    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(S2CLangData.TYPE, (pkt, ctx) -> S2CLangData.handle(pkt));
    }
}
