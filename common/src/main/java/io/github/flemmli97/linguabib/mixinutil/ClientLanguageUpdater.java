package io.github.flemmli97.linguabib.mixinutil;

import java.util.Map;

public interface ClientLanguageUpdater {

    void linguaBib$injectData(Map<String, String> fallback, Map<String, String> data);
}
