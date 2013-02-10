package org.libex.enums;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

import org.junit.Test;

@ParametersAreNonnullByDefault
@ThreadSafe
public class SingularPluralTest {

	@Test
	public void testIsSingular() {
		assertThat(SingularPlural.PLURAL.isSingular(), is(false));
		assertThat(SingularPlural.SINGULAR.isSingular(), is(true));
	}

	@Test
	public void testIsPlural() {
		assertThat(SingularPlural.PLURAL.isPlural(), is(true));
		assertThat(SingularPlural.SINGULAR.isPlural(), is(false));
	}

}
