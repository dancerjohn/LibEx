package org.libex.test.gettersetter.primitive;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

@ParametersAreNonnullByDefault
@ThreadSafe
public class CharacterGetterSetterTester extends PrimitiveGetterSetterTester<Character> {

	private static final Character[] values = new Character[] { null, Character.MIN_VALUE, 'a', ' ', '1', '#', '\r', Character.MAX_VALUE };

	public CharacterGetterSetterTester() {
		super(char.class, Character.class, values);
	}
}
