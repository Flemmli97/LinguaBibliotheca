package io.github.flemmli97.linguabib.forge;

import io.github.flemmli97.linguabib.LinguaBib;
import io.github.flemmli97.linguabib.data.Config;
import io.github.flemmli97.linguabib.data.LinguaCommands;
import io.github.flemmli97.linguabib.data.ServerLangManager;
import io.github.flemmli97.linguabib.network.PacketRegistrar;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(value = LinguaBib.MODID)
public class LinguaBibForge {

    public static final SimpleChannel DISPATCHER =
            NetworkRegistry.ChannelBuilder.named(new ResourceLocation(LinguaBib.MODID, "packets"))
                    .clientAcceptedVersions(a -> true)
                    .serverAcceptedVersions(a -> true)
                    .networkProtocolVersion(() -> "v1.0").simpleChannel();

    public LinguaBibForge() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "*", (s1, s2) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(LinguaBibForge::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(LinguaBibForge::addReloadListener);
        MinecraftForge.EVENT_BUS.addListener(LinguaBibForge::commands);
        Config.handleConfigFile(FMLPaths.CONFIGDIR.get());
        LinguaBib.ftbRanks = ModList.get().isLoaded("ftbranks");
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        PacketRegistrar.registerClientPackets(new PacketRegistrar.ClientPacketRegister() {
            @Override
            public <P> void registerMessage(int index, ResourceLocation id, Class<P> clss, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, Consumer<P> handler) {
                DISPATCHER.registerMessage(index, clss, encoder, decoder, handlerClient(handler), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
            }
        }, 0);
    }

    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(ServerLangManager.INSTANCE);
    }

    public static void commands(RegisterCommandsEvent event) {
        LinguaCommands.register(event.getDispatcher(), false);
    }

    private static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> handlerClient(Consumer<T> handler) {
        return (p, ctx) -> {
            ctx.get().enqueueWork(() -> handler.accept(p));
            ctx.get().setPacketHandled(true);
        };
    }
}
