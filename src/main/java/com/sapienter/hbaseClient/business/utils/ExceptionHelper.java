package com.sapienter.hbaseClient.business.utils;

import java.util.List;

/**
 * Created by marcolin on 25/03/15.
 */
public interface ExceptionHelper {
    default void runConvertingToRuntime(RunnableThatThrows runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    default <T> List<T> runConvertingToRuntime(CallableThatThrows<List<T>> runnable) {
        try {
            return runnable.apply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
