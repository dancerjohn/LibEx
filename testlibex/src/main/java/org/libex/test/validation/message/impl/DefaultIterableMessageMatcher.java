package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;

import org.hamcrest.Matcher;
import org.libex.test.validation.message.IterableMessageMatcher;

import com.google.common.base.Function;
import com.google.common.base.Functions;

@ParametersAreNonnullByDefault
@ThreadSafe
@Getter
public class DefaultIterableMessageMatcher<InputType, MatchedType>
        extends AbstractMessageMatcher<InputType, MatchedType>
        implements IterableMessageMatcher<InputType, MatchedType> {

    public static <MatchedType> DefaultIterableMessageMatcher<MatchedType, MatchedType> is(
            final Matcher<? super List<? super MatchedType>> matcher)
    {
        return new DefaultIterableMessageMatcher<MatchedType, MatchedType>(Functions.<MatchedType> identity(), matcher);
    }

    public static <InputType, MatchedType> DefaultIterableMessageMatcher<InputType, MatchedType> is(
            final Function<? super InputType, ? extends MatchedType> function,
            final Matcher<? super List<MatchedType>> matcher)
    {
        return new DefaultIterableMessageMatcher<InputType, MatchedType>(function, matcher);
    }

    private final Matcher<? super List<MatchedType>> matcher;

    private DefaultIterableMessageMatcher(
            final Function<? super InputType, ? extends MatchedType> function,
            final Matcher<? super List<MatchedType>> matcher) {
        super(function);
        this.matcher = checkNotNull(matcher);
    }
}
