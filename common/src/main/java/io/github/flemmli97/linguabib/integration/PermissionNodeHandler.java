package io.github.flemmli97.linguabib.integration;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import io.github.flemmli97.linguabib.LinguaBib;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class PermissionNodeHandler {

    public static final String cmdReload = "lingua_bib.command.reload";
    public static final String cmdLanguage = "lingua_bib.command.language";
    public static final String cmdTranslate = "lingua_bib.command.translate";

    public static boolean perm(CommandSourceStack src, String perm) {
        return perm(src, perm, false);
    }

    public static boolean perm(CommandSourceStack src, String perm, boolean adminCmd) {
        if (LinguaBib.permissionAPI) {
            if (adminCmd)
                return Permissions.check(src, perm, 2);
            return Permissions.check(src, perm, true);
        }
        if (!LinguaBib.ftbRanks || !(src.getEntity() instanceof ServerPlayer player))
            return !adminCmd || src.hasPermission(2);
        return FTBRanksAPI.getPermissionValue(player, perm).asBoolean().orElse(!adminCmd || player.hasPermissions(2));
    }
}