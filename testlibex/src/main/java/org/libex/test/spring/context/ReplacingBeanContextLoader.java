package org.libex.test.spring.context;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import junit.framework.Assert;

import org.libex.annotation.DefaultScope;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class ReplacingBeanContextLoader extends GenericXmlContextLoader {

	private Map<String, Object> beans = Maps.newHashMap();
	private List<Method> generateContextBeanReplacementsMethods = newArrayList();

	@Nullable
	private Object testClassInstance = null;

	@Override
	protected void customizeContext(GenericApplicationContext context) {
		super.customizeContext(context);

		registerBeans(context);
	}

	@DefaultScope
	void registerBeans(GenericApplicationContext context) {
		for (Entry<String, Object> bean : beans.entrySet()) {
			context.registerBeanDefinition(bean.getKey(), createBeanDefinition(bean.getValue()));
		}
	}

	@Override
	protected String[] generateDefaultLocations(Class<?> clazz) {
		inspectClass(clazz);
		return super.generateDefaultLocations(clazz);
	}

	@Override
	protected String[] modifyLocations(Class<?> arg0, String... arg1) {
		inspectClass(arg0);
		return super.modifyLocations(arg0, arg1);
	}

	@DefaultScope
	void inspectClass(Class<?> type) {
		createTestClassInstance(type);
		findGenerateBeansReplacementsMethod(type);
		invokeGenerateBeansReplacementMethod();
		findReplacesBeans(type);
	}

	private void createTestClassInstance(Class<?> type) {
		if (testClassInstance == null) {
			try {
				testClassInstance = type.newInstance();
			} catch (Exception e1) {
				e1.printStackTrace();

				Assert.fail(e1.getMessage());
			}
		}
	}

	private void findGenerateBeansReplacementsMethod(Class<?> type) {
		for (Method method : type.getDeclaredMethods()) {
			if (method.getAnnotation(GenerateContextBeanReplacements.class) != null) {
				this.generateContextBeanReplacementsMethods.add(method);
			}
		}
	}

	private void invokeGenerateBeansReplacementMethod() {
		try {
			for (Method method : generateContextBeanReplacementsMethods) {
				@SuppressWarnings("unchecked")
				Map<String, Object> newBeans = (Map<String, Object>) method.invoke(testClassInstance);
				beans.putAll(newBeans);
			}
		} catch (Exception e) {
			e.printStackTrace();

			Assert.fail(e.getMessage());
		}

	}

	public AbstractBeanDefinition createBeanDefinition(Object bean) {
		GenericBeanDefinition def = new GenericBeanDefinition();
		def.setBeanClass(bean.getClass());
		def.setSource(bean);
		return def;
	}

	private void findReplacesBeans(Class<?> type) {
		Field[] fields = type.getDeclaredFields();
		for (Field field : fields) {
			ReplaceContextBean annotation = field.getAnnotation(ReplaceContextBean.class);
			if (annotation != null) {

				String replaceName = getBeanName(annotation, field);
				Object bean = getBeanInstance(annotation, type, field);
				beans.put(replaceName, bean);
			}
		}
	}

	private String getBeanName(ReplaceContextBean annotation, Field field) {
		String fieldName = field.getName();
		String replaceName = Objects.firstNonNull(Strings.emptyToNull(annotation.name()), fieldName);
		return replaceName;
	}

	private Object getBeanInstance(ReplaceContextBean annotation, Class<?> type, Field field) {
		try {
			Object result;
			if (annotation.factoryMethod().isEmpty()) {
				result = field.get(testClassInstance);
			} else {
				Method factoryMethod = type.getDeclaredMethod(annotation.factoryMethod());
				result = factoryMethod.invoke(testClassInstance);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();

			Assert.fail(e.getMessage());
			return null; // Cannot get here
		}
	}
}
