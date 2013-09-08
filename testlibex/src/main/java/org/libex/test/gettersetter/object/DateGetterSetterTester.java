package org.libex.test.gettersetter.object;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Date;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.test.gettersetter.BaseGetterSetterTester;

@ParametersAreNonnullByDefault
@ThreadSafe
public class DateGetterSetterTester extends BaseGetterSetterTester<Date> {

	private static final List<Date> values = newArrayList(
			null, new Date(0), new Date(), new Date(Long.MAX_VALUE));

	public DateGetterSetterTester() {
		super(Date.class, values);
	}
}
