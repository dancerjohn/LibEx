package org.libex.enums;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Singular / Plural enum
 * 
 * @author John Butler
 * 
 */
@ThreadSafe
@ParametersAreNonnullByDefault
public enum SingularPlural {
	SINGULAR, PLURAL;

	public boolean isSingular() {
		return this == SINGULAR;
	}

	public boolean isPlural() {
		return this == PLURAL;
	}
}
