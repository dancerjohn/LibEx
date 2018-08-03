package org.libex.additions.rest.role.spring;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.libex.additions.rest.role.HasRoles;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class RolePermissionFilter<EntityType extends HasRoles<?>> extends
		OncePerRequestFilter {

	private final BiFunction<HttpServletRequest, HttpServletResponse, ? extends EntityType> toEntity;
	private final Function<? super EntityType, Collection<GrantedAuthority>> toAuthorities;

	@Override
	protected void doFilterInternal(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			FilterChain filterChain)
			throws ServletException, IOException {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		if (authentication != null) {
			Collection<? extends GrantedAuthority> existingAuthorities = authentication.getAuthorities();

			Collection<GrantedAuthority> newAuthorities = toEntity.andThen(toAuthorities)
					.apply(httpServletRequest, httpServletResponse);

			if (newAuthorities != null && !newAuthorities.isEmpty()) {
				log.info("Adding new authorities {}", newAuthorities);

				Set<GrantedAuthority> newGrantedAuthoritySet = Sets.newHashSet(existingAuthorities);
				newGrantedAuthoritySet.addAll(newAuthorities);
				if (newGrantedAuthoritySet.size() > existingAuthorities.size()) {
					Authentication newAuthentication = createNewAuthenticationWithNewAuthorities(
							authentication,
							newGrantedAuthoritySet);
					context.setAuthentication(newAuthentication);
				}
			}
		}
		else {
			log.info("No authorities added because no authentication found");
		}
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private Authentication createNewAuthenticationWithNewAuthorities(
			Authentication authentication,
			Set<GrantedAuthority> newGrantedAuthoritySet) {
		Authentication newAuthentication;
		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			PreAuthenticatedAuthenticationToken existingAuthentication = (PreAuthenticatedAuthenticationToken) authentication;
			newAuthentication = new PreAuthenticatedAuthenticationToken(
					existingAuthentication.getPrincipal(), existingAuthentication.getCredentials(),
					newGrantedAuthoritySet);
		}
		else if (authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken existingAuthentication = (UsernamePasswordAuthenticationToken) authentication;
			newAuthentication = new UsernamePasswordAuthenticationToken(
					existingAuthentication.getPrincipal(), existingAuthentication.getCredentials(),
					newGrantedAuthoritySet);
		}
		else if (authentication instanceof TestingAuthenticationToken) {
			TestingAuthenticationToken existingAuthentication = (TestingAuthenticationToken) authentication;
			newAuthentication = new TestingAuthenticationToken(
					existingAuthentication.getPrincipal(), existingAuthentication.getCredentials(),
					Lists.newArrayList(newGrantedAuthoritySet));
		}
		else {
			throw new UnsupportedOperationException(
					"Cannot support instance of " + authentication.getClass());
		}
		return newAuthentication;
	}
}
