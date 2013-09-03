package org.libex.test.theories.suppliers;

import static com.google.common.collect.Lists.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.libex.test.theories.suppliers.BottomBeanTestSupplier.InvalidBottomBean;
import org.libex.test.theories.suppliers.BottomBeanTestSupplier.MultipleValidBottomBean;
import org.libex.test.theories.suppliers.BottomBeanTestSupplier.SingleValidBottomBean;
import org.libex.test.theories.suppliers.MiddleBean.State;
import org.libex.test.theories.suppliers.MiddleBeanTestSupplier.MiddleBeansWithState;
import org.libex.test.theories.suppliers.MiddleBeanTestSupplier.SingleMiddleBean;
import org.libex.test.theories.suppliers.TopBeanTestSupplier.DefaultTopBean;

@RunWith(Theories.class)
public class ExampleTest {

	private static int test1Count = 0;

	@Theory
	public void test1(@DefaultTopBean @SingleMiddleBean @SingleValidBottomBean TopBean topBean) {
		assertThat(topBean.getField1(), notNullValue());

		MiddleBean middleBean = topBean.getMiddleBean();
		assertThat(middleBean.getState(), is(MiddleBean.State.State1));

		List<BottomBean> bottomBeans = middleBean.getBottomBeans();
		assertThat(bottomBeans, IsCollectionWithSize.hasSize(1));

		BottomBean bottomBean = bottomBeans.get(0);
		assertThat(bottomBean.getField1(), equalTo(BottomBeanTestSupplier.DEFAULT_FIELD1 + 0));
		assertThat(bottomBean.getValue(), equalTo(BottomBeanTestSupplier.DEFAULT_VALUE));

		test1Count++;
		assertThat(test1Count, is(1));
	}

	private static int test2Count = 0;

	@Theory
	public void test2(@DefaultTopBean @SingleMiddleBean @InvalidBottomBean TopBean topBean) {
		assertThat(topBean.getField1(), notNullValue());

		MiddleBean middleBean = topBean.getMiddleBean();
		assertThat(middleBean.getState(), is(MiddleBean.State.State1));

		List<BottomBean> bottomBeans = middleBean.getBottomBeans();
		assertThat(bottomBeans, IsCollectionWithSize.hasSize(1));

		BottomBean bottomBean = bottomBeans.get(0);
		assertThat(bottomBean.getField1(), equalTo("blah"));
		assertThat(bottomBean.getValue(), equalTo(-123));

		test2Count++;
		assertThat(test2Count, is(1));
	}

	private static int test3Count = 0;
	private static List<State> observedStates = newArrayList();

	// This test will fail the second time due to the count being 2 instead of
	// 1.
	// This shows that since to TopBeans were provided the test entered twice.
	@Theory
	public void test3(
			@DefaultTopBean @MiddleBeansWithState(states = { State.State1, State.State3 }) @MultipleValidBottomBean(count = 2) TopBean topBean) {
		System.out.println(topBean.getField1());
		assertThat(topBean.getField1(), notNullValue());

		MiddleBean middleBean = topBean.getMiddleBean();
		observedStates.add(middleBean.getState());
		if (test3Count != 0) {
			assertThat(observedStates, IsIterableContainingInAnyOrder.containsInAnyOrder(State.State1, State.State3));
		}

		List<BottomBean> bottomBeans = middleBean.getBottomBeans();
		assertThat(bottomBeans, IsCollectionWithSize.hasSize(2));

		BottomBean bottomBean = bottomBeans.get(0);
		assertThat(bottomBean.getField1(), equalTo(BottomBeanTestSupplier.DEFAULT_FIELD1 + 0));
		assertThat(bottomBean.getValue(), equalTo(BottomBeanTestSupplier.DEFAULT_VALUE + 0));

		bottomBean = bottomBeans.get(1);
		assertThat(bottomBean.getField1(), equalTo(BottomBeanTestSupplier.DEFAULT_FIELD1 + 1));
		assertThat(bottomBean.getValue(), equalTo(BottomBeanTestSupplier.DEFAULT_VALUE + 1));

		test3Count++;

		// This test will fail on the second theory
		assertThat(test3Count, is(1));
	}
}
