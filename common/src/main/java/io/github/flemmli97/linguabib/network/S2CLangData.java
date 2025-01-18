package io.github.flemmli97.linguabib.network;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.client.ClientLanguageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class S2CLangData implements CustomPacketPayload {

    public static final Type<S2CLangData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LinguaBib.MODID, "s2c_language_data"));

    public static final StreamCodec<FriendlyByteBuf, S2CLangData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CLangData decode(FriendlyByteBuf buf) {
            return new S2CLangData(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf),
                    buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, S2CLangData pkt) {
            buf.writeMap(pkt.fallback, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
            buf.writeMap(pkt.language, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
            ;
        }
    };

    private final Map<String, String> fallback;
    private final Map<String, String> language;

    public S2CLangData(Map<String, String> fallback, Map<String, String> language) {
        this.fallback = fallback;
        this.language = language;
    }

    public static void handle(S2CLangData pkt) {
        ClientLanguageHandler.updateLanguage(pkt.fallback, pkt.language);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
