package org.libex.test.jms.validation.capture;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.camel.Exchange;
import org.libex.collect.CollectionsEx;
import org.libex.collect.MultimapsEx;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;

@ParametersAreNonnullByDefault
@NotThreadSafe
public final class JmsExchangeCapturers {

    private JmsExchangeCapturers() {
    }

    public static <T> Function<JmsExchangeCapturer<T>, ImmutableList<Exchange>>
            toDestination(
                    final T destination)
    {
        checkNotNull(destination);
        return new Function<JmsExchangeCapturer<T>, ImmutableList<Exchange>>() {

            @Override
            public ImmutableList<Exchange> apply(
                    final JmsExchangeCapturer<T> input)
            {
                return input.getExchangeList(destination);
            }
        };
    }

    public static <T> Function<JmsExchangeCapturer<T>, ImmutableMultimap<T, Exchange>>
            toExchangeMap()
    {
        return new Function<JmsExchangeCapturer<T>, ImmutableMultimap<T, Exchange>>() {

            @Override
            public ImmutableMultimap<T, Exchange> apply(
                    final JmsExchangeCapturer<T> input)
            {
                return input.getExchangeMap();
            }
        };
    }

    public static <T> Function<JmsExchangeCapturer<T>, Collection<Exchange>>
            toExchanges()
    {
        return Functions.compose(MultimapsEx.<Exchange> toValues(), JmsExchangeCapturers.<T> toExchangeMap());
    }

    public static <T> Function<JmsExchangeCapturer<T>, Map<T, Integer>>
            toCountMap()
    {
        return new Function<JmsExchangeCapturer<T>, Map<T, Integer>>() {

            @Override
            public Map<T, Integer> apply(
                    final JmsExchangeCapturer<T> input)
            {
                Map<T, Collection<Exchange>> map = input.getExchangeMap().asMap();
                Map<T, Integer> countMap = Maps.transformValues(map, CollectionsEx.toSize());
                return countMap;
            }
        };
    }
}
