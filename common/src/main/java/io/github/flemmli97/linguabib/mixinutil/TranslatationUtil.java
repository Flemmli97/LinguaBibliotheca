package io.github.flemmli97.linguabib.mixinutil;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.Platform;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;

public class TranslatationUtil {

    public static final AttributeKey<Connection> CONNECTION_ATTRIBUTE_KEY = AttributeKey.newInstance("linguabib:connection_ctx");
    private static final ThreadLocal<ServerPlayer> CONTEXT = new ThreadLocal<>();

    public static void handleEncode(ChannelHandlerContext ctx, boolean start) {
        if (start) {
            Connection listener = ctx.channel().attr(TranslatationUtil.CONNECTION_ATTRIBUTE_KEY).get();
            if (listener.getPacketListener() instanceof ServerGamePacketListenerImpl impl) {
                CONTEXT.set(impl.getPlayer());
            }
        } else {
            CONTEXT.remove();
        }
    }

    /**
     * Modifies the component if the player does not have the mod on the client
     * If the player has the mod on the client the client will handle translations
     */
    public static Component modifyComponent(Component orig) {
        ServerPlayer player = CONTEXT.get();
        if (!LinguaBib.disableComponentMod && player != null && orig.getContents() instanceof TranslatableContents trans && !Platform.INSTANCE.hasRemote(player)) {
            return Component.translatable(ServerLangManager.INSTANCE.getTranslationFor(LanguageAPI.getPlayerLanguage(player), trans.getKey()), trans.getArgs()).setStyle(orig.getStyle());
        }
        return orig;
    }

    /**
     * Tooltip stuff are saved in strings and not component
     * Resetting them in the currrent context will automatically localize the data
     */
    public static ItemStack modifyStack(ItemStack orig) {
        ServerPlayer player = CONTEXT.get();
        CompoundTag tag;
        if (!LinguaBib.disableComponentMod && player != null && !Platform.INSTANCE.hasRemote(player) && (tag = orig.getTag()) != null && tag.contains("display")) {
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
