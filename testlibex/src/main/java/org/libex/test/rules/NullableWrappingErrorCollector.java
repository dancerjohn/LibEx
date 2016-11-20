package org.libex.test.rules;

import java.util.concurrent.Callable;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.rules.ErrorCollector;

import com.google.common.base.Throwables;

@ParametersAreNonnullByDefault
@ThreadSafe
public class NullableWrappingErrorCollector {

    @CheckForNull
    private ErrorCollector delegate;

    public NullableWrappingErrorCollector(
            @Nullable final ErrorCollector delegate) {
        this.delegate = delegate;
    }

    public <T> void checkThat(
            final T value,
            final Matcher<T> matcher)
    {
        if (delegate != null) {
            delegate.checkThat(value, matcher);
        } else {
            MatcherAssert.assertThat(value, matcher);
        }
    }

    public <T> void checkThat(
            final String reason,
            final T value,
            final Matcher<T> matcher)
    {
        if (delegate != null) {
            delegate.checkThat(reason, value, matcher);
        } else {
            MatcherAssert.assertThat(reason, value, matcher);
        }
    }

    public <T> void checkThat(
            final String reason,
            final T value,
            final Matcher<T> matcher,
            final boolean skipErrorCollector) // causes fail fast
    {
        if (skipErrorCollector || delegate == null) {
            MatcherAssert.assertThat(reason, value, matcher);
        } else {
            delegate.checkThat(reason, value, matcher);
        }
    }

    public Object checkSucceeds(
            final Callable<Object> callable)
    {
        if (delegate != null) {
            return delegate.checkSucceeds(callable);
        } else {
            try {
                return callable.call();
            } catch (Throwable e) {
                if (delegate != null) {
                    delegate.addError(e);
                    return null;
                } else {
                    throw Throwables.propagate(e);
                }
            }
        }
    }
}
