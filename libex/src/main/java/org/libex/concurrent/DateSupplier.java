/**
 * Copyright 2012 John Butler
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language erning permissions and
 * limitations under the License.
 */
package org.libex.concurrent;

import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.ParametersAreNonnullByDefault;

import org.joda.time.DateTime;

import com.google.common.base.Supplier;

/**
 * Provides the current system {@code Date} in a test-overridable manner.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public class DateSupplier implements Supplier<Date> {

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();

    private static DateSupplier instance;

    /**
     * @return the current {@link Date} produced via {@code new Date()}
     */
    public static Date getCurrentDate() {
        return getInstance().getOverridableDate();
    }

    /**
     * @return the current {@link DateTime} produced via {@code new Date()}
     */
    public static DateTime getCurrentDateTime() {
        return getInstance().getOverridableDateTime();
    }

    /**
     * @return the current time produced via {@code new Date().getTime()}
     */
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

    /**
     * Allows for setting the singleton instance in a test environment.
     * 
     * @param instance
     *            the replacement instance
     */
    protected static void setInstance(final DateSupplier instance) {

        writeLock.lock();
        try {
            DateSupplier.instance = instance;
        } finally {
            writeLock.unlock();
        }
    }

    protected DateSupplier() {
    }

    /**
     * Method that should be overridden in a test environment to control the
     * date.
     * 
     * @return the current {@link Date}
     */
    protected Date getOverridableDate() {
        return new Date();
    }

    /**
     * Method that should be overridden in a test environment to control the
     * date.
     * 
     * @return the current {@link DateTime}
     */
    protected DateTime getOverridableDateTime() {
        return new DateTime();
    }

    @Override
    public Date get()
    {
        return getOverridableDate();
    }
}
