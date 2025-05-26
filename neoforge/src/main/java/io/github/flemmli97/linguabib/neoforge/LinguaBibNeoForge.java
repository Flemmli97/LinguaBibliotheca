package io.github.flemmli97.linguabib.neoforge;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.data.Config;
import io.github.flemmli97.linguabib.data.LinguaCommands;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.network.S2CLangData;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(value = LinguaBib.MODID)
public class LinguaBibNeoForge {

    public LinguaBibNeoForge(IEventBus modBus) {
        modBus.addListener(LinguaBibNeoForge::registerPackets);
        NeoForge.EVENT_BUS.addListener(LinguaBibNeoForge::addReloadListener);
        NeoForge.EVENT_BUS.addListener(LinguaBibNeoForge::commands);
        NeoForge.EVENT_BUS.addListener(LinguaBibNeoForge::login);
        Config.handleConfigFile(FMLPaths.CONFIGDIR.get());
        LinguaBib.ftbRanks = ModList.get().isLoaded("ftbranks");
    }

    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(ServerLangManager.INSTANCE);
    }

    public static void commands(RegisterCommandsEvent event) {
        LinguaCommands.register(event.getDispatcher());
    }

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(LinguaBib.MODID).optional();
        registrar.playToClient(S2CLangData.TYPE, S2CLangData.STREAM_CODEC, (pkt, ctx) -> S2CLangData.handle(pkt));
    }

    public static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            ServerLangManager.syncServerLangs(player);
    }
}
