package io.github.flemmli97.linguabib.client;

import io.github.flemmli97.linguabib.mixinutil.ClientLanguageUpdater;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.locale.Language;

import java.util.Map;

public class ClientLanguageHandler {

    public static void updateLanguage(Map<String, String> fallback, Map<String, String> language) {
        if (Language.getInstance() instanceof ClientLanguage lang) {
            ((ClientLanguageUpdater) lang).linguaBib$injectData(fallback, language);
        }
    }
}
