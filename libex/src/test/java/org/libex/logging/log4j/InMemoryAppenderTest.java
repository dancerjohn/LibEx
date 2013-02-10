package org.libex.logging.log4j;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;
import org.libex.test.TestBase;

@SuppressWarnings("deprecation")
public class InMemoryAppenderTest extends TestBase {

	private LoggingEvent event1 = new LoggingEvent("blah",
			Category.getInstance(InMemoryAppenderTest.class), Level.DEBUG, "event1 message", null);

	private InMemoryAppender appender;

	@Before
	public void setUp() throws Exception {
		appender = new InMemoryAppender();
	}

	@Test
	public void testClose() {
		appender.close();
	}

	@Test
	public void testRequiresLayout() {
		assertThat(appender.requiresLayout(), is(false));
	}

	@Test
	public void testAppendLoggingEvent_first() {
		// test
		appender.append(event1);

		// verify
		assertThat(appender.getLoggingEvents(), IsIterableContainingInOrder.contains(event1));
	}

	@Test
	public void testGetLoggingEvents() {
		List<LoggingEvent> events = newArrayList();
		for (int i = 0; i < 5000; i++) {
			events.add(new LoggingEvent("blah",
					Category.getInstance(InMemoryAppenderTest.class), Level.DEBUG, "event message " + i, null));
		}

		for (LoggingEvent event : events) {
			appender.append(event);
		}

		assertThat(appender.getLoggingEvents(), IsIterableContainingInOrder.contains(events.toArray()));
	}

	@Test
	public void testClear() {
		// setup
		List<LoggingEvent> events = newArrayList();
		for (int i = 0; i < 5000; i++) {
			events.add(new LoggingEvent("blah",
					Category.getInstance(InMemoryAppenderTest.class), Level.DEBUG, "event message " + i, null));
		}

		for (LoggingEvent event : events) {
			appender.append(event);
		}

		// test
		appender.clear();

		// verify
		assertThat(appender.getLoggingEvents(), IsEmptyCollection.empty());
		events = newArrayList();
		for (int i = 0; i < 5000; i++) {
			events.add(new LoggingEvent("blah",
					Category.getInstance(InMemoryAppenderTest.class), Level.DEBUG, "event message " + i + 5000, null));
		}

		for (LoggingEvent event : events) {
			appender.append(event);
		}
		assertThat(appender.getLoggingEvents(), IsIterableContainingInOrder.contains(events.toArray()));
	}

}
