package org.libex.additions.rest.role.spring;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.method.DelegatingMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
public class PermissionMethodSecurityConfiguration
		extends GlobalMethodSecurityConfiguration {

	@Override
	protected AccessDecisionManager accessDecisionManager() {
		AffirmativeBased result = (AffirmativeBased) super.accessDecisionManager();
		List<AccessDecisionVoter<? extends Object>> decisionVoters = result.getDecisionVoters();

		decisionVoters.add(new PermissionVoter());
		return result;
	}

	@Bean
	public MethodSecurityMetadataSource methodSecurityMetadataSource() {
		DelegatingMethodSecurityMetadataSource source = (DelegatingMethodSecurityMetadataSource) super
				.methodSecurityMetadataSource();
		source.getMethodSecurityMetadataSources().add(new PermissionsMethodSecurityMetadataSource());
		return source;
	}
}
