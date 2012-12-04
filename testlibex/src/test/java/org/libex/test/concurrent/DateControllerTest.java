package org.libex.test.concurrent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.hamcrest.number.OrderingComparisons;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.libex.concurrent.DateSupplier;
import org.libex.test.TestBase;

/**
 * @author John Butler
 * 
 */
public class DateControllerTest extends TestBase {
	
	@Rule
	public DateController dateContoller = new DateController();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultBehavior() {
		assertDefaultBehavior();
	}
	
	@Test
	public void testSetCurrentTime(){
		// setup
		Date date = new Date(123443);
		
		// test
		dateContoller.setCurrentTime(date);
		
		// verify
		assertDateEquals(date);
	}

	@Test
	public void testDefaultBehavior_again() {
		assertDefaultBehavior();
	}
	
	@Test
	public void testSetCurrentTimeToNow() throws InterruptedException{
		// setup
		Date before = new Date();
		
		// test
		dateContoller.setCurrentTimeToNow();
		
		// verify
		Date after = new Date();

		Thread.sleep(300);
		Date date = DateSupplier.getCurrentDate();
		long dateTime = DateSupplier.getCurrentDateInMilliseconds();
		assertDateInRange(date, dateTime, before, after);
	}

	@Test
	public void testDefaultBehavior_again2() {
		assertDefaultBehavior();
	}
	
	@Test
	public void testReset(){
		// setup
		Date date = new Date(12123);
		dateContoller.setCurrentTime(date);
		assertDateEquals(date);
		
		// test
		dateContoller.reset();
		
		// verify
		assertDefaultBehavior();
	}

	private void assertDefaultBehavior(){
		// setup
		Date before = new Date();
		
		// test
		Date date = DateSupplier.getCurrentDate();
		long dateTime = DateSupplier.getCurrentDateInMilliseconds();
		
		// verify
		Date after = new Date();
		assertDateInRange(date, dateTime, before, after);
	}
	
	private void assertDateInRange(Date retrievedDate, long retreiveMs, Date before, Date after){
		assertThat(retrievedDate, OrderingComparisons.lessThanOrEqualTo(after));
		assertThat(retreiveMs, OrderingComparisons.lessThanOrEqualTo(after.getTime()));
		assertThat(retrievedDate, OrderingComparisons.greaterThanOrEqualTo(before));
		assertThat(retreiveMs, OrderingComparisons.greaterThanOrEqualTo(before.getTime()));
	}
	
	private void assertDateEquals(Date date){
		assertThat(DateSupplier.getCurrentDate(), equalTo(date));
		assertThat(DateSupplier.getCurrentDateInMilliseconds(), equalTo(date.getTime()));
	}
}
