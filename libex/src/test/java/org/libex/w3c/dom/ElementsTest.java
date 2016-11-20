package org.libex.w3c.dom;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Function;

public class ElementsTest {

    private static final Function<Node, Element> TO_ELEMENT = Elements.toElement();

    @Mock
    public Element element;

    @Mock
    public Attr attr;

    @Before
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNull()
    {
        assertThat(TO_ELEMENT.apply(null), nullValue());
    }

    @Test
    public void testElement()
    {
        assertThat(TO_ELEMENT.apply(element), sameInstance(element));
    }

    @Test(expected = ClassCastException.class)
    public void testAttr(){
        TO_ELEMENT.apply(attr);
    }
}
