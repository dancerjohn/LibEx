package org.libex.test.gettersetter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import javax.annotation.Nullable;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;
import org.libex.test.gettersetter.DefaultGetterSetterTester.ExpectedValueSupplier;
import org.libex.test.gettersetter.DefaultGetterSetterTester.ProcessingArguments;
import org.libex.test.gettersetter.object.StringGetterSetterTester;
import org.libex.test.gettersetter.old.IntPrimitiveGetterSetterTester;
import org.libex.test.theories.suppliers.TestOn;

@RunWith(Theories.class)
public class GetterSetterTesterTest extends TestBase {

	@Test
	public void testNullInstance() {
		expectException(NullPointerException.class, "class under test");

		IntPrimitiveGetterSetterTester.testIntGetterSetter(null, "intValue");
	}

	@Theory
	public void testNullName(@TestOn(strings = { "", TestOn.NULL }) String value) {
		expectException(IllegalArgumentException.class, "field name must be provided");

		IntPrimitiveGetterSetterTester.testIntGetterSetter(new TestClass1(), value);
	}

	@Test
	public void testInstanceNeeded() {
		try {
			IntPrimitiveGetterSetterTester.testIntGetterSetter(TestClass2.class, "intValue");
			Assert.fail();
		} catch (AssertionError error) {
			assertThat(error.getMessage(), containsString("instance must be provided"));
		}
	}

	@Test
	public void testInstanceCreated() {
		IntPrimitiveGetterSetterTester.testIntGetterSetter(TestClass1.class, "intValue");
	}

	// @Test
	// public void testInstance() {
	// IntPrimitiveGetterSetterTester.testIntGetterSetter(new TestClass1(),
	// "intValue");
	// IntegerGetterSetterTester.testIntegerGetterSetter(new TestClass1(),
	// "integerValue");
	// IntegerGetterSetterTester.testIntegerGetterSetter(new TestClass1(),
	// "nullableIntegerValue");
	// StringGetterSetterTester.testStringGetterSetter(new TestClass1(),
	// "stringValue");
	// ObjectGetterSetterTester.testObjectGetterSetter(new TestClass1(),
	// "document", Document.class);
	// }
	//
	// @Test
	// public void testGenericInstance() {
	// GetterSetterTester.testGetterSetter(new TestClass1(), "intValue",
	// int.class);
	// GetterSetterTester.testGetterSetter(new TestClass1(), "integerValue",
	// Integer.class);
	// GetterSetterTester.testGetterSetter(new TestClass1(),
	// "nullableIntegerValue", Integer.class);
	// GetterSetterTester.testGetterSetter(new TestClass1(), "stringValue",
	// String.class);
	// GetterSetterTester.testGetterSetter(new TestClass1(), "document",
	// Document.class);
	// }
	//
	// @Test
	// public void testTrimmedInstance() {
	// TransformingExpectedValueSupplier.transformExpectedValues(
	// StringGetterSetterTester.createTester(TestClass1.class, new TestClass1(),
	// "trimmedStringValue"),
	// StringsEx.toTrimmed())
	// .testGetterSetter();
	// }

	@Test
	public void testBuilderInstance() {
		BuilderGetterInvoker.builderTester(
				new StringGetterSetterTester().createTester(BuilderExampleA.BuilderExampleABuilder.class, BuilderExampleA.builder(), "stringValue"))
				.testGetterSetter();
	}

	@Test
	public void testInstance2() {
		DefaultGetterSetterTester<Integer> tester = IntPrimitiveGetterSetterTester.createTester(
				TestClass2.class, new TestClass2(5), "intValue")
				.setExpectedValueSupplier(new ExpectedValueSupplier<Integer>() {

					@Override
					@Nullable
					public Integer transform(ProcessingArguments<Integer> args) {
						return args.valueBeingSet() + 2;
					}
				});
		tester.testGetterSetter();
	}
}
