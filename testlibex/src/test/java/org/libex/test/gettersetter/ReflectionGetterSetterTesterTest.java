package org.libex.test.gettersetter;

import org.junit.Test;
import org.libex.test.TestBase;

public class ReflectionGetterSetterTesterTest extends TestBase {

	@Test
	public void test() {
		ReflectionGetterSetterTester.testAllGetterSetters(new TestClassA(), errorCollector, "trueBooleanPrimitive");
	}

	@Test
	public void test2() {
		ReflectionGetterSetterTester.testAllGetterSetters(new TestClassA(), errorCollector);
	}

	@Test
	public void testBuilder() {
		ReflectionGetterSetterTester.testAllGetterSetters(new TestClassA(), errorCollector);
	}
}
