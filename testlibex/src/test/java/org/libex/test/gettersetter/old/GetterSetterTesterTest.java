package org.libex.test.gettersetter.old;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;
import org.libex.test.gettersetter.TestClass1;
import org.libex.test.gettersetter.TestClass2;
import org.libex.test.gettersetter.old.IntGetterSetterTester;
import org.libex.test.gettersetter.old.IntegerGetterSetterTester;
import org.libex.test.theories.suppliers.TestOn;

@RunWith(Theories.class)
public class GetterSetterTesterTest extends TestBase {

	@Test
	public void testNullInstance() {
		try {
			IntGetterSetterTester.testIntGetterSetter(null, "intValue");
			Assert.fail();
		} catch (AssertionError error) {
			assertThat(error.getMessage(), containsString("class under test"));
		}
	}

	@Theory
	public void testNullName(@TestOn(strings = { "", TestOn.NULL }) String value) {
		try {
			IntGetterSetterTester.testIntGetterSetter(new TestClass1(), value);
			Assert.fail();
		} catch (AssertionError error) {
			assertThat(error.getMessage(), containsString("field name to test must be supplied"));
		}
	}

	@Test
	public void testInstanceNeeded() {
		try {
			IntGetterSetterTester.testIntGetterSetter(TestClass2.class, "intValue");
			Assert.fail();
		} catch (AssertionError error) {
			assertThat(error.getMessage(), containsString("instance must be provided"));
		}
	}

	@Test
	public void testInstanceCreated() {
		IntGetterSetterTester.testIntGetterSetter(TestClass1.class, "intValue");
	}

	@Test
	public void testInstance() {
		IntGetterSetterTester.testIntGetterSetter(new TestClass1(), "intValue");
		IntegerGetterSetterTester.testIntegerGetterSetter(new TestClass1(), "integerValue");
	}

	@Test
	public void testInstance2() {
		IntGetterSetterTester.testIntGetterSetter(new TestClass2(5), "intValue");
	}
}
