package org.libex.test.gettersetter.object;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.joda.time.DateTime;
import org.libex.test.gettersetter.BaseGetterSetterTester;

@ParametersAreNonnullByDefault
@ThreadSafe
public class DateTimeGetterSetterTester extends BaseGetterSetterTester<DateTime> {

	private static final List<DateTime> values = newArrayList(
			null, new DateTime(0), new DateTime(), new DateTime(Long.MAX_VALUE));

	public DateTimeGetterSetterTester() {
		super(DateTime.class, values);
	}
}
