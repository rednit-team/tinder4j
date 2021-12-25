package com.rednit.tinder4j.internal.async;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface RestAction<T> {

    default void queue() {
        queue(null);
    }

    default void queue(@Nullable Consumer<? super T> success) {
        queue(success, null);
    }

    void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

    T complete();
}
