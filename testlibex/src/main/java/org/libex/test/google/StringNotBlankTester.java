package org.libex.test.google;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.junit.rules.ErrorCollector;
import org.libex.test.google.NullPointerTester.VisibilityLocal;

import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.MutableClassToInstanceMap;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.Parameter;
import com.google.common.reflect.TypeToken;
import com.google.common.testing.ArbitraryInstances;
import com.google.common.testing.NullPointerTester.Visibility;

/**
 * This class tests that all {@code String} arguments to methods in the class
 * under test are validated as not allowing for blank values. The set of values
 * to test against is configurable via the constructor.
 * 
 * This class does not sub-class the local version of {@link NullPointerTester} because
 * it is hoped that that instance will be removed once the google library is updated to
 * allow for modifying the policy.
 * 
 * This class might eventually be updated to test any argument type.
 */
public class StringNotBlankTester {

    private static final List<String> DEFAULT_VALUES_TO_TEST = newArrayList(null, "", " ", "     ", "\t");
    private static final String DEFAULT_STRING_VALUE = "SomeValue";

    private final ClassToInstanceMap<Object> defaults =
            MutableClassToInstanceMap.create();
    private final List<Member> ignoredMembers = Lists.newArrayList();
    private final List<String> valuesToTest;

    private ExceptionTypePolicy policy = ExceptionTypePolicy.NPE_IAE_OR_UOE;
    private Optional<ErrorCollector> errorCollector = Optional.absent();

    public StringNotBlankTester() {
        this(DEFAULT_VALUES_TO_TEST);
    }

    public StringNotBlankTester(
            final Iterable<String> valuesToTest) {
        checkArgument(!isEmpty(valuesToTest), "at least one value to test must be provided");

        this.valuesToTest = newArrayList(valuesToTest);
    }

    /**
     * Set an {@link ErrorCollector} to use. If one is provided all errors will be accumulated
     * via the {@link ErrorCollector}. If not provided (and by default) this class will error out
     * on first failure.
     * 
     * @param errorCollector
     *            the ErrorCollector to use
     */
    public void setErrorCollector(
            @Nullable final ErrorCollector errorCollector)
    {
        this.errorCollector = Optional.fromNullable(errorCollector);
    }

    /**
     * Sets a default value that can be used for any parameter of type {@code type}. Returns this object.
     * 
     * @param type
     *            Class for which passed value should be used as the non-null default
     * @param value
     *            value to use as non-null default
     * @return this object
     * @param <T>
     *            type of value to set
     */
    public <T> StringNotBlankTester setDefault(
            final Class<T> type,
            final T value)
    {
        defaults.putInstance(type, checkNotNull(value));
        return this;
    }

    /**
     * Ignore {@code method} in the tests that follow. Returns this object.
     *
     * @since 13.0
     * @param method
     *            method to test
     * @return this object
     */
    public StringNotBlankTester ignore(final Method method) {
      ignoredMembers.add(checkNotNull(method));
      return this;
    }

    /**
     * Sets the policy used to determine what type of exception are expected to
     * be thrown when null is provided
     *
     * @param policy the {@link ExceptionTypePolicy}
     * @return this object
     */
    public StringNotBlankTester policy(final ExceptionTypePolicy policy) {
      this.policy = policy;
      return this;
    }

    /**
     * Runs {@link #testConstructor} on every constructor in class {@code c} that
     * has at least {@code minimalVisibility}.
     * 
     * @param c
     *            class to test
     * @param minimalVisibility
     *            Visibility level above which and including that should be tested
     */
    public void testConstructors(
            final Class<?> c,
            final Visibility minimalVisibility)
    {
        for (Constructor<?> constructor : c.getDeclaredConstructors()) {
            if (convert(minimalVisibility).isVisible(constructor)
                    && !isIgnored(constructor)
                    && hasStringParameter(constructor)) {
                testConstructor(constructor);
            }
        }
    }

    private VisibilityLocal convert(
            final Visibility visibility)
    {
        return VisibilityLocal.valueOf(visibility.name());
    }

    /**
     * Runs {@link #testConstructor} on every public constructor in class {@code c}.
     * 
     * @param c
     *            class to test
     */
    public void testAllPublicConstructors(final Class<?> c) {
      testConstructors(c, Visibility.PUBLIC);
    }

