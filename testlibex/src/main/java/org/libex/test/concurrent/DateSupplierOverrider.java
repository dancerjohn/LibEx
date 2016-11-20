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
package org.libex.test.concurrent;

import static com.google.common.base.Objects.*;

import java.util.Date;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.joda.time.DateTime;
import org.libex.concurrent.DateSupplier;

/**
 * Overrides {@link DateSupplier} to allow control in a Unit test environment.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
class DateSupplierOverrider extends DateSupplier {

    private static DateSupplierOverrider instance;

    protected synchronized static DateSupplierOverrider getInstance() {
        if (null == instance) {
            DateSupplierOverrider.instance = new DateSupplierOverrider();
            DateSupplier.setInstance(instance);
        }

        return instance;
    }

    @Nullable
    private DateTime currentTime = null;

    /**
     * Constructor
     */
    private DateSupplierOverrider() {
    }

    @Override
    protected Date getOverridableDate() {
        DateTime dateTime = firstNonNull(currentTime, super.getOverridableDateTime());
        return new Date(dateTime.getMillis());
    }

    @Override
    protected DateTime getOverridableDateTime() {
        return firstNonNull(currentTime, super.getOverridableDateTime());
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the passed {@code date}
     * 
     * @param date
     *            the {@code Date} to which to set the current time
     */
    void setCurrentTime(Date date) {
        currentTime = new DateTime(date.getTime());
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the passed {@code date}
     * 
     * @param date
     *            the {@code DateTime} to which to set the current time
     */
    void setCurrentTime(DateTime date) {
        currentTime = date;
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the current system time
     */
    void setCurrentTimeToNow() {
        currentTime = super.getOverridableDateTime();
    }

    /**
     * Resets the {@link DateSupplier} to the default behavior (returning the current time)
     */
    void reset() {
        currentTime = null;
    }
}
