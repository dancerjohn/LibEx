package org.libex.base;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.libex.primitives.tiny.integer.NaturalInteger;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class StringNumerics {

    /**
     * Trims decimal places from a numeric string.
     *
     * @param numericString
     *            the numeric String
     * @param numberOfDecimalPlaces
     *            the number of characters after the first "." to keep
     * @return the substring of passed string
     */
    @Nullable
    public static String limitDecimalPlaces(
            @Nullable String numericString,
            NaturalInteger numberOfDecimalPlaces)
    {
        if (isNullOrEmpty(numericString)) {
            return numericString;
        }

        int decimalLocation = numericString.indexOf(".");
        int newLength = decimalLocation + numberOfDecimalPlaces.getInt() + 1;
        if (decimalLocation == -1 ||
                numericString.length() < newLength) {
            return numericString;
        } else {
            return numericString.substring(0, newLength);
        }
    }

    private StringNumerics() {
    }
}
