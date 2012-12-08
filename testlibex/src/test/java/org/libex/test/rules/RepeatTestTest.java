package org.libex.test.rules;

import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.libex.test.annotation.Repeat;

@RunWith(Enclosed.class)
public class RepeatTestTest {

	public static class NoReExecute {
		@Rule
		public RepeatTest repeatTest = RepeatTest.findRepeats();

		private static Map<String, AtomicInteger> expected = newHashMap();
		private static Map<String, AtomicInteger> map = newHashMap();

		@BeforeClass
		public static void beforeClass() {
			map.put("apply", new AtomicInteger());
			map.put("once", new AtomicInteger());
			map.put("twice", new AtomicInteger());
			map.put("five", new AtomicInteger());

			expected.put("apply", new AtomicInteger(1));
			expected.put("once", new AtomicInteger(1));
			expected.put("twice", new AtomicInteger(2));
			expected.put("five", new AtomicInteger(5));
		}

		@AfterClass
		public static void afterClass() {
			for (String key : expected.keySet()) {
				assertThat(map.get(key).get(), equalTo(expected.get(key).get()));
			}
		}

		@Test
		public void testApply() {
			System.out.println("test apply");
			incremement("apply");
		}

		@Test
		@Repeat(count = 1)
		public void testWithRepeatOnce() {
			System.out.println("test once");
			incremement("once");
		}

		@Test
		@Repeat(count = 2)
		public void testWithRepeatTwice() {
			System.out.println("test twice");
			incremement("twice");
		}

		@Test
		@Repeat(count = 5)
		public void testWithRepeatFive() {
			System.out.println("test five");
			incremement("five");
		}

		private void incremement(String value) {
			map.get(value).incrementAndGet();
		}
	}

}
