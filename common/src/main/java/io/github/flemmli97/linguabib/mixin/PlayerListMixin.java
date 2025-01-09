package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.data.ServerLangManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "reloadResources", at = @At("RETURN"))
    private void onRoadingRes(CallbackInfo info) {
        ServerLangManager.onServerLangUpdate(this.server);
    }
}
