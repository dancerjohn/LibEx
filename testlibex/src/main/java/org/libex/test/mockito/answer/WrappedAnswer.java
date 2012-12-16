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

/**
 * An {@link Answer} that allows for wrapping (value(s), Supplier or Answer)
 * 
 * @author John Butler
 * 
 * @param <T>
 *            the type of the answer
 */
@NotThreadSafe
@ParametersAreNonnullByDefault
public class WrappedAnswer<T> implements Answer<T> {

	/**
	 * @return a new empty {@link WrappedAnswer}
	 */
	@Nonnull
	public static <T> WrappedAnswer<T> create() {
		return new WrappedAnswer<T>();
	}

	private Optional<Iterator<T>> values = Optional.absent();
	private Optional<Answer<T>> answer = Optional.absent();
	private Optional<Supplier<T>> supplier = Optional.absent();
	private Optional<Throwable> throwable = Optional.absent();

	private final List<TimeSpan> invocationTimeSpans = newArrayList();

	protected WrappedAnswer() {
	}

	/**
	 * Configures the answer to always return the passed value. This method is
	 * mutually exclusive from {@link #setValues(Object, Object...)},
	 * {@link #setAnswer(Answer)}, {@link #setThrowable(Throwable)} and
	 * {@link #setSupplier(Supplier)}.
	 * 
	 * @param value
	 *            the value that the answer should return
	 * @return this instance
	 * @throws IllegalStateException
	 *             if any of the mutually exclusives setters has been called
	 */
	@SuppressWarnings("unchecked")
	@Nonnull
	public <U extends T> WrappedAnswer<T> setValue(@Nullable U value) {
		return setValues(value);
	}

	/**
	 * Configures the answer to return the passed values for invocations. The
	 * value returned from each invocation will cycle through the values passed.
	 * This method is mutually exclusive from {@link #setValue(Object)},
	 * {@link #setAnswer(Answer)}, {@link #setThrowable(Throwable)} and
	 * {@link #setSupplier(Supplier)}.
	 * 
	 * @param value
	 *            first required value
	 * @param values
	 *            any additional values
	 * @return this instance
	 * @throws IllegalStateException
	 *             if any of the mutually exclusives setters has been called
	 */
	@Nonnull
	public <U extends T> WrappedAnswer<T> setValues(@Nullable U value,
			@Nullable U... values) {
		checkNoOptionalsSet();

		@SuppressWarnings("unchecked")
		List<T> valueList = Lists.<T> newArrayList(value);
		valueList.addAll(Lists.<T> newArrayList(values));
		Iterable<T> iterable = Iterables.<T> cycle(valueList);
		this.values = Optional.of(iterable.iterator());
		return this;
	}

	/**
	 * Configures the answer to return the result of the passed answer. This
	 * method is mutually exclusive from {@link #setValue(Object)},
	 * {@link #setValues(Object, Object...)}, {@link #setThrowable(Throwable)}
	 * and {@link #setSupplier(Supplier)}.
	 * 
	 * @param answer
	 *            the wrapped answer
	 * @return this instance
	 * @throws IllegalStateException
	 *             if any of the mutually exclusives setters has been called
	 */
	@Nonnull
	public WrappedAnswer<T> setAnswer(Answer<T> answer) {
		checkNoOptionalsSet();

		this.answer = Optional.of(answer);
		return this;
	}

	/**
	 * Configures the answer to return the result of the passed suppler. This
	 * method is mutually exclusive from {@link #setValue(Object)},
	 * {@link #setValues(Object, Object...)}, {@link #setThrowable(Throwable)}
	 * and {@link #setAnswer(Answer)}.
	 * 
	 * @param supplier
	 *            the supplier
	 * @return this instance
	 * 
	 * @throws IllegalStateException
	 *             if any of the mutually exclusives setters has been called
	 */
	@Nonnull
	public WrappedAnswer<T> setSupplier(Supplier<T> supplier) {
		checkNoOptionalsSet();

		this.supplier = Optional.of(supplier);
		return this;
	}

	/**
	 * Configures the answer to throw the passed Throable. This method is
	 * mutually exclusive from {@link #setValue(Object)},
	 * {@link #setValues(Object, Object...)}, {@link #setAnswer(Answer)} and
	 * {@link #setSupplier(Supplier)}.
	 * 
	 * @param supplier
	 *            the supplier
	 * @return this instance
	 * 
	 * @throws IllegalStateException
	 *             if any of the mutually exclusives setters has been called
	 */
	@Nonnull
	public WrappedAnswer<T> setThrowable(Throwable throwable) {
		checkNoOptionalsSet();

		this.throwable = Optional.of(throwable);
		return this;
	}

	private void checkNoOptionalsSet() {
		checkState(
				!values.isPresent()
						&& !answer.isPresent()
						&& !supplier.isPresent()
						&& !throwable.isPresent(),
				"Only one behavior may be set: Value, Answer, Throwable or Supplier");
	}

	/**
	 * @return a list of the duration of each mock invocation
	 */
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
		invocationTimeSpans.add(new TimeSpan(watch.elapsedMillis(),
				TimeUnit.MILLISECONDS));
		return result;
	}

	/**
	 * Method to override to alter the behavior of the answer. In general all
	 * overrides of this method should invoke this method using
	 * {@code super(invocation)}
	 * 
	 * @param invocation
	 *            the invocation
	 * @return the value that should be returned for the invocation
	 * @throws Throwable
	 */
	@Nullable
	protected T getResult(InvocationOnMock invocation) throws Throwable {
		T result = null;

		if (values.isPresent()) {
			result = values.get().next();
		} else if (supplier.isPresent()) {
			result = supplier.get().get();
		} else if (answer.isPresent()) {
			result = answer.get().answer(invocation);
		} else if (throwable.isPresent()) {
			throw throwable.get();
		}

		return result;
	}
}
