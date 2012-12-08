package org.libex.test;

import java.lang.reflect.Constructor;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.base.ObjectsEx;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TestUtilities {

	public static void invokeDefaultConstructor(Class<?> type) throws Exception {
		Constructor<?> constructor = ObjectsEx.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}
}
