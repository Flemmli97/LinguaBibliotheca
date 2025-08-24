package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.lang.LanguageWrapper;
import net.minecraft.locale.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Language.class)
public abstract class LanguageMixin {

    @ModifyVariable(method = "inject", at = @At("HEAD"), argsOnly = true)
    private static Language lang(Language orig) {
        if (!(orig instanceof LanguageWrapper))
            return new LanguageWrapper(orig);
        return orig;
    }
}
