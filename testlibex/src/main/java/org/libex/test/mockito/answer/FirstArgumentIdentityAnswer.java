package org.libex.test.mockito.answer;

import com.google.common.base.Functions;

public class FirstArgumentIdentityAnswer<T> extends FirstArgumentFunctionAnswer<T, T> {

    public static <T> FirstArgumentIdentityAnswer<T> create() {
        return new FirstArgumentIdentityAnswer<T>();
    }

    public FirstArgumentIdentityAnswer() {
        super(Functions.<T> identity());
    }
}
