package io.github.flemmli97.linguabib.data;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerLangManager extends SimpleJsonResourceReloadListener {

    private static final String DIRECTORY = "lang";
    private static final String DEFAULT_LANG = "en_us";

    private Map<String, Map<String, String>> translations = ImmutableMap.of();
    private Map<String, Map<String, Integer>> multilineTracker = ImmutableMap.of();

    private String defaultServerLang = DEFAULT_LANG;

    public static final ServerLangManager INSTANCE = new ServerLangManager();

    private ServerLangManager() {
        super(new GsonBuilder().create(), DIRECTORY);
    }

    public static void onServerLangUpdate(MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(ServerLangManager::syncServerLangs);
    }

    public static void syncServerLangs(ServerPlayer player) {
        if (LinguaBib.disableComponentMod)
            return;
        String langCode = LanguageAPI.getPlayerLanguage(player);
        if (langCode != null)
            Platform.INSTANCE.sendTo(ServerLangManager.INSTANCE.syncPacket(langCode), player);
    }

    public void setDefaultServerLang(String lang) {
        this.defaultServerLang = lang;
    }

    public String getDefaultServerLang() {
        return this.defaultServerLang;
    }

    public String getTranslationFor(String key) {
        return this.getTranslationFor(this.getDefaultServerLang(), key);
    }

    public String getTranslationFor(String language, String key) {
        return this.fetch(this.translations, language, key, key);
    }

    public int multilineCounter(String language, String key) {
        return this.fetch(this.multilineTracker, language, key, 0);
    }

    private <T> T fetch(Map<String, Map<String, T>> data, String language, String key, T or) {
        LinguaBib.LOGGER.error("fetching for {} {} {}", data, language, key);
        Map<String, T> entry = data.get(language);
        T value = entry != null ? entry.get(key) : null;
        if (value != null)
            return value;
        entry = data.get(this.defaultServerLang);
        value = entry != null ? entry.get(key) : null;
        if (value != null)
            return value;
        entry = data.get(DEFAULT_LANG);
        if (entry == null)
            return or;
        return entry.getOrDefault(key, or);
    }

    public S2CLangData syncPacket(String language) {
        return new S2CLangData(this.translations.getOrDefault(this.defaultServerLang, Map.of()), this.translations.getOrDefault(language, Map.of()));
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        HashMap<String, Map<String, String>> translations = new HashMap<>();
        HashMap<String, Map<String, Integer>> multilineCounter = new HashMap<>();
        data.forEach((fres, el) -> {
            try {
                String language = fres.getPath();
                Map<String, String> langTranslation = translations.computeIfAbsent(language, k -> new HashMap<>());
                Map<String, Integer> counter = multilineCounter.computeIfAbsent(language, k -> new HashMap<>());
                JsonObject obj = el.getAsJsonObject();
                obj.keySet().forEach((key) -> {
                    langTranslation.put(key, obj.get(key).getAsString());
                    int end = key.lastIndexOf(".");
                    String endPath = end > 0 && !key.endsWith(".") ? key.substring(end + 1) : null;
                    if (endPath != null && endPath.matches("\\d+")) {
                        try {
                            int num = Integer.parseInt(endPath) + 1;
                            String path = key.substring(0, end);
                            counter.compute(path, (k, i) -> {
                                if (i == null)
                                    return num;
                                return i < num ? num : i;
                            });
                        } catch (NumberFormatException ignored) {
                        }
                    }
                });
            } catch (Exception ex) {
                LinguaBib.LOGGER.error("Couldn't parse language file {} {}", fres, ex);
                ex.fillInStackTrace();
            }
        });
        // Verify that each line has a translation
        multilineCounter.forEach((lang, counter) -> {
            HashMap<String, List<Integer>> invalid = new HashMap<>();
            counter.forEach((path, num) -> {
                for (int i = 1; i < num; i++) {
                    String translation = path + "." + i;
                    if (translations.get(lang).get(translation) == null) {
                        invalid.computeIfAbsent(path, k -> new ArrayList<>())
                                .add(i);
                        break;
                    }
                }
            });
            if (!invalid.isEmpty()) {
                LinguaBib.LOGGER.error("Some multiline keys have no translations. Missing {}", invalid);
            }
            invalid.forEach((k, v) -> counter.remove(k));
        });
        this.translations = ImmutableMap.copyOf(translations);
        this.multilineTracker = ImmutableMap.copyOf(multilineCounter);
    }
}
