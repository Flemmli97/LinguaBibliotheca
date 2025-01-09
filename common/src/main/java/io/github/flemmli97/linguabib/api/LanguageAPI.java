package io.github.flemmli97.linguabib.api;

import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.mixinutil.PlayerLanguageAccess;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * API for this library.
 * 99% of cases you won't need the content here.
 * If you want to have dynamic multiline translations then use this. (Vanilla does not support linebreaks in text components)
 */
public class LanguageAPI {

    public static int getLineNumbers(ServerPlayer player, String key) {
        return getLineNumbers(getPlayerLanguage(player), key);
    }

    /**
     * Returns the amount of lines for this translation key
     * <p>
     * Lines are defined as a translation key + a numeric suffix
     * <p>
     * E.g. If the lang file contains the entries:
     * <blockquote><pre>
     *  "some.translation.0": ...
     *  "some.translation.1": ...
     *  "some.translation.2": ...
     * </pre></blockquote>
     * Then "some.translation" has 3 lines
     * <p>
     * This is helpful cause vanilla does not support linebreaks when displaying components
     * so the component needs to be split up into multiple ones
     * <p>
     * <b>Explicit support for multiline translation is required. Its NOT done automatically!</b>
     *
     * @param language The language selected
     * @param key      The translation key
     * @return The amount of lines assigned to the translation. Or 0 if none
     */
    public static int getLineNumbers(String language, String key) {
        return ServerLangManager.INSTANCE.multilineCounter(language, key);
    }

    public static List<String> getFormattedKeys(ServerPlayer player, String key) {
        return getFormattedKeys(getPlayerLanguage(player), key);
    }

    /**
     * See detailed explanation above.
     * Formats the key to be used in a {@link net.minecraft.network.chat.TranslatableComponent}
     */
    public static List<String> getFormattedKeys(String language, String key) {
        int nums = getLineNumbers(language, key);
        if (nums == 0)
            return Collections.singletonList(key);
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < nums; i++) {
            keys.add(key + "." + i);
        }
        return keys;
    }

    /**
     * The current default language defined for the server
     */
    public static String getPlayerLanguage(ServerPlayer player) {
        return ((PlayerLanguageAccess) player).linguaBib$getPlayerLanguage();
    }

    /**
     * The current default language defined for the server
     */
    public static String defaultServerLanguage() {
        return ServerLangManager.INSTANCE.getDefaultServerLang();
    }
}
