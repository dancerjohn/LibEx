package org.libex.examples.aop;

import java.util.Random;

public class Profiled {

	private int waitValue = 4000;

	public void setWaitValue(int waitValue) {
		this.waitValue = waitValue;
	}

	public void method() throws InterruptedException {
		Random random = new Random();
		int value = (random.nextInt(waitValue) + 1);
		System.out.println("sleeping " + value);
		Thread.sleep(value);
	}

	@Override
	public String toString() {
		return "Profiled";
	}
}
