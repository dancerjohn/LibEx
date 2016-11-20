package org.libex.base;

import javax.annotation.Nullable;

public interface FoldFunction<A, B> {

    @Nullable
    B apply(@Nullable B b, @Nullable A a);
}
