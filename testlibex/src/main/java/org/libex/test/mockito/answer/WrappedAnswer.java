package org.libex.test.mockito.answer;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;

import org.libex.concurrent.TimeSpan;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.base.Optional;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@NotThreadSafe
@ParametersAreNonnullByDefault
public class WrappedAnswer<T> implements Answer<T> {

	@Nonnull
	public static <T> WrappedAnswer<T> create() {
		return new WrappedAnswer<T>();
	}

	private Optional<Iterator<T>> values = Optional.absent();
	private Optional<Answer<T>> answer = Optional.absent();
	private Optional<Supplier<T>> supplier = Optional.absent();

	private final List<TimeSpan> invocationTimeSpans = newArrayList();

	protected WrappedAnswer() {

	}

	@SuppressWarnings("unchecked")
	@Nonnull
	public <U extends T> WrappedAnswer<T> setValue(@Nullable U value) {
		return setValues(value);
	}

	@Nonnull
	public <U extends T> WrappedAnswer<T> setValues(@Nullable U value, @Nullable U... values) {
		checkNoOptionalsSet();

		@SuppressWarnings("unchecked")
		List<T> valueList = Lists.<T> newArrayList(value);
		valueList.addAll(Lists.<T> newArrayList(values));
		Iterable<T> iterable = Iterables.<T> cycle(valueList);
		this.values = Optional.of(iterable.iterator());
		return this;
	}

	@Nonnull
	public WrappedAnswer<T> setAnswer(Answer<T> answer) {
		checkNoOptionalsSet();

		this.answer = Optional.of(answer);
		return this;
	}

	@Nonnull
	public WrappedAnswer<T> setSupplier(Supplier<T> supplier) {
		checkNoOptionalsSet();

		this.supplier = Optional.of(supplier);
		return this;
	}

	private void checkNoOptionalsSet() {
		checkState(!values.isPresent() &&
				!answer.isPresent() &&
				!supplier.isPresent(), "Only one behavior may be set: Value, Answer or Supplier");
	}

	@Nonnull
	public ImmutableList<TimeSpan> getInvocationTimeSpans() {
		return ImmutableList.copyOf(invocationTimeSpans);
	}

	@Override
	@Nullable
	public T answer(InvocationOnMock invocation) throws Throwable {
		Stopwatch watch = new Stopwatch();
		watch.start();

		T result = getResult(invocation);

		watch.stop();
		invocationTimeSpans.add(new TimeSpan(watch.elapsedMillis(), TimeUnit.MILLISECONDS));
		return result;
	}

	@Nullable
	protected T getResult(InvocationOnMock invocation) throws Throwable {
		T result = null;

		if (values.isPresent()) {
			result = values.get().next();
		} else if (supplier.isPresent()) {
			result = supplier.get().get();
		} else if (answer.isPresent()) {
			result = answer.get().answer(invocation);
		}

		return result;
	}
}
