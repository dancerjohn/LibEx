package org.libex.test.logging.log4j;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.libex.test.TestBase;
import org.libex.test.hamcrest.IsThrowable;
import org.libex.test.logging.log4j.Log4jCapturer.LogAssertion;

public class Log4jCapturerTest extends TestBase {

	private static final Logger LOG = Logger.getLogger(Log4jCapturerTest.class);

	@Rule
	public Log4jCapturer logCapturer = Log4jCapturer.builder().build();

	@Before
	public void setUp() throws Exception {
		LOG.info("info message 1");
		LOG.info("info message 2");
		LOG.warn("warn message 1");
		LOG.warn("warn message 2");
		LOG.error("error message 1");
		LOG.error("error message 2");
		LOG.error("error message with ex", new RuntimeException("test"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMessageLogged() {
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.DEBUG).isNotLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion().withLevel(
				Level.INFO));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.INFO).withRenderedMessage("info message"));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.INFO).withRenderedMessage("info message")
				.isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.INFO).withRenderedMessage("warn message")
				.isNotLogged());

		logCapturer.assertThat(LogAssertion.newLogAssertion().withLevel(
				Level.WARN));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.WARN).withRenderedMessage("warn message"));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.WARN).withRenderedMessage("warn message")
				.isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.WARN).withRenderedMessage("warn message 1 +")
				.isNotLogged());

		logCapturer.assertThat(LogAssertion.newLogAssertion().withLevel(
				Level.ERROR));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR).withRenderedMessage("error message"));
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR).withRenderedMessage("error message")
				.isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR).withRenderedMessage("error message")
				.withException(instanceOf(RuntimeException.class)).isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR)
				.withRenderedMessage(Matchers.containsString("error message"))
				.withException(instanceOf(RuntimeException.class)).isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR).withRenderedMessage("error message")
				.withException(instanceOf(Exception.class)).isLogged());
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.ERROR)
				.withException(instanceOf(IllegalArgumentException.class))
				.isNotLogged());

		logCapturer
				.assertThat(LogAssertion
						.newLogAssertion()
						.withLevel(Level.ERROR)
						.withRenderedMessage("error message")
						.withException(
								IsThrowable.isThrowable(RuntimeException.class,
										"test")).isLogged());
		logCapturer.assertThat(LogAssertion
				.newLogAssertion()
				.withLevel(Level.ERROR)
				.withRenderedMessage("error message")
				.withException(
						IsThrowable
								.isThrowable(RuntimeException.class, "test2"))
				.isNotLogged());
	}

	@Test
	public void testMessageLoggedNot() {
		expectedException.expect(AssertionError.class);

		// test
		logCapturer.assertThat(LogAssertion.newLogAssertion()
				.withLevel(Level.INFO).withRenderedMessage("warn message")
				.isLogged());
	}

}
