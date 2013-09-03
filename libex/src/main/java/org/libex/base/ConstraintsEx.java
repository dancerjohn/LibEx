package org.libex.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Constraint;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class ConstraintsEx {

	public static <T> Constraint<T> predicateConstraint(final Predicate<T> predicate) {
		return new PredicateConstraint<T>(predicate);
	}

	public static <T> Constraint<T> bypassConstraint() {
		return new PredicateConstraint<T>(Predicates.alwaysTrue());
	}

	@ParametersAreNonnullByDefault
	@ThreadSafe
	@Immutable
	public static final class PredicateConstraint<T> implements Constraint<T> {
		private final Predicate<? super T> predicate;
		private final String errorMessage;

		public PredicateConstraint(Predicate<? super T> predicate) {
			this(predicate, "%s does not satisfy predicate %s");
		}

		public PredicateConstraint(Predicate<? super T> predicate, String errorMessage) {
			super();
			this.predicate = checkNotNull(predicate, "predicate");
			this.errorMessage = checkNotNull(errorMessage, "errorMessage");
		}

		@Override
		public T checkElement(T element) {
			checkArgument(predicate.apply(element), errorMessage, element, predicate);
			return element;
		}
	}

	private ConstraintsEx() {
	}
}
