package io.github.flemmli97.linguabib.data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.integration.PermissionNodeHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;

public class LinguaCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        dispatcher.register(Commands.literal("lingua_bib")
                .then(Commands.literal("reload").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.cmdReload, true)).executes(LinguaCommands::reloadConfig))
                .then(Commands.literal("lang").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.cmdLanguage)).executes(LinguaCommands::language))
                .then(Commands.literal("translate").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.cmdTranslate, true))
                        .then(Commands.argument("translation", StringArgumentType.string())
                                .executes(LinguaCommands::translate))));
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        Config.handleConfigFile(null);
        context.getSource().sendSuccess(new TranslatableComponent("lingua_bib.reload"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int language(CommandContext<CommandSourceStack> context) {
        String language = LanguageAPI.defaultServerLanguage();
        if (context.getSource().getEntity() instanceof ServerPlayer player)
            language = LanguageAPI.getPlayerLanguage(player);
        context.getSource().sendSuccess(new TranslatableComponent("lingua_bib.language", language), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int translate(CommandContext<CommandSourceStack> context) {
        String key = StringArgumentType.getString(context, "translation");
        context.getSource().sendSuccess(new TranslatableComponent("lingua_bib.translate", new TranslatableComponent(key)), true);
        return Command.SINGLE_SUCCESS;
    }
}
