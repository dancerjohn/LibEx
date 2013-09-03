package org.libex.test.gettersetter;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester.ExpectedValueSupplier;
import org.libex.test.gettersetter.DefaultGetterSetterTester.ProcessingArguments;

import com.google.common.base.Function;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TransformingExpectedValueSupplier<T> implements ExpectedValueSupplier<T> {

	public static <T> DefaultGetterSetterTester<T> transformExpectedValues(
			@Nonnull DefaultGetterSetterTester<T> tester,
			@Nonnull Function<T, T> function) {
		return tester.setExpectedValueSupplier(new TransformingExpectedValueSupplier<T>(function));
	}

	private final Function<T, T> function;

	private TransformingExpectedValueSupplier(Function<T, T> function) {
		super();
		this.function = checkNotNull(function, "function may not be null");
	}

	@Override
	@Nullable
	public T transform(@Nonnull ProcessingArguments<T> args) {
		return function.apply(args.valueBeingSet());
	}

}
