package io.github.flemmli97.linguabib.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.flemmli97.linguabib.LinguaBib;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path CONFIG_PATH;

    public static void handleConfigFile(@Nullable Path configDir) {
        if (CONFIG_PATH == null)
            CONFIG_PATH = configDir;
        if (CONFIG_PATH == null)
            return;
        File config = CONFIG_PATH.resolve("lingua_bib.json").toFile();
        try {
            if (!config.exists()) {
                config.createNewFile();
                save(config);
            } else {
                load(config);
            }
        } catch (IOException e) {
            LinguaBib.LOGGER.error("Error handling config", e);
        }
    }

    public static void load(File config) {
        try {
            FileReader reader = new FileReader(config);
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            ServerLangManager.INSTANCE.setDefaultServerLang(GsonHelper.getAsString(obj, "default_lang", "en_us"));
            reader.close();
        } catch (IOException e) {
            LinguaBib.LOGGER.error("Error loading config", e);
        }
        save(config);
    }

    private static void save(File config) {
        JsonObject obj = new JsonObject();
        obj.addProperty("default_lang", ServerLangManager.INSTANCE.getDefaultServerLang());
        try {
            FileWriter writer = new FileWriter(config);
            GSON.toJson(obj, writer);
            writer.close();
        } catch (IOException e) {
            LinguaBib.LOGGER.error("Error saving config", e);
        }
    }
}
