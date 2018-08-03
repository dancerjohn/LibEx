package org.libex.additions.rest.role.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.libex.additions.rest.role.PermissionsAllowed;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.method.AbstractFallbackMethodSecurityMetadataSource;

public class PermissionsMethodSecurityMetadataSource
		extends AbstractFallbackMethodSecurityMetadataSource {
	private String defaultPermissionPrefix = PermissionVoter.PERMISSION_PREFIX;

	public PermissionsMethodSecurityMetadataSource() {
	}

	public void setDefaultRolePrefix(String defaultPermissionPrefix) {
		this.defaultPermissionPrefix = defaultPermissionPrefix;
	}

	protected Collection<ConfigAttribute> findAttributes(Class<?> clazz) {
		return this.processAnnotations(clazz.getAnnotations());
	}

	protected Collection<ConfigAttribute> findAttributes(Method method, Class<?> targetClass) {
		return this.processAnnotations(AnnotationUtils.getAnnotations(method));
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	private List<ConfigAttribute> processAnnotations(Annotation[] annotations) {
		if (annotations != null && annotations.length != 0) {
			List<ConfigAttribute> attributes = new ArrayList();
			Annotation[] var3 = annotations;
			int var4 = annotations.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				Annotation a = var3[var5];

				if (a instanceof PermissionsAllowed) {
					PermissionsAllowed ra = (PermissionsAllowed) a;
					String[] var8 = ra.value();
					int var9 = var8.length;

					for (int var10 = 0; var10 < var9; ++var10) {
						String allowed = var8[var10];
						String defaultedAllowed = this.getRoleWithDefaultPrefix(allowed);
						attributes.add(new SecurityConfig(defaultedAllowed));
					}

					return attributes;
				}
			}

			return null;
		}
		else {
			return null;
		}
	}

	private String getRoleWithDefaultPrefix(String role) {
		if (role == null) {
			return role;
		}
		else if (this.defaultPermissionPrefix != null && this.defaultPermissionPrefix.length() != 0) {
			return role.startsWith(this.defaultPermissionPrefix) ? role
					: this.defaultPermissionPrefix + role;
		}
		else {
			return role;
		}
	}
}
