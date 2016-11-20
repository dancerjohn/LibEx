package org.libex.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Optional;

public class FinalizableSettableOnceTest extends SettableOnceTest {

	private FinalizableSettableOnce<String> fso;
	
	protected SettableOnce<String> create(){
		return fso = new FinalizableSettableOnce<String>();
	}
	
	@Test
	public void SubclassParameterizedConstructorTest(){
		fso = new FinalizableSettableOnce<String>(input1);
		
		assertNull(fso.get());
		assertEquals(fso.getOptional(), Optional.absent());
		assertEquals(fso.getMessage(), "Field " + input1 + " cannot be set more than once");
	}
	
	@Test
	public void finalizeThenSet(){
		fso.finalize();
		
		thrown.expect(IllegalStateException.class);
		fso.set(input1);
	}
	
	@Test
	public void finalizeThenSetIfAbsent(){
		fso.finalize();
		
		fso.setIfAbsent(input1);
		
		assertNull(fso.get());
		assertEquals(fso.getOptional(), Optional.absent());
		assertTrue(fso.getMessage().contains(" has been finalized"));
	}
}
