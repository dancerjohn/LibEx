package org.libex.test.rules.duration;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.libex.concurrent.TimeSpan;
import org.libex.hamcrest.IsOptional;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;

@RunWith(Enclosed.class)
public class TestDurationTest {

	@RunWith(Parameterized.class)
	public static class GetMaxDuration {

		@Parameters
		public static Collection<Object[]> getData() {
			return newArrayList(
					new Object[] { TestDuration.SHORT, TimeSpan.seconds(3) },
					new Object[] { TestDuration.MEDIUM, TimeSpan.seconds(10) },
					new Object[] { TestDuration.LONG, null });
		}

		private final TestDuration duration;
		private final TimeSpan timeSpan;

		public GetMaxDuration(TestDuration duration, TimeSpan timeSpan) {
			super();
			this.duration = duration;
			this.timeSpan = timeSpan;
		}

		@Test
		public void testGetMaxDuration() {
			// test
			Optional<TimeSpan> result = duration.getMaxDuration();

			// verify
			assertThat(result, IsOptional.ofNullableValue(timeSpan));
		}
	}

	@RunWith(Parameterized.class)
	public static class ShouldTestBeRun {

		@Parameters
		public static Collection<Object[]> getData() {
			return newArrayList(
					new Object[] { TestDuration.SHORT, true, "" },
					new Object[] { TestDuration.MEDIUM, true, "" },
					new Object[] { TestDuration.LONG, false, "" },
					new Object[] { TestDuration.SHORT, true, "runShortTests=true,runMediumTests=false,runLongTests=false" },
					new Object[] { TestDuration.SHORT, false, "runShortTests=false,runMediumTests=true,runLongTests=true" },
					new Object[] { TestDuration.MEDIUM, true, "runShortTests=false,runMediumTests=true,runLongTests=false" },
					new Object[] { TestDuration.MEDIUM, false, "runShortTests=true,runMediumTests=false,runLongTests=true" },
					new Object[] { TestDuration.LONG, true, "runShortTests=false,runMediumTests=false,runLongTests=true" },
					new Object[] { TestDuration.LONG, false, "runShortTests=true,runMediumTests=true,runLongTests=false" },
					// Do This Last
					new Object[] { TestDuration.LONG, false, "runShortTests=true,runMediumTests=true,runLongTests=false" });
		}

		private static final MapSplitter splitter = Splitter.on(",")
				.trimResults()
				.omitEmptyStrings()
				.withKeyValueSeparator("=");

		private final TestDuration duration;
		private final boolean expectedResult;
		private final Map<String, String> properties;

		public ShouldTestBeRun(TestDuration duration, boolean expectedResult, String properties) {
			super();
			this.duration = duration;
			this.expectedResult = expectedResult;
			this.properties = splitter.split(properties);
		}

		@Test
		public void testShouldTestBeRun() {
			// setup
			for (Map.Entry<String, String> entry : properties.entrySet()) {
				System.setProperty(entry.getKey(), entry.getValue());
			}

			// test
			boolean result = duration.shouldTestBeRun();

			// verify
			assertThat(result, is(expectedResult));
		}
	}

}
