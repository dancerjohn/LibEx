package org.libex.hamcrest;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsMapContaining;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class IsMapContainingInAnyOrder {

    @Factory
    public static <K, V> Matcher<Map<? extends K, ? extends V>> containsInAnyOrder(
            final Map<K, V> expectedValues)
    {
        List<Matcher<? super Map<? extends K, ? extends V>>> matcherList = newArrayList();
        matcherList.add(IsMapWithSize.hasSize(expectedValues.size()));

        for (Entry<K, V> entry : expectedValues.entrySet()) {
            matcherList.add(IsMapContaining.hasEntry(entry.getKey(), entry.getValue()));
        }

        return Matchers.allOf(matcherList);
    }

    private IsMapContainingInAnyOrder() {
    }
}
