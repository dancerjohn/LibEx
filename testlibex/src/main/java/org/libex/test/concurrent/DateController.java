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

import java.util.Date;

import javax.annotation.ParametersAreNonnullByDefault;

import org.joda.time.DateTime;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.libex.concurrent.DateSupplier;

/**
 * Used to control the {@link DateSupplier} in a unit test environment.
 * 
 * @author John Butler
 * 
 */
@ParametersAreNonnullByDefault
public class DateController implements TestRule {

    private final DateSupplierOverrider timeProvider = DateSupplierOverrider
            .getInstance();

    @Override
    public Statement apply(final Statement statement, Description arg1) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } finally {
                    timeProvider.reset();
                }
            }
        };
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the passed {@code date}
     * 
     * @param date
     *            the {@code Date} to which to set the current time
     */
    public void setCurrentTime(Date date) {
        timeProvider.setCurrentTime(date);
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the passed {@code date}
     * 
     * @param date
     *            the {@code DateTime} to which to set the current time
     */
    public void setCurrentTime(DateTime date) {
        timeProvider.setCurrentTime(date);
    }

    /**
     * Sets the time returned by {@link DateSupplier} to the current system time
     */
    public void setCurrentTimeToNow() {
        timeProvider.setCurrentTimeToNow();
    }

    /**
     * Resets the {@link DateSupplier} to the default behavior (returning the current time)
     */
    public void reset() {
        timeProvider.reset();
    }

}
