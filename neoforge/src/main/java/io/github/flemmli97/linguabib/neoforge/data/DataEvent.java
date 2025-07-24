package io.github.flemmli97.linguabib.neoforge.data;

import io.github.flemmli97.linguabib.LinguaBib;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = LinguaBib.MODID)
public class DataEvent {

    @SubscribeEvent
    public static void data(GatherDataEvent.Server event) {
        DataGenerator data = event.getGenerator();
        data.addProvider(true, new ENLangGen(data.getPackOutput()));
    }

}
