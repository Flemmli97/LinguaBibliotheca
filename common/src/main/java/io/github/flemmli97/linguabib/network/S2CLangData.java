package io.github.flemmli97.linguabib.network;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.lang.LanguageWrapper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class S2CLangData implements CustomPacketPayload {

    public static final Type<S2CLangData> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LinguaBib.MODID, "s2c_language_data"));

    public static final StreamCodec<FriendlyByteBuf, S2CLangData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CLangData decode(FriendlyByteBuf buf) {
            return new S2CLangData(buf.readMap(FriendlyByteBuf::readUtf, (StreamDecoder<? super FriendlyByteBuf, String>) FriendlyByteBuf::readUtf));
        }

        @Override
        public void encode(FriendlyByteBuf buf, S2CLangData pkt) {
            buf.writeMap(pkt.translation, FriendlyByteBuf::writeUtf, (StreamEncoder<? super FriendlyByteBuf, String>) FriendlyByteBuf::writeUtf);
        }
    };

    private final Map<String, String> translation;

    public S2CLangData(Map<String, String> fallback, Map<String, String> language) {
        this.translation = new HashMap<>(language);
        fallback.forEach(this.translation::putIfAbsent);
    }

    private S2CLangData(Map<String, String> translation) {
        this.translation = translation;
    }

    public static void handle(S2CLangData pkt) {
        LanguageWrapper.updateServerLanguage(pkt.translation);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
