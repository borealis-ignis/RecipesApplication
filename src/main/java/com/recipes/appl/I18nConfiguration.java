package com.recipes.appl;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author Kastalski Sergey
 */
@Configuration
public class I18nConfiguration implements WebMvcConfigurer {
	
	@Bean(name="messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
		final ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		resource.setBasenames(
				"classpath:i18n/messages",
				"classpath:i18n/recipes/messages",
				"classpath:i18n/singlerecipe/messages",
				"classpath:i18n/login/messages",
				"classpath:i18n/admin/statics/messages",
				"classpath:i18n/admin/ingredients/messages",
				"classpath:i18n/admin/recipes/messages");
				resource.setDefaultEncoding("UTF-8");
		return resource;
	}
	
	@Bean
	public LocaleResolver localeResolver() {
		final SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(new Locale("ru", "RU"));
		return sessionLocaleResolver;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
}
