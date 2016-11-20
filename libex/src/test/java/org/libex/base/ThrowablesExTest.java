package org.libex.base;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ThrowablesExTest {

    private final String message = "this is the message";
    private final Throwable throwable = new Throwable(message);

    @Test
    public void testStackTraceAsString()
    {
        assertContainsAllStackTraceElements(ThrowablesEx.stackTraceAsString().apply(throwable));
    }

    private void assertContainsAllStackTraceElements(final String string)
    {
        for (final StackTraceElement stackElement : throwable.getStackTrace()) {
            assertThat(string, containsString(stackElement.toString()));
        }
    }

    @Test
    public void testMessage()
    {
        assertThat(ThrowablesEx.message().apply(throwable), equalTo(message));
    }

}
