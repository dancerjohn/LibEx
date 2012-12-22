package org.libex.base.concurrent.cancelling;

import static org.mockito.Mockito.*;

import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.libex.concurrent.TimeSpan;
import org.libex.concurrent.cancelling.SelfCancelingCallable;
import org.libex.test.TestBase;
import org.libex.test.mockito.answer.DelayedAnswer;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@ParametersAreNonnullByDefault
@ThreadSafe
public class SelfCancelingCallableTest extends TestBase {

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

	@Mock
	public Callable<String> shortDelay, longDelay, justLongDelay;

	private final TimeSpan cancelTime = new TimeSpan(500, TimeUnit.MILLISECONDS);
	private final TimeSpan shortTime = new TimeSpan(490, TimeUnit.MILLISECONDS);
	private final TimeSpan justLongTime = new TimeSpan(600, TimeUnit.MILLISECONDS);
	private final TimeSpan longTime = new TimeSpan(2000, TimeUnit.MILLISECONDS);

	private Timer timer = new Timer(true);

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(shortDelay.call()).thenAnswer(DelayedAnswer.create(shortTime));
		when(longDelay.call()).thenAnswer(DelayedAnswer.create(longTime));
		when(justLongDelay.call()).thenAnswer(DelayedAnswer.create(justLongTime));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelfCancellingCallable() {
		nullPointerTester.setDefault(TimeSpan.class, new TimeSpan(1, TimeUnit.MILLISECONDS));

		nullPointerTester.testAllPublicConstructors(SelfCancelingCallable.class);
	}

	@Test
	public void testCancel_executor() throws Exception {
		SelfCancelingCallable<String> cancelling = new SelfCancelingCallable<String>(longDelay, cancelTime, executorService);

		expectedException.expect(InterruptedException.class);

		cancelling.call();
	}

	@Test
	public void testNoCancel_executor() throws Exception {
		SelfCancelingCallable<String> cancelling = new SelfCancelingCallable<String>(shortDelay, cancelTime, executorService);
		cancelling.call();
	}

	@Test
	public void testCancel_timer() throws Exception {
		SelfCancelingCallable<String> cancelling = new SelfCancelingCallable<String>(justLongDelay, cancelTime, timer);

		expectedException.expect(InterruptedException.class);

		cancelling.call();
	}

	@Test
	public void testNoCancel_timer() throws Exception {
		SelfCancelingCallable<String> cancelling = new SelfCancelingCallable<String>(shortDelay, cancelTime, timer);
		cancelling.call();
	}

}
