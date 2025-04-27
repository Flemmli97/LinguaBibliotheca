package io.github.flemmli97.linguabib.forge.mixin;

import io.github.flemmli97.linguabib.data.ServerLangManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.resource.PathPackResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ModernFix makes it so all language access default to {@link PackType#CLIENT_RESOURCES}
 * Forge does it too but only for {@link net.minecraft.server.packs.PathPackResources#getResource} which we don't need
 */
@Mixin(value = PathPackResources.class, priority = 900)
public abstract class PathPackResourcesMixin {

    @Shadow(remap = false)
    protected abstract Path resolve(String... paths);

    @Shadow(remap = false)
    private static String[] getPathFromLocation(PackType type, ResourceLocation location) {
        throw new AssertionError();
    }

    /**
     * Replace the PackResources.ResourceOutput capturing and resolving to correct path
     */
    @ModifyVariable(method = "listResources", at = @At(value = "HEAD"), argsOnly = true)
    private PackResources.ResourceOutput langRes(PackResources.ResourceOutput orig, PackType type, String namespace, String path) {
        if (path.equals("lang") && ServerLangManager.FETCHING_LANG) {
            return (res, o) -> {
                Path target = this.resolve(getPathFromLocation(type, res));
                orig.accept(res, () -> Files.newInputStream(target));
            };
        }
        return orig;
    }
}
