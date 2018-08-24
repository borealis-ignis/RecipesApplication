package com.recipes.appl.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author Kastalski Sergey
 */
@Component
public class RecipesApplSuccessHandler implements AuthenticationSuccessHandler {
	
	private static Logger logger = LoggerFactory.getLogger(RecipesApplSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(final HttpServletRequest requestServlet, final HttpServletResponse responseServlet, final Authentication auth) throws IOException, ServletException {
		final Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		authorities.forEach(authority -> {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				try {
					redirectStrategy.sendRedirect(requestServlet, responseServlet, "/admin/ingredients"); 
				} catch (Exception e) {
					logger.error("Redirection is failed after authorization", e);
				} 
			} else { 
				throw new IllegalStateException();
			}
		});
	}

}
 