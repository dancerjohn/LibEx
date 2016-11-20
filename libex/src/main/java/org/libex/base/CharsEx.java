package org.libex.base;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Predicate;

@ParametersAreNonnullByDefault
@ThreadSafe
public final class CharsEx {

    private static final Predicate<Character> IS_DIGIT = new Predicate<Character>() {

        @Override
        public boolean apply(@Nullable Character input) {
            return input != null && Character.isDigit(input);
        }
    };

    public static Predicate<Character> isDigit() {
        return IS_DIGIT;
    }

    private static final Predicate<Character> IS_LOWER_CASE = new Predicate<Character>() {

        @Override
        public boolean apply(@Nullable Character input) {
            return input != null && Character.isLowerCase(input);
        }
    };

    public static Predicate<Character> isLowerCase() {
        return IS_LOWER_CASE;
    }

    private static final Predicate<Character> IS_UPPER_CASE = new Predicate<Character>() {

        @Override
        public boolean apply(@Nullable Character input) {
            return input != null && Character.isUpperCase(input);
        }
    };

    public static Predicate<Character> isUpperCase() {
        return IS_UPPER_CASE;
    }

    private CharsEx() {
    }
}
