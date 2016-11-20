package org.libex.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
//import org.libex.test.TestBase;
import org.libex.test.TestBaseLocal;

import com.google.common.base.Optional;

public class SettableOnceTest extends TestBaseLocal {
	
	final String DEFAULT_MESSAGE = "Field cannot be set more than once";
	SettableOnce<String> so;
	String input1 = "Test 1", input2 = "Test 2";
	
	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp(){
		so = create();
	}
	
	protected SettableOnce<String> create(){
		return new SettableOnce<String>();
	}
	
	@Test
	public void testNulls(){
		nullPointerTester.testAllPublicConstructors(so.getClass());
		nullPointerTester.testAllPublicInstanceMethods(so);
	}
	
	@Test
	public void defaultConstructorTest(){
		assertNull(so.get());
		assertEquals(so.getOptional(), Optional.absent());
		assertEquals(so.getMessage(), DEFAULT_MESSAGE);
	}
	
	@Test
	public void ParameterizedConstructorTest(){
		so = new SettableOnce<String>(input1);
		
		assertNull(so.get());
		assertEquals(so.getOptional(), Optional.absent());
		assertEquals(so.getMessage(), "Field " + input1 + " cannot be set more than once");
	}
	
	@Test
	public void set_ValueTest(){
		so.set(input1);
		assertEquals(so.getOptional(), Optional.of(input1));
		assertEquals(so.get(), input1);
		assertEquals(so.getMessage(), DEFAULT_MESSAGE);
	}
	
	@Test 
	public void set_setTwiceTest(){
		// setup
		so.set(input1);
		
		// use ExpectedException @Rule
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage(DEFAULT_MESSAGE);
		
		// test
		so.set(input2);
	}
	
	@Test 
	public void setIfAbsent_twiceTest(){
		// setup
		so.setIfAbsent(input1);

		// test
		so.setIfAbsent(input2);

		// verify
		assertEquals(so.getOptional(), Optional.of(input1));
		assertEquals(so.get(), input1);
	}

	@Test 
	public void setIfAbsent_thenSetTest(){
		// setup
		so.setIfAbsent(input1);
		
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage(DEFAULT_MESSAGE);

		// test
		so.set(input2);
	}
	
	@Test 
	public void setIfAbsent_SecondNullTest(){
		so.setIfAbsent(input1);
		
		thrown.expect(NullPointerException.class);
		
		so.setIfAbsent(null);
	}
	
	@Test
	public void setMessageTest(){
		so.setMessage(input1);
		assertEquals(so.getMessage(), input1);
	}
}
