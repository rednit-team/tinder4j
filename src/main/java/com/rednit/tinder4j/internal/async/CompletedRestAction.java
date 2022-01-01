package com.rednit.tinder4j.internal.async;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CompletedRestAction<T> implements RestAction<T> {

    private final T object;

    public CompletedRestAction(T object) {
        this.object = object;
    }

    @Override
    public void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure) {
        if (success != null) {
            success.accept(object);
        }
    }

    @Override
    public T complete() {
        return object;
    }
}
