package org.libex.test.exampleinput.expected;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import lombok.Data;
import lombok.experimental.Accessors;

import org.joda.time.DateTime;

@ParametersAreNonnullByDefault
@ThreadSafe
public class TestModifiableExampleInputWithExpectedValues {

    @Data
    @Accessors(chain = true, fluent = true)
    public static class SubClass1 {
        private DateTime date;
        private String value1;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public static class SubClass2 {
        private boolean isTest;
        private int intValue;
    }

    @Data
    @Accessors(chain = true, fluent = true)
    public static class RootValues {
        private SubClass1 class1;
        private SubClass2 class2;
        private List<String> names;
    }

    @Data
    @Accessors(chain = true)
    public static class SubClass21 {
        private DateTime date;
        private String value1;
    }

    @Data
    @Accessors(chain = true)
    public static class SubClass22 {
        private boolean isTest;
        private int intValue;
    }

    @Data
    @Accessors(chain = true)
    public static class RootValues2 {
        private SubClass21 class1;
        private SubClass22 class2;
        private List<String> names;
    }
    public TestModifiableExampleInputWithExpectedValues() {
    }

}
