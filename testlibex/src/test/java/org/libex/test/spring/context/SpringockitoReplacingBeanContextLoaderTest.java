package org.libex.test.spring.context;

import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoReplacingBeanContextLoader.class,
		locations = "classpath:spring/context/contextExample.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SpringockitoReplacingBeanContextLoaderTest {

	@ReplaceWithMock
	public Int1 class1;

	@ReplaceContextBean(name = "class12", factoryMethod = "createOther2")
	public Int1 other2;

	public Int1 createOther2() {
		return new Class2();
	}

	@Autowired
	public Class3 class3;

	@Autowired
	public Class3 class32;

	@Autowired
	public Class3 class33;

	@Test
	public void test() {
		assertThat(class3, notNullValue());
		assertThat(class3.value, nullValue());
		assertThat(class32, notNullValue());
		assertThat(class32.value, equalTo("class2"));
		assertThat(class33, notNullValue());
		assertThat(class33.value, equalTo("class2"));
	}

	@Test
	public void test2() {
		assertThat(class3, notNullValue());
		assertThat(class3.value, nullValue());
	}

	@GenerateContextBeanReplacements
	public Map<String, Object> init() {
		Map<String, Object> map = newHashMap();
		map.put("class13", new Class2());
		return map;
	}

}
