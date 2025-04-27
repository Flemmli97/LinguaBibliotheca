package io.github.flemmli97.linguabib.lang;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.Map;

public class LanguageWrapper extends Language {

    private static Map<String, String> translations = Map.of();

    public static void updateServerLanguage(Map<String, String> translation) {
        translations = translation;
    }

    private final Language wrapped;

    public LanguageWrapper(Language wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getOrDefault(String key, String defaultValue) {
        String original = this.wrapped.getOrDefault(key, defaultValue);
        if (original.equals(key) && key.equals(defaultValue)) {
            String server = translations.get(key);
            if (server != null)
                return server;
        }
        return original;
    }

    @Override
    public boolean has(String id) {
        return this.wrapped.has(id) || translations.containsKey(id);
    }

    @Override
    public boolean isDefaultRightToLeft() {
        return this.wrapped.isDefaultRightToLeft();
    }

    @Override
    public FormattedCharSequence getVisualOrder(FormattedText text) {
        return this.wrapped.getVisualOrder(text);
    }
}
