package io.github.flemmli97.linguabib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.linguabib.mixinutil.TranslatationUtil;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TranslatableContents.class)
public abstract class TranslatableContentsMixin {

    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;mapCodec(Ljava/util/function/Function;)Lcom/mojang/serialization/MapCodec;"),
            remap = false)
    private static MapCodec<TranslatableContents> modifyCodec(MapCodec<TranslatableContents> orig) {
        return orig.xmap(contents -> contents, TranslatationUtil::modifyComponent);
    }
}
