package org.libex.additions.rest.role.spring;

import lombok.experimental.UtilityClass;

import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import static org.libex.additions.rest.role.spring.PermissionVoter.*;

@UtilityClass
public class RoleGrantedAuthority {

	public static final String ROLE_PREFIX = new RoleVoter().getRolePrefix();

	public static String toRoleName(final String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		return role.startsWith(ROLE_PREFIX) ? role : ROLE_PREFIX + role;
	}

	public static SimpleGrantedAuthority toRoleGrantedAuthority(final String role) {
		return new SimpleGrantedAuthority(toRoleName(role));
	}

	public static String toPermissionName(final String permission) {
		Assert.hasText(permission, "A granted authority textual representation is required");
		return permission.startsWith(PERMISSION_PREFIX) ? permission : PERMISSION_PREFIX + permission;
	}

	public static SimpleGrantedAuthority toPermissionGrantedAuthority(final String permission) {
		return new SimpleGrantedAuthority(toPermissionName(permission));
	}
}
