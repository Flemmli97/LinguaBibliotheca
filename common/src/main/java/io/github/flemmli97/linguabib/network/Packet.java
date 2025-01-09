package io.github.flemmli97.linguabib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface Packet {

    void write(FriendlyByteBuf buf);

    ResourceLocation getID();
}
