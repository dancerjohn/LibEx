package org.libex.examples.aop;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:aopProfilingMetricsExample.xml")
public class MetricsProfilingTest {

	@Autowired
	public Profiled profiled;

	@Before
	public void setUp() throws Exception {
		profiled.setWaitValue(50);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMethod() throws InterruptedException {
		assertThat(profiled, notNullValue());

		for (int i = 0; i < 50; i++) {
			profiled.method();
		}
	}

}
