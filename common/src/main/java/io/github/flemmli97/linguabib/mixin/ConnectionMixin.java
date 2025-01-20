package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.mixinutil.TranslatationUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Shadow
    private Channel channel;

    @Inject(method = "channelActive", at = @At("RETURN"))
    private void onChannel(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        if(this.channel != null)
           this.channel.attr(TranslatationUtil.CONNECTION_ATTRIBUTE_KEY)
                .set((Connection) (Object) this);
    }
}
