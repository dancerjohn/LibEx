package org.libex.test.spring.context;

import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.context.support.GenericApplicationContext;

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
