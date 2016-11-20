package org.libex.test.spring.context;

import static com.google.common.collect.Lists.newArrayList;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.junit.Assert;
import org.libex.annotation.DefaultScope;
import org.libex.reflect.ReflectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

/**
 * ContextLoader to be used in tests to allow for the replacement of beans into
 * a Spring context. This is used in conjunction with the {@link org.springframework.test.context.ContextConfiguration}
 * as the
 * <b>loader</b>.
 * <p>
 * The following annotations are used for determining beans to be injected into the context:
 * <p>
 * {@link ReplaceContextBean}
 * <p>
 * {@link GenerateContextBeanReplacements}
 * <p>
 * Basic usage:
 *
 * <pre>
 * &#064;RunWith(SpringJUnit4ClassRunner.class)
 * &#064;ContextConfiguration(loader = ReplacingBeanContextLoader.class,
 *         locations = &quot;classpath:spring/context/contextExample.xml&quot;)
 * public class MyTest {
 *
 *     &#064;ReplaceContextBean(name = &quot;class12&quot;, factoryMethod = &quot;createOther2&quot;)
 *     public Int1 other2;
 * }
 * </pre>
 *
 * @author John Butler
 *
 */
public class ReplacingBeanContextLoader extends GenericXmlContextLoader {

    private Map<String, Object> beans = Maps.newHashMap();
    private Map<String, BeanDefinition> beanDefinitions = Maps.newHashMap();
    private List<Method> generateContextBeanReplacementsMethods = newArrayList();

    @Nullable
    private Object testClassInstance = null;

    @Override
    protected void customizeContext(final GenericApplicationContext context)
    {
        super.customizeContext(context);

        registerBeans(context);
    }

    @DefaultScope
    void registerBeans(final GenericApplicationContext context)
    {
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        for (Entry<String, Object> bean : beans.entrySet()) {
            if (beanFactory.containsBean(bean.getKey())) {
                beanFactory.removeBeanDefinition(bean.getKey());
            }
            beanFactory.registerSingleton(bean.getKey(), bean.getValue());
        }

        for (Entry<String, BeanDefinition> bean : beanDefinitions.entrySet()) {
            if (beanFactory.containsBeanDefinition(bean.getKey())
                    || beanFactory.containsBean(bean.getKey())) {
                beanFactory.removeBeanDefinition(bean.getKey());
            }
            beanFactory.registerBeanDefinition(bean.getKey(), bean.getValue());
        }
    }

    @Override
    protected String[] generateDefaultLocations(final Class<?> clazz)
    {
        inspectClass(clazz);
        return super.generateDefaultLocations(clazz);
    }

    @Override
    protected String[] modifyLocations(final Class<?> arg0, final String... arg1)
    {
        inspectClass(arg0);
        return super.modifyLocations(arg0, arg1);
    }

    @DefaultScope
    void inspectClass(final Class<?> type)
    {
        findGenerateBeansReplacementsMethod(type);
        invokeGenerateBeansReplacementMethod(type);
        findReplacesBeans(type);
        addReplacementBeanContextConfigurationReplacementBeans(type);
    }

    private void createTestClassInstance(final Class<?> type)
    {
        if (testClassInstance == null) {
            try {
                testClassInstance = type.newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();

                Assert.fail(e1.getMessage());
            }
        }
    }

    private void findGenerateBeansReplacementsMethod(final Class<?> type)
    {
        for (Method method : ReflectionUtils.getMethodsUpTo(type, Object.class)) {
            if (method.getAnnotation(GenerateContextBeanReplacements.class) != null) {
                method.setAccessible(true);
                this.generateContextBeanReplacementsMethods.add(method);
            }
        }
    }

    private void invokeGenerateBeansReplacementMethod(final Class<?> type)
    {
        try {
            for (Method method : generateContextBeanReplacementsMethods) {
                createTestClassInstance(type);

                @SuppressWarnings("unchecked")
                Map<String, Object> newBeans = (Map<String, Object>) method.invoke(testClassInstance);
                beans.putAll(newBeans);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Assert.fail(e.getMessage());
        }
    }

    private void addReplacementBeanContextConfigurationReplacementBeans(final Class<?> type)
    {
        try {
            ReplacementBeanContextConfiguration contextConfiguration = type
                    .getAnnotation(ReplacementBeanContextConfiguration.class);
            if (contextConfiguration != null &&
                    contextConfiguration.locations().length > 0) {

                try (ClassPathXmlApplicationContext context =
                        new ClassPathXmlApplicationContext(contextConfiguration.locations())) {
                    ConfigurableListableBeanFactory factory = context.getBeanFactory();
                    for (String key : factory.getBeanDefinitionNames()) {
                        beanDefinitions.put(key, factory.getBeanDefinition(key));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            Assert.fail(e.getMessage());
        }
    }

    private void findReplacesBeans(final Class<?> type)
    {
        Field[] fields = ReflectionUtils.getFieldsUpTo(type, Object.class);
        for (Field field : fields) {
            ReplaceContextBean annotation = field.getAnnotation(ReplaceContextBean.class);
            if (annotation != null) {
                field.setAccessible(true);
                String replaceName = getBeanName(annotation, field);
                Object bean = getBeanInstance(annotation, type, field);
                beans.put(replaceName, bean);
            }
        }
    }

    private String getBeanName(final ReplaceContextBean annotation, final Field field)
    {
        String fieldName = field.getName();
        String replaceName = Objects.firstNonNull(Strings.emptyToNull(annotation.name()), fieldName);
        return replaceName;
    }

    private Object getBeanInstance(final ReplaceContextBean annotation, final Class<?> type, final Field field)
    {
        try {
            createTestClassInstance(type);

            Object result;
            if (annotation.factoryMethod().isEmpty()) {
                result = field.get(testClassInstance);
            }
            else {
                Method factoryMethod = field.getDeclaringClass().getDeclaredMethod(annotation.factoryMethod());
                factoryMethod.setAccessible(true);
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
