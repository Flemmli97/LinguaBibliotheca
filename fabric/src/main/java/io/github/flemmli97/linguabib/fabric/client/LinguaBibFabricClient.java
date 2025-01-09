package io.github.flemmli97.linguabib.fabric.client;

import io.github.flemmli97.linguabib.network.PacketRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class LinguaBibFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PacketRegistrar.registerClientPackets(new PacketRegistrar.ClientPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler) {
                ClientPlayNetworking.registerGlobalReceiver(id, handlerClient(decoder, handler));
            }
        }, 0);
    }

    private static <T> ClientPlayNetworking.PlayChannelHandler handlerClient(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        return (client, handler1, buf, responseSender) -> {
            T pkt = decoder.apply(buf);
            client.execute(() -> handler.accept(pkt));
        };
    }
}
