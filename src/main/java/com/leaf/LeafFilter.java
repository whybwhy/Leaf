package com.leaf;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class LeafFilter {

    public static void filter(Runnable ... array) {
        for (Runnable runnable : array) {
           runnable.run();
        }
    }

    public static  <X extends Throwable> void test(Supplier<? extends X> exceptionSupplier, boolean expected) throws X {
        if(!expected) {
            throw exceptionSupplier.get();
        }
    }
}
