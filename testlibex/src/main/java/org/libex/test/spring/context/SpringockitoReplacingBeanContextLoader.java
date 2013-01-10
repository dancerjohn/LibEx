package org.libex.test.spring.context;

import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.context.support.GenericApplicationContext;

/**
 * ContextLoader to be used in tests to allow for the replacement of beans into
 * a Spring context. This is used in conjunction with the
 * {@link org.springframework.test.context.ContextConfiguration} as the
 * <b>loader</b>. This loader extends {@link SpringockitoContextLoader} and
 * therefore supports all the Springockito annotations.
 * <p>
 * The following annotations are used for determining beans to be injected into
 * the context:
 * <p>
 * {@link ReplaceContextBean}
 * <p>
 * {@link GenerateContextBeanReplacements}
 * <p>
 * 
 * <p>
 * Basic usage:
 * 
 * <pre>
 * &#064;RunWith(SpringJUnit4ClassRunner.class)
 * &#064;ContextConfiguration(loader = SpringockitoReplacingBeanContextLoader.class,
 * 		locations = &quot;classpath:spring/context/contextExample.xml&quot;)
 * public class MyTest {
 * 
 * 	&#064;ReplaceWithMock
 * 	public Int1 other2;
 * 
 * 	&#064;ReplaceContextBean(name = &quot;class12&quot;, factoryMethod = &quot;createOther2&quot;)
 * 	public Int1 other2;
 * }
 * </pre>
 * 
 * @author John Butler
 * 
 */
public class SpringockitoReplacingBeanContextLoader extends SpringockitoContextLoader {

	private final ReplacingBeanContextLoader wrappedLoader = new ReplacingBeanContextLoader();

	@Override
	protected void customizeContext(GenericApplicationContext context) {
		super.customizeContext(context);

		wrappedLoader.registerBeans(context);
	}

	@Override
	protected String[] generateDefaultLocations(Class<?> clazz) {
		wrappedLoader.inspectClass(clazz);
		return super.generateDefaultLocations(clazz);
	}

	@Override
	protected String[] modifyLocations(Class<?> arg0, String... arg1) {
		wrappedLoader.inspectClass(arg0);
		return super.modifyLocations(arg0, arg1);
	}
}
