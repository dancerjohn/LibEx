package org.libex.base.concurrent.cancelling;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.libex.concurrent.TimeSpan;
import org.libex.concurrent.cancelling.SelfCancellingCallable;
import org.libex.test.TestBase;

@ParametersAreNonnullByDefault
@ThreadSafe
public class SelfCancellingCallableTest extends TestBase {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSelfCancellingCallable() {
		nullPointerTester.setDefault(TimeSpan.class, new TimeSpan(1, TimeUnit.MILLISECONDS));

		nullPointerTester.testAllPublicConstructors(SelfCancellingCallable.class);
	}

	@Test
	public void testCall() {
		fail("Not yet implemented");
	}

}
