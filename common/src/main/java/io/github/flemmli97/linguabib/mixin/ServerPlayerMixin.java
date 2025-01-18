package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.mixinutil.PlayerLanguageAccess;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements PlayerLanguageAccess {

    @Shadow
    private String language;

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Inject(method = "updateOptions", at = @At("TAIL"))
    private void onOptions(ClientInformation clientInformation, CallbackInfo ci) {
        if (this.connection != null)
            ServerLangManager.syncServerLangs((ServerPlayer) (Object) this);
    }

    @Override
    public String linguaBib$getPlayerLanguage() {
        return this.language;
    }
}
