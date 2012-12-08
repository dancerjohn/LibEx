package org.libex.test;

import java.lang.reflect.Constructor;

import org.junit.Ignore;

@Ignore
public class TestUtilities {

	public static void invokeDefaultConstructor(Class<?> type) throws Exception {
		Constructor<?> constructor = type.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}
}
