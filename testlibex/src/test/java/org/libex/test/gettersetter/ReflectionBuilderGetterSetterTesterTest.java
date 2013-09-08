package org.libex.test.gettersetter;

import org.junit.Test;
import org.libex.test.TestBase;

public class ReflectionBuilderGetterSetterTesterTest extends TestBase {

	@Test
	public void test2() {
		ReflectionBuilderGetterSetterTester.testAllGetterSetters(BuilderExampleA.builder(), errorCollector);
	}
}
