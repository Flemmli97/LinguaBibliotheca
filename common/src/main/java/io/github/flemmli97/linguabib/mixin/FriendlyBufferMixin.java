package io.github.flemmli97.linguabib.mixin;

import io.github.flemmli97.linguabib.mixinutil.TranslationUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyBufferMixin {

    @ModifyVariable(method = "writeItem", at = @At("HEAD"), argsOnly = true)
    private ItemStack onStack(ItemStack stack) {
        return TranslationUtil.modifyStack(stack);
    }
}
