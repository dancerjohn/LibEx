package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.libex.test.validation.message.BaseMessageMatcher;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
@Getter
public abstract class AbstractMessageMatcher<InputType, TransformedType>
        implements BaseMessageMatcher<InputType, TransformedType> {

    @Nullable
    @Setter
    @Accessors(chain = true)
    private String message;

    @Setter
    @Accessors(chain = true)
    private boolean failFast = false;

    @Setter
    @Accessors(chain = true)
    private Function<? super InputType, ? extends TransformedType> function;

    protected AbstractMessageMatcher(
            final Function<? super InputType, ? extends TransformedType> function) {
        this.function = checkNotNull(function);
    }

    @Override
    public boolean failFast()
    {
        return failFast;
    }
}
