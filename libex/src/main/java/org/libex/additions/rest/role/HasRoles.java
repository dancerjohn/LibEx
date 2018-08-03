package org.libex.additions.rest.role;

import javax.annotation.Nonnull;

public interface HasRoles<RoleType extends Role<?>> {

	@Nonnull
	Iterable<RoleType> getRoles();
}
