package org.libex.examples.aop;

import java.util.Random;

public class Profiled {

	public void method() throws InterruptedException {
		Random random = new Random();
		int value = (random.nextInt(4000) + 1);
		System.out.println("sleeping " + value);
		Thread.sleep(value);
	}

	@Override
	public String toString() {
		return "Profiled";
	}
}
