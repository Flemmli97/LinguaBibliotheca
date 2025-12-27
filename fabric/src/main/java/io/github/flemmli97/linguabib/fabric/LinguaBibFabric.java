package io.github.flemmli97.linguabib.fabric;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.data.Config;
import io.github.flemmli97.linguabib.data.LinguaCommands;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.fabricmc.loader.api.FabricLoader;

public class LinguaBibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DataResourceLoader.get().registerReloader(ServerLangManager.ID, ServerLangManager.INSTANCE);
        PayloadTypeRegistry.playS2C().register(S2CLangData.TYPE, S2CLangData.STREAM_CODEC);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> LinguaCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> ServerLangManager.syncServerLangs(handler.player)));
        Config.handleConfigFile(FabricLoader.getInstance().getConfigDir());
        LinguaBib.disableComponentMod = FabricLoader.getInstance().isModLoaded("server_translations");
        LinguaBib.permissionAPI = FabricLoader.getInstance().isModLoaded("fabric-permissions-api-v0");
        LinguaBib.ftbRanks = FabricLoader.getInstance().isModLoaded("ftbranks");
    }
}
