package com.rednit.tinder4j.api.requests;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A class representing a bridge between the Tinder API and the user. Allows the user to decide how this library should
 * handle the request. RestActions can be finished in two ways:
 * <ul>
 *     <li>
 *         {@link #queue()}, {@link #queue(Consumer)} or {@link #queue(Consumer, Consumer)}
 *         These methods are asynchronous. The response can be accessed in a callback function.
 *     </li>
 *     <li>
 *         {@link #complete()} This will block the thread and return the request result, or throw an exception.
 *     </li>
 * </ul>
 * <p>
 * It is recommended to use {@link #queue()} whenever possible.
 * </p>
 * <p>
 * The concept and great parts of the implementation are copied from
 * <a href="https://github.com/DV8FromTheWorld/JDA">JDA</a> but in a simplified way that meets the projects
 * requirements.
 * </p>
 *
 * @param <T> the generic response type for this RestAction
 * @author Kaktushose
 * @version 1.0.0
 * @since 1.0.0
 */
public interface RestAction<T> {

    /**
     * Submits a Request for execution.
     *
     * <p>To access the response you can use {@link #queue(java.util.function.Consumer)}
     * and to handle failures use {@link #queue(java.util.function.Consumer, java.util.function.Consumer)}.
     *
     * <p><b>This method is asynchronous</b>
     *
     * @see #queue(Consumer)
     * @see #queue(Consumer, Consumer)
     */
    default void queue() {
        queue(null);
    }

    /**
     * Submits a Request for execution.
     * <br>Using the default failure callback function.
     *
     * <p>To handle failures use {@link #queue(java.util.function.Consumer, java.util.function.Consumer)}.
     *
     * <p><b>This method is asynchronous</b>
     *
     * @param success the success callback
     * @see #queue(Consumer, Consumer)
     */
    default void queue(@Nullable Consumer<? super T> success) {
        queue(success, null);
    }

    /**
     * Submits a Request for execution.
     *
     * <p><b>This method is asynchronous</b>
     *
     * @param success the success callback
     * @param failure the failure callback
     */
    void queue(@Nullable Consumer<? super T> success, @Nullable Consumer<? super Throwable> failure);

    /**
     * Blocks the current Thread and awaits the completion of a request.
     *
     * @return the response value
     * @throws IllegalStateException If used within a {@link #queue(Consumer, Consumer) queue(...)} callback
     */
    T complete();
}
