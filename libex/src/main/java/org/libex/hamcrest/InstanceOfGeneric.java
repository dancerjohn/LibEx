package org.libex.hamcrest;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.google.common.reflect.TypeToken;

@ParametersAreNonnullByDefault
@ThreadSafe
@Slf4j
public class InstanceOfGeneric<T> extends BaseMatcher<T> {

    public static <T> InstanceOfGeneric<T> isA(
            final TypeToken<T> typeToken)
    {
        return new InstanceOfGeneric<T>(typeToken);
    }

    private final TypeToken<T> typeToken;

    public InstanceOfGeneric(
            final TypeToken<T> typeToken) {
        this.typeToken = checkNotNull(typeToken);
    }

    @Override
    public boolean matches(
            final Object item)
    {
        boolean result = item != null && typeToken.getRawType().isAssignableFrom(item.getClass());
        if (!result) {
            log.info("Expected instance of {}, received {}", typeToken.getRawType(), item.getClass());
        }
        return result;
    }

    @Override
    public void describeTo(
            final Description description)
    {
        description.appendText(String.format("isA(%s)", typeToken));
    }

}
