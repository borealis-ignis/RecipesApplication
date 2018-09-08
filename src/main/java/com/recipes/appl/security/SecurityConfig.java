package com.recipes.appl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Kastalski Sergey
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private AuthenticationSuccessHandler successHandler;
	
	@Value("${security.admin.login}")
	private String adminLogin;
	
	@Value("${security.admin.password}")
	private String adminPassword;
	
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
					.antMatchers("/css/**", "/js/**").permitAll()
					.antMatchers("/recipes", "/recipe", "/login", "/403").permitAll()
					.antMatchers("/admin/ingredients").hasAnyRole("ADMIN")
					.antMatchers("/admin/recipes").hasAnyRole("ADMIN")
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
		auth.inMemoryAuthentication().withUser(adminLogin).password("{noop}" + adminPassword).roles("ADMIN");
	}
	
}
