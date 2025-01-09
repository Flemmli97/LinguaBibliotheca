package io.github.flemmli97.linguabib.forge.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.resource.PathResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Forge packs always delegate to client type for language which breaks everything
 */
@Mixin(PathResourcePack.class)
public abstract class PathResourcePackMixin extends AbstractPackResources {

    private PathResourcePackMixin(File file) {
        super(file);
    }

    @Inject(method = "getResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Ljava/io/InputStream;", at = @At("HEAD"), cancellable = true)
    private void langRes(PackType type, ResourceLocation location, CallbackInfoReturnable<InputStream> info) throws IOException {
        if (location.getPath().startsWith("lang/")) {
            info.setReturnValue(super.getResource(type, location));
        }
    }

    @Inject(method = "hasResource(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Z", at = @At("HEAD"), cancellable = true)
    private void langHasRes(PackType type, ResourceLocation location, CallbackInfoReturnable<Boolean> info) {
        if (location.getPath().startsWith("lang/")) {
            info.setReturnValue(super.hasResource(type, location));
        }
    }
}
