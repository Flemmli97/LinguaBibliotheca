package io.github.flemmli97.linguabib.network;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.client.ClientLanguageHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class S2CLangData implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(LinguaBib.MODID, "s2c_language_data");

    private final Map<String, String> fallback;
    private final Map<String, String> language;

    public S2CLangData(Map<String, String> fallback, Map<String, String> language) {
        this.fallback = fallback;
        this.language = language;
    }

    public static S2CLangData read(FriendlyByteBuf buf) {
        return new S2CLangData(buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf),
                buf.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readUtf));
    }

    public static void handle(S2CLangData pkt) {
        ClientLanguageHandler.updateLanguage(pkt.fallback, pkt.language);
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeMap(this.fallback, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
        buf.writeMap(this.language, FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeUtf);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
