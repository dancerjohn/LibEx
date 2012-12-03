package org.libex.concurrent;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@ParametersAreNonnullByDefault
public class SimpleBarrier {

	private final Object lock = new Object();
	private boolean closed = false;

	public void open() {
		synchronized (lock) {
			closed = false;
			lock.notifyAll();
		}
	}

	public void close() {
		synchronized (lock) {
			closed = true;
			lock.notifyAll();
		}
	}

	public void await() throws InterruptedException {
		synchronized (lock) {
			if (closed) {
				lock.wait();
			}
		}
	}
}
