package org.libex.math;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;
import org.libex.test.TestBase;

@RunWith(Theories.class)
public class BasicRunningAverageTest extends TestBase {

	public RunningAverage createRunningAverage(int numberValuesToMaintain) {
		return new BasicRunningAverage(numberValuesToMaintain);
	}

	private RunningAverage runningAverage;

	@Theory
	public void testBasicRunningAverage_invalidArg(@TestedOn(ints = { -1, 0 }) int value) {
		expectedException.expect(IllegalArgumentException.class);

		createRunningAverage(value);
	}

	@Test
	public void testSetNumberToSkip() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGroupingRange() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberEventsRecorded() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNumberEventsInAverage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRunningAverage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSnapshot() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEventCountSet() {
		fail("Not yet implemented");
	}

}
