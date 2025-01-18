package io.github.flemmli97.linguabib.mixinutil;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class TranslatationUtil {

    public static ServerPlayer context;

    /**
     * Modifies the component if the player does not have the mod on the client
     * If the player has the mod on the client the client will handle translations
     */
    public static Component modifyComponent(Component orig) {
        if (!LinguaBib.disableComponentMod && context != null && orig.getContents() instanceof TranslatableContents trans && !Platform.INSTANCE.hasRemote(context)) {
            return Component.translatable(ServerLangManager.INSTANCE.getTranslationFor(LanguageAPI.getPlayerLanguage(context), trans.getKey()), trans.getArgs()).setStyle(orig.getStyle());
        }
        return orig;
    }

    /**
     * Tooltip stuff are saved in strings and not component
     * Resetting them in the currrent context will automatically localize the data
     */
    public static ItemStack modifyStack(ItemStack orig) {
        CompoundTag tag;
        if (!LinguaBib.disableComponentMod && context != null && !Platform.INSTANCE.hasRemote(context) && (tag = orig.getTag()) != null && tag.contains("display")) {
            ItemStack copy = orig.copy();
            tag = copy.getTag();
            if (copy.hasCustomHoverName())
                copy.setHoverName(copy.getHoverName());
            CompoundTag display = tag.getCompound("display");
            if (display.contains("Lore", CompoundTag.TAG_LIST)) {
                ListTag lore = display.getList("Lore", CompoundTag.TAG_STRING);
                for (int i = 0; i < lore.size(); i++) {
                    Component comp = Component.Serializer.fromJson(lore.getString(i));
                    lore.set(i, StringTag.valueOf(Component.Serializer.toJson(comp)));
                }
            }
            return copy;
        }
        return orig;
    }
}
