package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.mixinutil.TranslatationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketEncoder.class)
public class PacketEncoderMixin {

    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;write(Lnet/minecraft/network/FriendlyByteBuf;)V"))
    private void onEncode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci) {
        TranslatationUtil.handleEncode(channelHandlerContext, true);
    }

    @Inject(method = "encode(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;Lio/netty/buffer/ByteBuf;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/Packet;write(Lnet/minecraft/network/FriendlyByteBuf;)V", shift = At.Shift.AFTER))
    private void onEncodeEnd(ChannelHandlerContext channelHandlerContext, Packet<?> packet, ByteBuf byteBuf, CallbackInfo ci) {
        TranslatationUtil.handleEncode(channelHandlerContext, false);
    }
}
