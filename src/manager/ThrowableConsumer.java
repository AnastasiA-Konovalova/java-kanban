package manager;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ThrowableConsumer<T> {
    static <T> Consumer<T> of(ThrowableConsumer<T> wrapped, Function<Throwable, RuntimeException> throwableFunction) {
        return t -> {
            try {
                wrapped.apply(t);
            } catch (Exception e) {
                throw throwableFunction.apply(e);
            }
        };
    }

    void apply(T t) throws Exception;
}

