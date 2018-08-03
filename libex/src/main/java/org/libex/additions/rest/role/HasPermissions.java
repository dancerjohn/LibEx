package org.libex.additions.rest.role;

import java.util.Collections;
import javax.annotation.Nonnull;

public interface HasPermissions<Permission> {

	@Nonnull
	default Iterable<Permission> getPermissions() {
		return Collections.emptyList();
	}
}
