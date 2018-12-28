package com.recipes.appl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Kastalski Sergey
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private AccessDeniedHandler accessDeniedHandler;
	
	private AuthenticationSuccessHandler successHandler;
	
	private String adminLogin;
	
	private String adminPassword;
	
	@Autowired
	public SecurityConfig(
			final AccessDeniedHandler accessDeniedHandler, 
			final AuthenticationSuccessHandler successHandler,
			@Value("${security.admin.login}") final String adminLogin,
			@Value("${security.admin.password}") final String adminPassword) {
		super();
		this.accessDeniedHandler = accessDeniedHandler;
		this.successHandler = successHandler;
		this.adminLogin = adminLogin;
		this.adminPassword = adminPassword;
	}
	
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
					.antMatchers("/css/**", "/js/**", "/images/**", "/recipes", "/recipe", "/login", "/403").permitAll()
					.antMatchers("/admin/statics", "/admin/ingredients", "/admin/recipes").hasRole(RolesEnum.ADMIN_ROLE.getValue())
					.anyRequest().authenticated()
				.and()
				.formLogin().successHandler(successHandler)
					.loginPage("/login")
					.permitAll()
				.and()
				.logout()
					.invalidateHttpSession(true)
                	.clearAuthentication(true)
					.permitAll()
				.and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}
	
	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		auth.inMemoryAuthentication().passwordEncoder(encoder).withUser(adminLogin).password(adminPassword).roles(RolesEnum.ADMIN_ROLE.getValue());
	}
}
