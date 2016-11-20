package org.libex.test.exampleinput.expected;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Getter;

import org.libex.test.exampleinput.ModifiableExampleInput;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@ParametersAreNonnullByDefault
@ThreadSafe
public class DefaultModifiableExampleInputWithExpectedValues<T>
        implements ModifiableExampleInputWithExpectedValues<T> {

    @lombok.Builder
    @Getter
    public static class ExpectedValueConverterInput<T> {
        private final Map<String, ?> variables;
        private final Entry<String, ?> currentEntry;
        private final T expectedValue;
    }

    public static final <T> Function<ExpectedValueConverterInput<?>, T> toCurrentEntryValue(
            final Class<T> type)
    {
        return new Function<DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput<?>, T>() {

            @Override
            public T apply(
                    final ExpectedValueConverterInput<?> input)
            {
                Object result = input.getCurrentEntry().getValue();
                return type.cast(result);
            }
        };
    }

    public static final <T> Function<ExpectedValueConverterInput<T>, T> toExpectedValue()
    {
        return new Function<DefaultModifiableExampleInputWithExpectedValues.ExpectedValueConverterInput<T>, T>() {

            @Override
            public T apply(
                    final ExpectedValueConverterInput<T> input)
            {
                return input.getExpectedValue();
            }
        };
    }

    public static interface ExpectedValueUpdater<T> {
        void update(
                final ExpectedValueConverterInput<? extends T> input);
    }

    private final ModifiableExampleInput input;
    private final Supplier<T> defaultExpectedValuesSupplier;
    private Multimap<String, ExpectedValueUpdater<? super T>> expectedValueUpdates;

    public DefaultModifiableExampleInputWithExpectedValues(
            final ModifiableExampleInput input,
            final Supplier<T> defaultExpectedValuesSupplier,
            final Multimap<String, ExpectedValueUpdater<? super T>> expectedValueUpdates) {
        this.input = checkNotNull(input);
        this.defaultExpectedValuesSupplier = checkNotNull(defaultExpectedValuesSupplier);
        this.expectedValueUpdates = expectedValueUpdates;
    }

    @Override
    public ModifiableExampleInput getExampleInput()
    {
        return input;
    }

    @Override
    public DefaultModifiableExampleInputWithExpectedValues<T> copyUsing(
            final ModifiableExampleInput input)
    {
        return new DefaultModifiableExampleInputWithExpectedValues<T>(
                input,
                this.defaultExpectedValuesSupplier,
                ArrayListMultimap.create(this.expectedValueUpdates));
    }

    @Override
    public ModifiableExampleInputWithExpectedValues<T> copyOverriding(
            final Map<String, ?> overrides)
    {
        return new DefaultModifiableExampleInputWithExpectedValues<T>(
                input.copy().overrideAll(overrides).build(),
                this.defaultExpectedValuesSupplier,
                ArrayListMultimap.create(this.expectedValueUpdates));
    }

    @Override
    public T getExpectedValues()
    {
        return updateExpectedValuesUsing(input.getVariables());
    }

    @Override
    public T getExpectedValuesUsing(
            final String key,
            @Nullable final Object value)
    {
        return updateExpectedValuesUsing(input.getVariablesUsing(key, value));
    }

    @Override
    public T getExpectedValuesUsing(
            @Nullable final Map<String, ?> overrides)
    {
        return updateExpectedValuesUsing(input.getVariablesUsing(overrides));
    }

    protected T updateExpectedValuesUsing(
            final Map<String, ?> variables)
    {
        T expectedValue = defaultExpectedValuesSupplier.get();
        ExpectedValueConverterInput.ExpectedValueConverterInputBuilder<T> inputBuilder = ExpectedValueConverterInput
                .<T> builder()
                .variables(variables)
                .expectedValue(expectedValue);

        for (Entry<String, ?> currentEntry : variables.entrySet()) {
            ExpectedValueConverterInput<T> input = inputBuilder
                    .currentEntry(currentEntry)
                    .build();

            Collection<ExpectedValueUpdater<? super T>> updateFunctions = expectedValueUpdates
                    .get(currentEntry
                    .getKey());
            for (ExpectedValueUpdater<? super T> function : updateFunctions) {
                function.update(input);
            }
        }
        
        return expectedValue;
    }

    // /////////////////////////////////////////////////////////////
    // Implementing ExampleInput
    // /////////////////////////////////////////////////////////////

    @Override
    public String getInput()
    {
        return getExampleInput().getInput();
    }
}
