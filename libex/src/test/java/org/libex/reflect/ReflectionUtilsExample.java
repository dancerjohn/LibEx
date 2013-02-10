package org.libex.reflect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.Ignore;

@Ignore
public class ReflectionUtilsExample {

	@Nonnull
	@Deprecated
	public String deprecatedAndNonnull() {
		return null;
	}

	@Deprecated
	public String deprecated() {
		return null;
	}

	@Nonnull
	public String nonnull() {
		return null;
	}

	@Nullable
	public String nullable() {
		return null;
	}

	@Nullable
	private String privateNullable() {
		return null;
	}

	@Nullable
	protected String protectedNullable() {
		return null;
	}

	public String noAnnotations() {
		return null;
	}

}
