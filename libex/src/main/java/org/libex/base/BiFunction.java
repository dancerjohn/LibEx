package org.libex.base;


public interface BiFunction<T, V> {

    V toRight(
            final T input);

    T toLeft(
            final V input);
}
