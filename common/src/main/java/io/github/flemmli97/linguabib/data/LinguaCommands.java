package io.github.flemmli97.linguabib.data;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.flemmli97.linguabib.api.LanguageAPI;
import io.github.flemmli97.linguabib.integration.PermissionNodeHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class LinguaCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lingua_bib")
                .then(Commands.literal("reload").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.COMMAND_RELOAD, true)).executes(LinguaCommands::reloadConfig))
                .then(Commands.literal("lang").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.COMMAND_LANGUAGE)).executes(LinguaCommands::language))
                .then(Commands.literal("translate").requires(src -> PermissionNodeHandler.perm(src, PermissionNodeHandler.COMMAND_TRANSLATE, true))
                        .then(Commands.argument("translation", StringArgumentType.string())
                                .executes(LinguaCommands::translate))));
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        Config.handleConfigFile(null);
        context.getSource().sendSuccess(() -> Component.translatable("lingua_bib.reload"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int language(CommandContext<CommandSourceStack> context) {
        String language = context.getSource().getEntity() instanceof ServerPlayer player ? LanguageAPI.getPlayerLanguage(player) : LanguageAPI.defaultServerLanguage();
        context.getSource().sendSuccess(() -> Component.translatable("lingua_bib.language", language), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int translate(CommandContext<CommandSourceStack> context) {
        String key = StringArgumentType.getString(context, "translation");
        context.getSource().sendSuccess(() -> Component.translatable("lingua_bib.translate", Component.translatable(key)), true);
        return Command.SINGLE_SUCCESS;
    }
}