    /**
     * Runs {@link #testMethod} on every static method of class {@code c} that has
     * at least {@code minimalVisibility}, including those "inherited" from
     * superclasses of the same package.
     * 
     * @param c
     *            class to test
     * @param minimalVisibility
     *            Visibility level above which and including that should be tested
     */
    public void testStaticMethods(final Class<?> c, final Visibility minimalVisibility) {
        for (Method method : convert(minimalVisibility).getStaticMethods(c)) {
            if (!isIgnored(method)
                    && hasStringParameter(method)) {
          testMethod(null, method);
        }
      }
    }

    /**
     * Runs {@link #testMethod} on every public static method of class {@code c},
     * including those "inherited" from superclasses of the same package.
     * 
     * @param c
     *            class to test
     */
    public void testAllPublicStaticMethods(final Class<?> c) {
      testStaticMethods(c, Visibility.PUBLIC);
    }

    /**
     * Runs {@link #testMethod} on every instance method of the class of {@code instance} with at least
     * {@code minimalVisibility}, including those
     * inherited from superclasses of the same package.
     * 
     * @param instance
     *            object to test
     * @param minimalVisibility
     *            Visibility level above which and including that should be tested
     */
    public void testInstanceMethods(final Object instance, final Visibility minimalVisibility) {
      for (Method method : getInstanceMethodsToTest(instance.getClass(), minimalVisibility)) {
        testMethod(instance, method);
      }
    }

    ImmutableList<Method> getInstanceMethodsToTest(
            final Class<?> c,
            final Visibility minimalVisibility)
    {
        ImmutableList.Builder<Method> builder = ImmutableList.builder();
        for (Method method : convert(minimalVisibility).getInstanceMethods(c)) {
            if (!isIgnored(method)
                    && hasStringParameter(method)) {
                builder.add(method);
            }
        }
        return builder.build();
    }

    /**
     * Runs {@link #testMethod} on every public instance method of the class of {@code instance}, including those
     * inherited from superclasses of the same
     * package.
     * 
     * @param instance
     *            object to test
     */
    public void testAllPublicInstanceMethods(final Object instance) {
      testInstanceMethods(instance, Visibility.PUBLIC);
    }

    /**
     * Verifies that {@code method} produces a {@link NullPointerException} or {@link UnsupportedOperationException}
     * whenever <i>any</i> of its
     * non-{@link Nullable} parameters are null.
     *
     * @param instance
     *            the instance to invoke {@code method} on, or null if {@code method} is static
     * @param method
     *            method to test
     */
    public void testMethod(@Nullable final Object instance, final Method method) {
      Class<?>[] types = method.getParameterTypes();
      for (int nullIndex = 0; nullIndex < types.length; nullIndex++) {
        testMethodParameter(instance, method, nullIndex);
      }
    }

    /**
     * Verifies that {@code ctor} produces a {@link NullPointerException} or {@link UnsupportedOperationException}
     * whenever <i>any</i> of its
     * non-{@link Nullable} parameters are null.
     * 
     * @param ctor
     *            constructor to test
     */
    public void testConstructor(final Constructor<?> ctor) {
      Class<?> declaringClass = ctor.getDeclaringClass();
      checkArgument(Modifier.isStatic(declaringClass.getModifiers())
          || declaringClass.getEnclosingClass() == null,
          "Cannot test constructor of non-static inner class: %s", declaringClass.getName());
      Class<?>[] types = ctor.getParameterTypes();
      for (int nullIndex = 0; nullIndex < types.length; nullIndex++) {
        testConstructorParameter(ctor, nullIndex);
      }
    }

    /**
     * Verifies that {@code method} produces a {@link NullPointerException} or {@link UnsupportedOperationException}
     * when the parameter in position {@code paramIndex} is null. If this parameter is marked {@link Nullable}, this
     * method does nothing.
     *
     * @param instance
     *            the instance to invoke {@code method} on, or null if {@code method} is static
     * @param method
     *            method to test
     * @param paramIndex
     *            index of parameter to test
     */
    public void testMethodParameter(
        @Nullable final Object instance, final Method method, final int paramIndex) {
      method.setAccessible(true);
      testParameter(instance, invokable(instance, method), paramIndex, method.getDeclaringClass());
    }

