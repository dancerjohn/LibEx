package org.libex.test.theories.suppliers;

import static com.google.common.collect.Lists.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParametersSuppliedBy;

public class TopBeanTestSupplier extends AbstractParameterSupplier<TopBean> {

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(TopBeanTestSupplier.class)
	public static @interface DefaultTopBean {
	}

	public static final String DEFAULT_FIELD1 = "topField1";

	public static TopBean createTopBean() {
		return createTopBeanLocal(DEFAULT_FIELD1, MiddleBeanTestSupplier.createMiddleBean());
	}

	public static TopBean createTopBean(String field1, MiddleBean middleBean) {
		return createTopBeanLocal(field1, middleBean);
	}

	private static TopBean createTopBeanLocal(String field1, MiddleBean middleBean) {
		TopBean bean = new TopBean();
		bean.setField1(field1);
		bean.setMiddleBean(middleBean);
		return bean;
	}

	public static List<TopBean> createTopBeans(ParameterSignature sig) {
		List<TopBean> beans = newArrayList();
		List<MiddleBean> middleBeans = MiddleBeanTestSupplier.createMiddleBeans(sig);

		for (MiddleBean middleBean : middleBeans) {
			beans.add(createTopBeanLocal(DEFAULT_FIELD1 + middleBean.getState().name(), middleBean));
		}

		return beans;
	}

	@Override
	public List<TopBean> getTestValues(ParameterSignature sig) {
		return createTopBeans(sig);
	}

	@Override
	protected String toKey(TopBean record) {
		return record.getField1();
	}

}
