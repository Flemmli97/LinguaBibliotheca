package io.github.flemmli97.linguabib.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketRegistrar {

    public static int registerClientPackets(ClientPacketRegister register, int id) {
        register.registerMessage(id++, S2CLangData.ID, S2CLangData.class, S2CLangData::write, S2CLangData::read, S2CLangData::handle);
        return id;
    }

    public interface ClientPacketRegister {
        <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler);
    }
}
