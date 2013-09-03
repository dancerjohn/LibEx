package org.libex.test.gettersetter;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TestClass2 {

	public TestClass2(int value) {

	}

	int intValue = 0;

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int value) {
		System.out.println("setIntValue with " + value);
		this.intValue = value + 2;
	}

}
