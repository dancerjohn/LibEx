package org.libex.test.validation.message.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import org.hamcrest.Matcher;
import org.libex.test.validation.message.SingleMessageMatcher;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

@ParametersAreNonnullByDefault
@ThreadSafe
@Getter
public class DefaultMessageMatcher<InputType, MatchedType>
        extends AbstractMessageMatcher<InputType, MatchedType>
        implements SingleMessageMatcher<InputType, MatchedType> {

    public static <InputType, MatchedType> DefaultMessageMatcher.DefaultMessageMatcherBuilder<InputType, MatchedType> of(
            final Class<InputType> inputClass,
            final Class<MatchedType> matchedClass){
        return DefaultMessageMatcher.builder();
    }

    public static <InputType> DefaultMessageMatcher.DefaultMessageMatcherBuilder<InputType, InputType> of(
            final Class<InputType> inputClass)
    {
        return DefaultMessageMatcher.builder();
    }

    @Setter
    @Accessors(chain = true)
    private Predicate<? super InputType> predicate;

    @Setter
    @Accessors(chain = true)
    private Matcher<? super MatchedType> matcher;

    @SuppressWarnings("unchecked")
    @Builder
    protected DefaultMessageMatcher(
            final Predicate<? super InputType> predicate,
            final Function<? super InputType, ? extends MatchedType> function,
            final Matcher<? super MatchedType> matcher) {
        super(function);
        this.predicate = (Predicate<? super InputType>) Objects.firstNonNull(predicate, Predicates.alwaysTrue());
        this.matcher = checkNotNull(matcher);
    }

    @Override
    public Predicate<? super InputType> getPredicate()
    {
        return predicate;
    }
}
