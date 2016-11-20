package org.libex.exception;

/**
 * A {@link RuntimeException} intended to be used when the compiler requires a
 * {@code return} or {@code throws} and neither makes sense given the context.
 * Throwing of this exception indicates that method execution should not reach
 * the throw instruction under any circumstances.
 */
public class ProgrammingDefect extends RuntimeException {

    private static final long serialVersionUID = 6564812428369366721L;

    public ProgrammingDefect()
    {
    }

    public ProgrammingDefect(final String message)
    {
        super(message);
    }

    public ProgrammingDefect(final Throwable cause)
    {
        super(cause);
    }

    public ProgrammingDefect(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ProgrammingDefect(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
