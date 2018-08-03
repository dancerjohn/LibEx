package org.libex.additions.rest.role.spring;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Singular;
import lombok.experimental.UtilityClass;
import org.libex.additions.rest.role.HasRoles;
import org.libex.additions.rest.role.Role;
import org.libex.additions.rest.role.RoleUtils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class SpringRoleUtils {

	public static Collection<GrantedAuthority> getRolesAsGrantedAuthorities(
			@Nullable final HasRoles<?> hasRoles) {
		return StreamSupport.stream(RoleUtils.getRoleNames(hasRoles).spliterator(), false)
				.map(RoleGrantedAuthority::toRoleGrantedAuthority)
				.collect(Collectors.toList());
	}

	public static <Permission, RoleType extends Role<Permission>> Collection<GrantedAuthority> getPermissionsAsGrantedAuthoritiesWithPrefix(
			@Nullable final HasRoles<RoleType> hasRoles,
			final Function<Permission, String> permissionStringFunction,
			final String permissionPrefix) {
		return getPermissionsAsGrantedAuthorities(hasRoles,
				permissionStringFunction.andThen(s -> permissionPrefix + s));
	}

	public static <Permission, RoleType extends Role<Permission>> Collection<GrantedAuthority> getPermissionsAsGrantedAuthorities(
			@Nullable final HasRoles<RoleType> hasRoles,
			final Function<Permission, String> permissionStringFunction) {
		return StreamSupport.stream(RoleUtils.getPermissions(hasRoles).spliterator(), false)
				.map(permissionStringFunction)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	public static <Permission, RoleType extends Role<Permission>> Collection<GrantedAuthority> getRolesAndPermissionsAsGrantedAuthorities(
			@Nullable final HasRoles<RoleType> hasRoles,
			final Function<HasRoles<RoleType>, Collection<GrantedAuthority>> permissionFunction) {
		return ImmutableList.<GrantedAuthority>builder()
				.addAll(getRolesAsGrantedAuthorities(hasRoles))
				.addAll(permissionFunction.apply(hasRoles))
				.build();
	}

	public static Object getUserDetails() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static <T> T getUserDetailsOf(Class<T> type) {
		Object result = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (type.isInstance(result)) {
			return type.cast(result);
		}
		else {
			return null;
		}
	}

	@Builder
	public static class PermissionConverter<Permission, RoleType extends Role<Permission>> implements
			Function<HasRoles<RoleType>, Collection<GrantedAuthority>> {

		@Builder.Default
		private final Function<? super Permission, String> permissionToStringFunction = Object::toString;

		@Singular
		private final List<String> prefixes;

		@Override
		public Collection<GrantedAuthority> apply(@Nullable HasRoles<RoleType> hasRoles) {
			return StreamSupport.stream(RoleUtils.getPermissions(hasRoles).spliterator(), false)
					.map(permissionToStringFunction)
					.flatMap(permission -> prefixes.stream().map(prefix -> prefix + permission))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		}
	}

	@Builder
	public static class RolePermissionConverter<Permission, RoleType extends Role<Permission>> implements
			Function<HasRoles<RoleType>, Collection<GrantedAuthority>> {

		@Builder.Default
		private final boolean includeRoles = true;
		@Builder.Default
		private final boolean includePermissions = true;
		@Builder.Default
		private final boolean includeRawValues = false;

		@Builder.Default
		private final Function<? super Permission, String> permissionToStringFunction = Object::toString;

		@Override
		public Collection<GrantedAuthority> apply(@Nullable HasRoles<RoleType> hasRoles) {
			Set<GrantedAuthority> result = Sets.newHashSet();
			if (includeRoles) {
				result.addAll(getRolesAsGrantedAuthorities(hasRoles));

				if (includeRawValues) {
					StreamSupport.stream(RoleUtils.getRoleNames(hasRoles).spliterator(), false)
							.map(SimpleGrantedAuthority::new)
							.forEach(result::add);
				}
			}

			if (includePermissions) {
				StreamSupport.stream(RoleUtils.getPermissions(hasRoles).spliterator(), false)
						.map(permissionToStringFunction)
						.map(RoleGrantedAuthority::toPermissionGrantedAuthority)
						.forEach(result::add);

				if (includeRawValues) {
					StreamSupport.stream(RoleUtils.getPermissions(hasRoles).spliterator(), false)
							.map(permissionToStringFunction)
							.map(SimpleGrantedAuthority::new)
							.forEach(result::add);
				}
			}
			return result;
		}
	}
}
