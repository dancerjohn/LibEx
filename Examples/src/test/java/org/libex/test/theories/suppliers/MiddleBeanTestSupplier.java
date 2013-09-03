package org.libex.test.theories.suppliers;

import static com.google.common.collect.Lists.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.libex.test.theories.suppliers.MiddleBean.State;

public class MiddleBeanTestSupplier extends AbstractParameterSupplier<MiddleBean> {

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface SingleMiddleBean {
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface MultipleMiddleBeans {
		int count() default 2;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@ParametersSuppliedBy(MiddleBeanTestSupplier.class)
	public static @interface MiddleBeansWithState {
		State[] states() default { State.State1 };
	}

	public static final MiddleBean.State DEFAULT_STATE = MiddleBean.State.State1;

	public static MiddleBean createMiddleBean() {
		return createMiddleBean(DEFAULT_STATE, BottomBeanTestSupplier.createBottomBean());
	}

	public static MiddleBean createMiddleBean(State state, BottomBean... beans) {
		return createMiddleBeanLocal(state, Arrays.asList(beans));
	}

	private static MiddleBean createMiddleBeanLocal(State state, List<BottomBean> bottomBeans) {
		MiddleBean bean = new MiddleBean();
		bean.setState(state);
		bean.setBottomBeans(bottomBeans);
		return bean;
	}

	public static List<MiddleBean> createMiddleBeans(ParameterSignature sig) {
		List<MiddleBean> beans = newArrayList();
		List<BottomBean> bottomBeans = BottomBeanTestSupplier.createBottomBeans(sig);

		if (sig.getAnnotation(MiddleBeansWithState.class) != null) {
			MiddleBeansWithState middleBeansWithState = sig.getAnnotation(MiddleBeansWithState.class);
			for (State state : middleBeansWithState.states()) {
				beans.add(createMiddleBeanLocal(state, bottomBeans));
			}
		} else {
			int count = 1;

			if (sig.getAnnotation(MultipleMiddleBeans.class) != null) {
				MultipleMiddleBeans multipleMiddleBean = sig.getAnnotation(MultipleMiddleBeans.class);
				count = multipleMiddleBean.count();
			}

			for (int i = 0; i < count; i++) {
				beans.add(createMiddleBeanLocal(DEFAULT_STATE, bottomBeans));
			}
		}

		return beans;
	}

	@Override
	public List<MiddleBean> getTestValues(ParameterSignature sig) {
		return createMiddleBeans(sig);
	}

	@Override
	protected String toKey(MiddleBean record) {
		return record.getState().name();
	}

}
