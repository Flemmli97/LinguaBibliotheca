package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.mixinutil.TranslatationUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Shadow
    public abstract PacketListener getPacketListener();

    @Inject(method = "doSendPacket", at = @At("HEAD"))
    private void onStartSend(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, ConnectionProtocol protocol, ConnectionProtocol connectionProtocol, CallbackInfo ci) {
        if (this.getPacketListener() instanceof ServerGamePacketListenerImpl impl) {
            TranslatationUtil.context = impl.player;
        }
    }

    @Inject(method = "doSendPacket", at = @At("TAIL"))
    private void onEndSend(Packet<?> packet, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, ConnectionProtocol protocol, ConnectionProtocol connectionProtocol, CallbackInfo ci) {
        TranslatationUtil.context = null;
    }
}
