package io.github.flemmli97.linguabib.fabric.client;

import io.github.flemmli97.linguabib.fabric.PacketRegister;
import net.fabricmc.api.ClientModInitializer;

public class LinguaBibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PacketRegister.register();
    }
}
