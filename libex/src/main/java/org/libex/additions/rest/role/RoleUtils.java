package org.libex.additions.rest.role;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleUtils {

	@Nonnull
	public static final <RoleType extends Role<?>> Iterable<String> getRoleNames(
			@Nullable final HasRoles<RoleType> hasRoles) {
		return (hasRoles == null) ? new ArrayList<>() :
				StreamSupport.stream(hasRoles.getRoles().spliterator(), false)
						.map(Role::getName)
						.collect(Collectors.toList());
	}

	@Nonnull
	public static final <Permission, RoleType extends Role<Permission>> Iterable<Permission> getPermissions(
			@Nullable final HasRoles<RoleType> hasRoles) {
		return (hasRoles == null) ? new ArrayList<>() :
				StreamSupport.stream(hasRoles.getRoles().spliterator(), false)
						.flatMap(p -> StreamSupport.stream(p.getPermissions().spliterator(), false))
						.collect(Collectors.toList());
	}
}
