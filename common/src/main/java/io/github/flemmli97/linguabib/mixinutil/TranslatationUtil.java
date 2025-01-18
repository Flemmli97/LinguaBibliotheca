package io.github.flemmli97.linguabib.mixinutil;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;

public class TranslatationUtil {

    public static ServerPlayer context;

    /**
     * Modifies the component if the player does not have the mod on the client
     * If the player has the mod on the client the client will handle translations
     */
    public static TranslatableContents modifyComponent(TranslatableContents orig) {
        if (!LinguaBib.disableComponentMod && context != null && !Platform.INSTANCE.hasRemote(context)) {
            return new TranslatableContents(orig.getKey(),
                    ServerLangManager.INSTANCE.getTranslationFor(LanguageAPI.getPlayerLanguage(context), orig.getKey()), orig.getArgs());
        }
        return orig;
    }
}
