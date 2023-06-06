package moe.wolfgirl.kubejs_stages;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Utils {
    public static <T> T runSided(@Nullable Supplier<Callable<T>> onClient, @Nullable Supplier<Callable<T>> onServer) {
        try {
            if (Platform.getEnvironment() == Env.CLIENT && onClient != null) {
                return onClient.get().call();
            } else if (Platform.getEnvironment() == Env.SERVER && onServer != null) {
                return onServer.get().call();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
