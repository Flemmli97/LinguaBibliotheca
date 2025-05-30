package io.github.flemmli97.linguabib.mixinutil;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class TranslationUtil {

    public static final AttributeKey<Connection> CONNECTION_ATTRIBUTE_KEY = AttributeKey.newInstance("linguabib:connection_ctx");
    private static final ThreadLocal<ServerPlayer> CONTEXT = new ThreadLocal<>();

    public static void handleEncode(ChannelHandlerContext ctx, boolean start) {
        if (start) {
            Connection listener = ctx.channel().attr(TranslationUtil.CONNECTION_ATTRIBUTE_KEY).get();
            if (listener != null && listener.getPacketListener() instanceof ServerGamePacketListenerImpl impl) {
                CONTEXT.set(impl.getPlayer());
            }
        } else {
            CONTEXT.remove();
        }
    }

    /**
     * Modifies the component if the player does not have the mod on the client
     * If the player has the mod on the client the client will handle translations
     */
    public static TranslatableContents modifyComponent(TranslatableContents orig) {
        ServerPlayer player = CONTEXT.get();
        if (!LinguaBib.disableComponentMod && player != null && !Platform.INSTANCE.hasRemote(player)) {
            return new TranslatableContents(orig.getKey(),
                    ServerLangManager.INSTANCE.getTranslationFor(LanguageAPI.getPlayerLanguage(player), orig.getKey()), orig.getArgs());
        }
        return orig;
    }
}
