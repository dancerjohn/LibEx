package org.libex.validation.validator;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SizeValidatorForIterableTest {

    @Size
    private List<?> defaultSize;

    @Size(min = 1)
    private List<?> notEmpty;

    @Size(min = 3, max = 5)
    private List<?> minAndMax;

    @Mock
    public Collection<?> mockedList;

    private SizeValidatorForIterable validator;

    @Test
    public void testDefault()
    {
        // setup
        validator = new SizeValidatorForIterable();
        validator.initialize(getAttotationOf("defaultSize"));

        // test
        assertThat(validator.isValid(null, null), is(true));
        assertListOfSize(0, true);
        assertListOfSize(1, true);
        assertMockedListOfSize(Integer.MAX_VALUE, true);
    }

    @Test
    public void testNotEmpty()
    {
        // setup
        validator = new SizeValidatorForIterable();
        validator.initialize(getAttotationOf("notEmpty"));

        // test
        assertListOfSize(0, false);
        assertListOfSize(1, true);
        assertMockedListOfSize(Integer.MAX_VALUE, true);
    }

    @Test
    public void testMinAndMax()
    {
        // setup
        validator = new SizeValidatorForIterable();
        validator.initialize(getAttotationOf("minAndMax"));

        // test
        for (int i = 0; i < 3; i++) {
            assertListOfSize(i, false);
        }

        for (int i = 3; i <= 5; i++) {
            assertListOfSize(i, true);
        }

        for (int i = 6; i < 10; i++) {
            assertListOfSize(i, false);
        }

        assertMockedListOfSize(Integer.MAX_VALUE, false);
    }

    private void assertListOfSize(final int size, final boolean isValid)
    {
        assertThat(validator.isValid(createListOfSize(size), null), is(isValid));
    }

    private void assertMockedListOfSize(final int size, final boolean isValid)
    {
        when(mockedList.size()).thenReturn(size);
        assertThat(validator.isValid(mockedList, null), is(isValid));
    }

    private List<?> createListOfSize(final int size)
    {
        List<String> list = newArrayList();
        for (int i = 0; i < size; i++) {
            list.add("");
        }
        return list;
    }

    private Size getAttotationOf(final String fieldName)
    {
        for (Field field : this.getClass().getDeclaredFields()) {
            String name = field.getName();
            if (name.equals(fieldName)) {
                return field.getAnnotation(Size.class);
            }
        }

        throw new IllegalStateException("annotation not found");
    }
}
