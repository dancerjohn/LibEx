package org.libex.additions.rest.role;

import javax.annotation.Nonnull;

public interface Role<Permission> extends HasPermissions<Permission> {
	@Nonnull
	String name();

	@Nonnull
	default String getName() {
		return name();
	}
}
