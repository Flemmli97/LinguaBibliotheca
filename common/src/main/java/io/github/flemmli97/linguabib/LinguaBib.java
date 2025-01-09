package io.github.flemmli97.linguabib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class LinguaBib {

    public static final String MODID = "lingua_bib";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static boolean disableComponentMod;
    public static boolean ftbRanks;
    public static boolean permissionAPI;

    @SuppressWarnings("unchecked")
    public static <T> T getPlatformInstance(Class<T> abstractClss, String... impls) {
        if (impls == null || impls.length == 0)
            throw new IllegalStateException("Couldn't create an instance of " + abstractClss + ". No implementations provided!");
        Class<?> clss = null;
        int i = 0;
        while (clss == null && i < impls.length) {
            try {
                clss = Class.forName(impls[i]);
            } catch (ClassNotFoundException ignored) {
            }
            i++;
        }
        if (clss == null)
            LinguaBib.LOGGER.fatal("No Implementation of {} found with given paths {}", abstractClss, Arrays.toString(impls));
        else if (abstractClss.isAssignableFrom(clss)) {
            try {
                Constructor<T> constructor = (Constructor<T>) clss.getDeclaredConstructor();
                return constructor.newInstance();
            } catch (NoSuchMethodException e) {
                LinguaBib.LOGGER.fatal("Implementation of {} needs to provide an no arg constructor", clss);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                LinguaBib.LOGGER.fatal("Error instantiating class", e);
            }
        }
        throw new IllegalStateException("Couldn't create an instance of " + abstractClss);
    }
}
