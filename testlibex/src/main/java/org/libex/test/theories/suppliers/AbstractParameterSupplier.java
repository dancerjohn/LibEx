package org.libex.test.theories.suppliers;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;

import com.google.common.base.Function;
import com.google.common.base.Joiner;

/**
 * Abstract base class for Test Supplier classes. Handles checking for
 * {@link ReturnAsList}.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public abstract class AbstractParameterSupplier<T> extends ParameterSupplier {

	private static final Joiner commaJoiner = Joiner.on(",");

    public static String checkForNull(
            final String input)
    {
        if (isNullOrEmpty(input)
                || TestOn.NULL.equals(input)) {
            return null;
        } else {
            return input;
        }
    }

	@Nonnull
	public abstract List<T> getTestValues(final ParameterSignature sig);

	@Nonnull
	protected abstract String toKey(final T record);

	private Function<T, String> toKey = new Function<T, String>() {

		@Override
		@Nullable
		public String apply(@Nullable final T record) {
			return toKey(record);
		}
	};

	private Function<T, PotentialAssignment> toPotentialAssignment = new Function<T, PotentialAssignment>() {

		@Override
		@Nullable
		public PotentialAssignment apply(@Nullable final T record) {
			return PotentialAssignment.forValue(toKey(record), record);
		}
	};

	@Override
	@Nonnull
	public List<PotentialAssignment> getValueSources(final ParameterSignature sig) {
		List<T> records = getTestValues(sig);

		if (sig.getAnnotation(ReturnAsList.class) == null) {
			return newArrayList(transform(records, toPotentialAssignment));
		} else {
			String listKey = commaJoiner.join(transform(records, toKey));
			return newArrayList(PotentialAssignment.forValue(listKey, records));
		}
	}
}
