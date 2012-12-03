/**
 * Copyright 2012 John Butler
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.libex.concurrent;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public class DateSupplier {

	private static final ReadWriteLock lock = new ReentrantReadWriteLock();
	private static final Lock readLock = lock.readLock();
	private static final Lock writeLock = lock.writeLock();

	private static DateSupplier instance;

	public static Date getCurrentDate() {
		return getInstance().getOverridableDate();
	}

	public static long getCurrentDateInMilliseconds() {
		return getCurrentDate().getTime();
	}

	protected static DateSupplier getInstance() {
		readLock.lock();
		try {
			if (instance != null) {
				return instance;
			}
		} finally {
			readLock.unlock();
		}

		writeLock.lock();
		try {
			if (instance == null) {
				instance = new DateSupplier();
			}

			return instance;
		} finally {
			writeLock.unlock();
		}
	}

	protected static void setInstance(DateSupplier instance) {

		writeLock.lock();
		try {
			DateSupplier.instance = instance;
		} finally {
			writeLock.unlock();
		}
	}

	protected DateSupplier() {
	}

	protected Date getOverridableDate() {
		return new Date();
	}
}
