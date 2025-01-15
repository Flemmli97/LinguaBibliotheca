package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.mixinutil.PlayerLanguageAccess;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements PlayerLanguageAccess {

    @Unique
    private String linguaBib$language = "en_us";

    /**
     * Why vanilla you no saving it...
     */
    @Inject(method = "updateOptions", at = @At("TAIL"))
    private void onOptions(ServerboundClientInformationPacket packet, CallbackInfo ci) {
        this.linguaBib$language = packet.language();
        ServerLangManager.syncServerLangs((ServerPlayer) (Object) this);
    }

    @Override
    public String linguaBib$getPlayerLanguage() {
        return this.linguaBib$language;
    }
}
