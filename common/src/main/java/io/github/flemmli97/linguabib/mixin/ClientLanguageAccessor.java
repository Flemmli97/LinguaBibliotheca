package io.github.flemmli97.linguabib.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.linguabib.mixinutil.ClientLanguageUpdater;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

/**
 * This injects additional translation to the current clients language map.
 * Any translation that already exist on the client take priority and are not overwritten
 */
@Mixin(ClientLanguage.class)
public abstract class ClientLanguageAccessor implements ClientLanguageUpdater {

    @Mutable
    @Final
    @Shadow
    private Map<String, String> storage;

    @Override
    public void linguaBib$injectData(Map<String, String> fallback, Map<String, String> data) {
        Map<String, String> current = new HashMap<>(this.storage);
        data.forEach(current::putIfAbsent);
        fallback.forEach(current::putIfAbsent);
        this.storage = ImmutableMap.copyOf(current);
    }
}