    /**
     * Verifies that {@code ctor} produces a {@link NullPointerException} or {@link UnsupportedOperationException} when
     * the parameter in position {@code paramIndex} is null. If this parameter is marked {@link Nullable}, this
     * method does nothing.
     * 
     * @param ctor
     *            constructor to test
     * @param paramIndex
     *            index of parameter to test
     */
    public void testConstructorParameter(final Constructor<?> ctor, final int paramIndex) {
      ctor.setAccessible(true);
      testParameter(null, Invokable.from(ctor), paramIndex, ctor.getDeclaringClass());
    }

    /**
     * Verifies that {@code invokable} produces a {@link NullPointerException} or {@link UnsupportedOperationException}
     * when the parameter in position {@code paramIndex} is null. If this parameter is marked {@link Nullable}, this
     * method does nothing.
     *
     * @param instance
     *            the instance to invoke {@code invokable} on, or null if {@code invokable} is static
     */
    private void testParameter(
            final Object instance,
            final Invokable<?, ?> invokable,
            final int paramIndex,
            final Class<?> testedClass)
    {
        if (isNullableOrNotString(invokable.getParameters().get(paramIndex))) {
            return; // there's nothing to test
        }

        for (String valueToTest : valuesToTest) {
            try {
                testParameterWithValue(instance, invokable, paramIndex, testedClass, valueToTest);
            } catch (RuntimeException | Error e) {
                if (errorCollector.isPresent()) {
                    errorCollector.get().addError(e);
                } else {
                    throw e;
                }
            }
        }
    }

