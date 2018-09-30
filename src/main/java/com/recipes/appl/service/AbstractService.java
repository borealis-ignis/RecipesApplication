package com.recipes.appl.service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.recipes.appl.exception.RecipeException;


/**
 * @author Kastalski Sergey
 */
public abstract class AbstractService {
	
	@Autowired
	private MessageSource messageSource;
	
	
	protected <T, E extends T> ResponseEntity<T> getErrorResponseMessage(final String messageCode, final Class<E> clazz) {
		final String message = messageSource.getMessage(new DefaultMessageSourceResolvable(messageCode), LocaleContextHolder.getLocale());
		
		try {
			final Constructor<E> constructor = clazz.getConstructor(String.class);
			final E object = constructor.newInstance(new Object[] { message });
			return new ResponseEntity<T>(object, HttpStatus.BAD_REQUEST);
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RecipeException("Error of creating ResponseEntity", e);
		}
	}
}
