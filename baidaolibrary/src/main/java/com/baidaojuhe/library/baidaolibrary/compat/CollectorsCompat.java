/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.baidaojuhe.library.baidaolibrary.compat;

import com.annimon.stream.Collector;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by box on 2018/10/19.
 */
public class CollectorsCompat {

    /**
     * Returns a {@code Collector} that fills new {@code Set} with input elements.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     */
    public static <T> Collector<T, ?, Set<T>> toLinkedHashSet() {
        return new CollectorsImpl<>(
                (Supplier<Set<T>>) LinkedHashSet::new,
                Set::add
        );
    }

    private static final class CollectorsImpl<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final Function<A, R> finisher;

        CollectorsImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator) {
            this(supplier, accumulator, null);
        }

        CollectorsImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, Function<A, R> finisher) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.finisher = finisher;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

    }
}
