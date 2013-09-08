package org.libex.test.gettersetter;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.DefaultGetterSetterTester.GetterInvoker;
import org.libex.test.gettersetter.DefaultGetterSetterTester.ProcessingArguments;

@ParametersAreNonnullByDefault
@ThreadSafe
public class BuilderGetterInvoker<T> implements GetterInvoker<T> {

	private static final String[] buildMethodNames = new String[] { "build", "create" };

	public static <T> DefaultGetterSetterTester<T> builderTester(
			@Nonnull DefaultGetterSetterTester<T> tester) {
		return tester.setGetterInvoker(new BuilderGetterInvoker<T>());
	}

	public static <T> DefaultGetterSetterTester<T> builderTester(
			@Nonnull DefaultGetterSetterTester<T> tester,
			@Nullable String buildMethodName,
			@Nullable Class<?> buildType) {
		return tester.setGetterInvoker(new BuilderGetterInvoker<T>(buildMethodName, buildType));
	}

	private String buildMethodName;
	private Class<?> buildType;
	private Method builderMethod = null;

	public BuilderGetterInvoker() {
	}

	public BuilderGetterInvoker(@Nullable String buildMethodName) {
		this.buildMethodName = buildMethodName;
	}

	public BuilderGetterInvoker(@Nullable Class<?> buildType) {
		this.buildType = buildType;
	}

	public BuilderGetterInvoker(@Nullable String buildMethodName, @Nullable Class<?> buildType) {
		this.buildMethodName = buildMethodName;
		this.buildType = buildType;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public T invokeGetter(@Nonnull ProcessingArguments<T> args) throws Exception {
		Object builtInstance = builderMethod.invoke(args.instanceUnderTest());
		if (builtInstance == null) {
			throw new AssertionError("Builder method returned null " + builderMethod);
		}
		return (T) args.getter().invoke(builtInstance);
	}

	@Override
	public Method findGetter(Class<?> classUnderTest, String fieldName, Class<?> fieldType) throws AssertionError {
		builderMethod = findBuilderMethod(classUnderTest);

		if (builderMethod == null) {
			throw new AssertionError("Unable to find builder method that returns " + fieldType);
		}

		return DefaultGetterSetterTester.findGetter(buildType, fieldName, fieldType);
	}

	@Nullable
	private Method findBuilderMethod(Class<?> classUnderTest) {
		if (builderMethod == null) {
			if (buildMethodName != null && buildType != null) {
				builderMethod = findBuilderMethodWithNameThatReturnsBuildType(classUnderTest);
			} else if (buildMethodName != null) {
				builderMethod = findBuilderMethodWithName(classUnderTest);
			} else if (buildType != null) {
				builderMethod = findBuilderMethodThatReturnsBuildType(classUnderTest);
			} else {
				builderMethod = findBuilderMethodUsingDefaultNames(classUnderTest);
			}
		}
		return builderMethod;
	}

	@Nullable
	private Method findBuilderMethodWithNameThatReturnsBuildType(Class<?> classUnderTest) {
		if (buildMethodName != null && buildType != null) {
			try {
				Method method = classUnderTest.getMethod(buildMethodName);
				Class<?> returnType = method.getReturnType();
				if (returnType == buildType) {
					return method;
				}
			} catch (Exception e) {
				// No op
			}
		}
		return null;
	}

	@Nullable
	private Method findBuilderMethodWithName(Class<?> classUnderTest) {
		if (buildMethodName != null) {
			try {
				Method method = classUnderTest.getMethod(buildMethodName);
				Class<?> returnType = method.getReturnType();
				if (returnType != null && returnType != Void.class) {
					buildType = returnType;
					return method;
				}
			} catch (Exception e) {
				// No op
			}
		}
		return null;
	}

	@Nullable
	private Method findBuilderMethodThatReturnsBuildType(Class<?> classUnderTest) {
		if (buildType != null) {
			Method[] methods = classUnderTest.getMethods();
			for (Method method : methods) {
				if (method.getParameterTypes().length == 0 &&
						method.getReturnType() == buildType) {
					return method;
				}
			}
		}
		return null;
	}

	@Nullable
	private Method findBuilderMethodUsingDefaultNames(Class<?> classUnderTest) {
		for (String methodName : buildMethodNames) {
			try {
				Method method = classUnderTest.getMethod(methodName);
				Class<?> returnType = method.getReturnType();
				if (returnType != null && returnType != Void.class) {
					buildType = returnType;
					return method;
				}
			} catch (Exception e) {
				// No op
			}
		}
		return null;
	}
}
