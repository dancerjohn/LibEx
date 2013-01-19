package org.libex.aop;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.libex.concurrent.Caller;
import org.libex.test.TestBase;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProceedingJoinPointToCallableTest extends TestBase {

	@Mock
	public Caller caller;

	@Mock
	public ProceedingJoinPoint joinPoint;

	@Captor
	public ArgumentCaptor<Callable<Object>> captor;

	private final Object callableResult = new Object();

	private ProceedingJoinPointToCallable joinpointCallable;

	@Before
	public void setUp() throws Throwable {
		MockitoAnnotations.initMocks(this);
		when(joinPoint.proceed()).thenReturn(callableResult);
		when(caller.call(captor.capture())).thenReturn(callableResult);

		joinpointCallable = new ProceedingJoinPointToCallable(caller);
	}

	@Test
	public void testNulls() {
		nullPointerTester.testAllPublicConstructors(ProceedingJoinPointToCallable.class);
		nullPointerTester.testAllPublicInstanceMethods(joinpointCallable);
	}

	@Test
	public void testDoBasicProfiling() throws Throwable {
		// test
		Object result = joinpointCallable.doBasicProfiling(joinPoint);

		// verify
		assertThat(result, sameInstance(callableResult));
		verifyNoMoreInteractions(joinPoint);

		Callable<Object> callable = captor.getValue();
		assertThat(callable, notNullValue());
	}

	@Test
	public void testCallable_noException() throws Throwable {
		// setup
		joinpointCallable.doBasicProfiling(joinPoint);
		Callable<Object> callable = captor.getValue();

		// test
		Object result = callable.call();

		// verify
		assertThat(result, sameInstance(callableResult));
		verify(joinPoint).proceed();
	}

	@Test
	public void testCallable_exception() throws Throwable {
		// setup
		Exception e = new Exception();
		reset(joinPoint);
		when(joinPoint.proceed()).thenThrow(e);
		joinpointCallable.doBasicProfiling(joinPoint);
		Callable<Object> callable = captor.getValue();

		// expect
		expectedException.expect(sameInstance(e));

		// test
		callable.call();
	}

	@Test
	public void testCallable_throwable() throws Throwable {
		// setup
		Throwable e = new Throwable();
		reset(joinPoint);
		when(joinPoint.proceed()).thenThrow(e);
		joinpointCallable.doBasicProfiling(joinPoint);
		Callable<Object> callable = captor.getValue();

		// expect
		expectedException.expect(Exception.class);

		// test
		callable.call();
	}

}