    private void testParameterWithValue(
            final Object instance,
            final Invokable<?, ?> invokable,
            final int paramIndex,
            final Class<?> testedClass,
            final String valueToTest)
    {
        Object[] params = buildParamList(invokable, paramIndex, valueToTest);
        try {
            @SuppressWarnings("unchecked")
            // We'll get a runtime exception if the type is wrong.
            Invokable<Object, ?> unsafe = (Invokable<Object, ?>) invokable;
            unsafe.invoke(instance, params);

            Assert.fail("No exception thrown for parameter at index " + paramIndex
                    + " from " + invokable + toString(params) + " for " + testedClass);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (policy.isExpectedType(cause)) {
                return;
            }
            AssertionFailedError error = new AssertionFailedError(
                    "wrong exception thrown from " + invokable + toString(params) + ": " + cause);
            error.initCause(cause);
            throw error;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Joiner JOINER = Joiner.on(",")
            .useForNull("null");

    private String toString(
            final Object[] params)
    {
        List<String> paramList = new ArrayList<>(params.length);
        for (Object o : params) {
            if (o != null && o instanceof String) {
                paramList.add(String.format("\"%s\"", o));
            } else {
                paramList.add(Objects.toString(o));
            }
        }
        return String.format("[%s]", JOINER.join(paramList));
    }

    private Object[] buildParamList(
            final Invokable<?, ?> invokable,
            final int indexOfParamToSetToNull,
            final String valueToTest)
    {
      ImmutableList<Parameter> params = invokable.getParameters();
      Object[] args = new Object[params.size()];
        args[indexOfParamToSetToNull] = valueToTest;

      for (int i = 0; i < args.length; i++) {
        Parameter param = params.get(i);
        if (i != indexOfParamToSetToNull) {
          args[i] = getDefaultValue(param.getType());
          Assert.assertTrue(
              "Can't find or create a sample instance for type '"
                  + param.getType()
                  + "'; please provide one using NullPointerTester.setDefault()",
              args[i] != null || isNullable(param));
        }
      }
      return args;
    }

    private <T> T getDefaultValue(
            final TypeToken<T> type)
    {
        // We assume that all defaults are generics-safe, even if they aren't,
        // we take the risk.
        @SuppressWarnings("unchecked")
        T defaultValue = (T) defaults.getInstance(type.getRawType());
        if (defaultValue != null) {
            return defaultValue;
        }
        if (type.getRawType().equals(String.class)) {
            @SuppressWarnings("unchecked")
            T stringValue = (T) DEFAULT_STRING_VALUE;
            return stringValue;
        }
        @SuppressWarnings("unchecked")
        // All arbitrary instances are generics-safe
        T arbitrary = (T) ArbitraryInstances.get(type.getRawType());
        if (arbitrary != null) {
            return arbitrary;
        }
        if (type.getRawType() == Class.class) {
            // If parameter is Class<? extends Foo>, we return Foo.class
            @SuppressWarnings("unchecked")
            T defaultClass = (T) getFirstTypeParameter(type.getType()).getRawType();
            return defaultClass;
        }
        if (type.getRawType() == TypeToken.class) {
            // If parameter is TypeToken<? extends Foo>, we return TypeToken<Foo>.
            @SuppressWarnings("unchecked")
            T defaultType = (T) getFirstTypeParameter(type.getType());
            return defaultType;
        }
        if (type.getRawType() == Converter.class) {
            TypeToken<?> convertFromType = type.resolveType(
                    Converter.class.getTypeParameters()[0]);
            TypeToken<?> convertToType = type.resolveType(
                    Converter.class.getTypeParameters()[1]);
            @SuppressWarnings("unchecked")
            // returns default for both F and T
            T defaultConverter = (T) defaultConverter(convertFromType, convertToType);
            return defaultConverter;
        }
        if (type.getRawType().isInterface()) {
            return newDefaultReturningProxy(type);
        }
        return null;
    }

    private <F, T> Converter<F, T> defaultConverter(
        final TypeToken<F> convertFromType, final TypeToken<T> convertToType) {
      return new Converter<F, T>() {
        @Override protected T doForward(final F a) {
          return doConvert(convertToType);
        }
        @Override protected F doBackward(final T b) {
          return doConvert(convertFromType);
        }

        private /*static*/ <S> S doConvert(final TypeToken<S> type) {
          return checkNotNull(getDefaultValue(type));
        }
      };
    }

    private static TypeToken<?> getFirstTypeParameter(final Type type) {
      if (type instanceof ParameterizedType) {
        return TypeToken.of(
            ((ParameterizedType) type).getActualTypeArguments()[0]);
      } else {
        return TypeToken.of(Object.class);
      }
    }

    private <T> T newDefaultReturningProxy(final TypeToken<T> type) {
      return new DummyProxy() {
        @Override <R> R dummyReturnValue(final TypeToken<R> returnType) {
          return getDefaultValue(returnType);
        }
      }.newProxy(type);
    }

    private static Invokable<?, ?> invokable(@Nullable final Object instance, final Method method) {
      if (instance == null) {
        return Invokable.from(method);
      } else {
        return TypeToken.of(instance.getClass()).method(method);
      }
    }

    static boolean isNullableOrNotString(final Parameter param) {
        Class<?> rawType = param.getType().getRawType();
        return rawType.isPrimitive()
                || !rawType.equals(String.class)
                || isNullable(param);
    }

    private static boolean isNullable(final Parameter param) {
      return param.isAnnotationPresent(Nullable.class);
    }

    private static boolean hasStringParameter(
            final Method method)
    {
        return hasStringParameter(method.getParameterTypes());
    }

    private static boolean hasStringParameter(
            final Constructor<?> method)
    {
        return hasStringParameter(method.getParameterTypes());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static boolean hasStringParameter(
            final Class<?>[] parameterTypes)
    {
        return Iterators.any(Iterators.forArray(parameterTypes), (Predicate) Predicates.equalTo(String.class));
    }

    private boolean isIgnored(final Member member) {
      return member.isSynthetic() || ignoredMembers.contains(member);
    }

    /**
     * Strategy for exception type matching used by {@link StringNotBlankTester}.
     */
    public enum ExceptionTypePolicy {

      /**
       * Exceptions should be {@link NullPointerException} or
       * {@link UnsupportedOperationException}.
       */
      NPE_OR_UOE() {
        @Override
        public boolean isExpectedType(final Throwable cause) {
          return cause instanceof NullPointerException
              || cause instanceof UnsupportedOperationException;
        }
      },

      /**
       * Exceptions should be {@link NullPointerException},
       * {@link IllegalArgumentException}, or
       * {@link UnsupportedOperationException}.
       */
      NPE_IAE_OR_UOE() {
        @Override
        public boolean isExpectedType(final Throwable cause) {
          return cause instanceof NullPointerException
              || cause instanceof IllegalArgumentException
              || cause instanceof UnsupportedOperationException;
        }
        },

        /**
         * Exceptions should be {@link IllegalArgumentException}, or {@link UnsupportedOperationException}.
         */
        IAE_OR_UOE() {
            @Override
            public boolean isExpectedType(
                    final Throwable cause)
            {
                return cause instanceof IllegalArgumentException
                        || cause instanceof UnsupportedOperationException;
            }
      };

      public abstract boolean isExpectedType(final Throwable cause);
    }
}
