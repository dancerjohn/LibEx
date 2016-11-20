package org.libex.tiny;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

@Immutable
@ThreadSafe
@ParametersAreNonnullByDefault
public abstract class StringTinyType<SELF_CLASS extends StringTinyType<? extends Object>>
        extends ComparableTinyType<String, SELF_CLASS> {

    public StringTinyType(String value) {
        super(value);
    }
}
