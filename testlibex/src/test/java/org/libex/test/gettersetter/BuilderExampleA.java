package org.libex.test.gettersetter;

import lombok.Getter;
import lombok.experimental.Builder;

import org.joda.time.DateTime;

@Builder
@Getter
public class BuilderExampleA {

	private int intValue;
	private DateTime dateTimeValue;
}
