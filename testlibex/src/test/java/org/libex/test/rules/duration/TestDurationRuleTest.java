package org.libex.test.rules.duration;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;

@RunWith(Enclosed.class)
public class TestDurationRuleTest extends TestBase {

	public static class DurationOnMethod extends TestBase {
		@Rule
		public TestDurationRule rule = new TestDurationRule();

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(3200);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_pass() throws InterruptedException {
			Thread.sleep(2900);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(10200);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_pass() throws InterruptedException {
			Thread.sleep(9900);
		}

		@Test
		@Duration(TestDuration.LONG)
		public void testLong_pass() throws Exception {
			throw new Exception();
		}
	}

	@Duration(TestDuration.SHORT)
	public static class DurationOnClassShort extends TestBase {
		@Rule
		public TestDurationRule rule = new TestDurationRule();

		@Test
		public void testShort_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(3200);
		}

		@Test
		public void testShort_pass() throws InterruptedException {
			Thread.sleep(2900);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShortOverride_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(3200);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShortOverride_pass() throws InterruptedException {
			Thread.sleep(2900);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(10200);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_pass() throws InterruptedException {
			Thread.sleep(9900);
		}

		@Test
		@Duration(TestDuration.LONG)
		public void testLong_pass() throws Exception {
			throw new Exception();
		}
	}

	@Duration(TestDuration.MEDIUM)
	public static class DurationOnClassMedium extends TestBase {
		@Rule
		public TestDurationRule rule = new TestDurationRule();

		@Test
		public void testMedium_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(10200);
		}

		@Test
		public void testMedium_pass() throws InterruptedException {
			Thread.sleep(9900);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(3200);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_pass() throws InterruptedException {
			Thread.sleep(2900);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMediumOverride_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(10200);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMediumOverride_pass() throws InterruptedException {
			Thread.sleep(9900);
		}

		@Test
		@Duration(TestDuration.LONG)
		public void testLong_pass() throws Exception {
			throw new Exception();
		}
	}

	@Duration(TestDuration.LONG)
	public static class DurationOnClassLong extends TestBase {
		@Rule
		public TestDurationRule rule = new TestDurationRule();

		@Test
		public void test_pass() throws Exception {
			throw new Exception();
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(3200);
		}

		@Test
		@Duration(TestDuration.SHORT)
		public void testShort_pass() throws InterruptedException {
			Thread.sleep(2900);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_fail() throws InterruptedException {
			expectedException.expect(Exception.class);

			Thread.sleep(10200);
		}

		@Test
		@Duration(TestDuration.MEDIUM)
		public void testMedium_pass() throws InterruptedException {
			Thread.sleep(9900);
		}

		@Test
		@Duration(TestDuration.LONG)
		public void testLong_pass() throws Exception {
			throw new Exception();
		}
	}

}
