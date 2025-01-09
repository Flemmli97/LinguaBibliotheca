package io.github.flemmli97.linguabib.forge.client;

import io.github.flemmli97.linguabib.forge.LinguaBibForge;
import io.github.flemmli97.linguabib.network.Packet;
import net.minecraft.client.Minecraft;

public class ForgeClientHandler {

    public static void sendPacket(Packet packet) {
        if (Minecraft.getInstance().getConnection() == null)
            return;
        LinguaBibForge.DISPATCHER.sendToServer(packet);
    }
}
