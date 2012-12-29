package org.libex.concurrent.cancelling;

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
import org.libex.test.TestBase;
import org.libex.test.mockito.answer.DelayedAnswer;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@ParametersAreNonnullByDefault
@ThreadSafe
public class SelfCancelingCallableTest extends TestBase {

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private final ListeningScheduledExecutorService executorServiceList = MoreExecutors
			.listeningDecorator(executorService);

	@Mock
	public Callable<String> shortDelay, longDelay, justLongDelay;

	@Mock
	public CancelingExecutorObserver<String> observer;

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
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(longDelay)
				.setTimeout(cancelTime)
				.setExecutorService(executorServiceList)
				.build();

		expectedException.expect(InterruptedException.class);

		cancelling.call();
	}

	@Test
	public void testCancel_executorWithObserver() throws Exception {
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(longDelay)
				.setTimeout(cancelTime)
				.setExecutorService(executorServiceList)
				.setObserver(observer)
				.build();

		expectedException.expect(InterruptedException.class);

		try {
			cancelling.call();
		} catch (Exception e) {
			Thread.sleep(50);
			verify(observer).onTaskCanceled(Mockito.same(longDelay));

			throw e;
		}
	}

	@Test
	public void testNoCancel_executor() throws Exception {
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(shortDelay)
				.setTimeout(cancelTime)
				.setExecutorService(executorServiceList)
				.build();
		cancelling.call();
		verifyNoMoreInteractions(observer);
	}

	@Test
	public void testCancel_timer() throws Exception {
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(justLongDelay)
				.setTimeout(cancelTime)
				.setTimer(timer)
				.build();

		expectedException.expect(InterruptedException.class);

		cancelling.call();
	}

	@Test
	public void testCancel_timerWithObserver() throws Exception {
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(justLongDelay)
				.setTimeout(cancelTime)
				.setTimer(timer)
				.setObserver(observer)
				.build();

		expectedException.expect(InterruptedException.class);

		try {
			cancelling.call();
		} catch (Exception e) {
			Thread.sleep(50);
			verify(observer).onTaskCanceled(Mockito.same(justLongDelay));

			throw e;
		}
	}

	@Test
	public void testNoCancel_timer() throws Exception {
		SelfCancelingCallable<String> cancelling = SelfCancelingCallable.<String> newBuilder()
				.setCallable(shortDelay)
				.setTimeout(cancelTime)
				.setTimer(timer)
				.build();
		cancelling.call();
		verifyNoMoreInteractions(observer);
	}

}
