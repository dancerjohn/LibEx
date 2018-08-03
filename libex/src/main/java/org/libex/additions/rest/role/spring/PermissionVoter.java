package org.libex.additions.rest.role.spring;

import org.springframework.security.access.vote.RoleVoter;

public class PermissionVoter extends RoleVoter {
	public static final String PERMISSION_PREFIX = "PERMISSION_";

	public PermissionVoter() {
		this.setRolePrefix(PERMISSION_PREFIX);
	}
}
