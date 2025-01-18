package io.github.flemmli97.linguabib.forge.data;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.api.ServerLangGen;
import net.minecraft.data.PackOutput;

public class ENLangGen extends ServerLangGen {

    public ENLangGen(PackOutput packOutput) {
        super(packOutput, LinguaBib.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("lingua_bib.reload", "Reloaded Config");
        this.add("lingua_bib.language", "Your language is set to %1$s");
        this.add("lingua_bib.translate", "Translation: %1$s");
    }
}
