package io.github.flemmli97.linguabib.network;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.lang.LanguageWrapper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class S2CLangData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(LinguaBib.MODID, "s2c_language_data");

    private final Map<String, String> translation;

    public S2CLangData(Map<String, String> fallback, Map<String, String> language) {
        this.translation = new HashMap<>(language);
        fallback.forEach(this.translation::putIfAbsent);
    }

    private S2CLangData(Map<String, String> translation) {
        this.translation = translation;
    }

    public static S2CLangData read(FriendlyByteBuf buf) {
        return new S2CLangData(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf));
    }

    public static void handle(S2CLangData pkt) {
        LanguageWrapper.updateServerLanguage(pkt.translation);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.translation, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
