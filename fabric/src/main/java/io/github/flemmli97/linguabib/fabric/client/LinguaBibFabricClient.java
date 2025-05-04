package io.github.flemmli97.linguabib.fabric.client;

import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LinguaBibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(S2CLangData.TYPE, (pkt, ctx) -> S2CLangData.handle(pkt));
    }
}
